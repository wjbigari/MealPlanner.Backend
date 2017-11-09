package com.company.DatabaseOps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpoonacularScrape {
	
	private static final int NUM_RESULTS = 15;
	
	public static void main(String[] args){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String choice;
			String query;
			System.out.println("Select a search mode:\n  1. Ingredient\n  2. Grocery Item");
			choice = in.readLine();
			if(choice.indexOf('1') > -1){
				System.out.println("Enter a ingredient search query:");
				query = in.readLine();
				getFoodItems(query);
			}else if(choice.indexOf('2') > -1) {
				System.out.println("Enter a grocery item search query:");
				query = in.readLine();
				getGroceryItems(query);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getFoodItems(String input){
		
		try {
			//1a. Search for ingredients using the specified input string
			URL searchUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/ingredients/autocomplete?metaInformation=true&number=" + NUM_RESULTS + "&query=" + input);
			HttpURLConnection request = (HttpURLConnection)searchUrl.openConnection();
			setRequestHeaders(request);
			String response = getResponse(request);
			request.disconnect();
			
			//1b. Create a list of ingredient id's from the response
			ArrayList<Integer> ingredients = new ArrayList<>();
			JSONArray responseArray = new JSONArray(response);
			for(int i = 0; i < responseArray.length(); ++i){
				JSONObject item = responseArray.getJSONObject(i);
				ingredients.add(item.getInt("id"));
			}
			String food_id = null;
			int flag=1;		//default 1
			for(Integer id : ingredients) {
				//2a. For each item, check whether this id already exists in the database
				//TODO: Run a new search of the FoodItem table for this id
				food_id = "f" + id;
				Connection con = null;
				Statement stmt = null;
				ResultSet rs = null;
				try {
					//Registering JDBC driver
					Class.forName("com.mysql.jdbc.Driver");

					//Open a connection
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

					String query = "SELECT * from ingredients " +
							"WHERE api_id = '" + food_id + "' ;";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query);


					if (!rs.next())
						flag = 0;   //item does not exist

				} catch (SQLException se) {
					se.printStackTrace();
				} catch (Exception e) {
					//Handle errors for Class.forName
					e.printStackTrace();
				} finally {
					//finally block used to close resources
					try {
						if (stmt != null)
							con.close();
					} catch (SQLException se) {
					}// do nothing
					try {
						if (con != null)
							con.close();
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}

				if (flag == 0) {
					//2b. If the item does not already exist, run a new ingredient search on that id
					URL itemUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/ingredients/" + id + "/information?amount=1&unit=serving");
					HttpURLConnection itemRequest = (HttpURLConnection) itemUrl.openConnection();
					setRequestHeaders(itemRequest);
					String itemResponse = getResponse(itemRequest);
					itemRequest.disconnect();

					//2c. From that item's nutrition facts, add a new FoodItem to the database
					JSONObject ingredient = new JSONObject(itemResponse);
					String foodName = ingredient.getString("name");
					JSONArray nutrientArray = ingredient.getJSONObject("nutrition").getJSONArray("nutrients");
					double cals = nutrientArray.getJSONObject(0).optDouble("amount", 0.0);
					double fat = nutrientArray.getJSONObject(1).optDouble("amount", 0.0);
					double carbs = nutrientArray.getJSONObject(3).optDouble("amount", 0.0);
					double prot = nutrientArray.getJSONObject(7).optDouble("amount", 0.0);

					//2d. Convert a single serving of this food item into grams
					URL unitUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/convert?ingredientName=" + foodName.replace(' ', '+') + "&sourceAmount=1&sourceUnit=serving&targetUnit=grams");
					HttpURLConnection unitRequest = (HttpURLConnection) unitUrl.openConnection();
					setRequestHeaders(unitRequest);
					String unitResponse = getResponse(unitRequest);
					unitRequest.disconnect();
					JSONObject conversion = new JSONObject(unitResponse);
					double servingValue = conversion.getDouble("targetAmount");
					String servingUnit = conversion.getString("targetUnit");

					//TODO: Add a new item to the database using the stats above
					if (servingValue > 1.0) {

						try{
							//Registering JDBC driver
							Class.forName("com.mysql.jdbc.Driver");

							//Open a connection
							con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

							//adding new item to database
							String minservamt = "" + servingValue;
							String query = "INSERT INTO ingredients(contents, cals, carbs, prots, fats, minservamt, servingname, api_id) " +
									"VALUES ('" + foodName + "' , " + cals + " , " + carbs + " , " + prot + " , " + fat + " , '"+ minservamt+"', '"+ servingUnit + "' ,'" + food_id + "');";
							stmt = con.createStatement();
							stmt.executeUpdate(query);


						}catch(SQLException se){
							se.printStackTrace();
						}catch (Exception e) {
							//Handle errors for Class.forName
							e.printStackTrace();
						} finally {
							//finally block used to close resources
							try {
								if (stmt != null)
									con.close();
							} catch (SQLException se) {
							}// do nothing
							try {
								if (con != null)
									con.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

						System.out.print("Added ingredient '" + foodName + "' (" + id + ") {Serving = " + servingValue + " " + servingUnit + "| cals: " + cals + "/carbs: " + carbs + "/prot: " + prot + "/fat: " + fat + "}\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getGroceryItems(String input){
		
		try {
			//1a. Search for grocery items using the specified input string
			URL searchUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/products/search?number=" + NUM_RESULTS + "&query=" + input);
			HttpURLConnection request = (HttpURLConnection)searchUrl.openConnection();
			setRequestHeaders(request);
			String response = getResponse(request);
			request.disconnect();
			
			//1b. Create a list of grocery item id's from the response
			ArrayList<Integer> products = new ArrayList<>();
			JSONArray responseArray = new JSONObject(response).getJSONArray("products");
			for(int i = 0; i < responseArray.length(); ++i){
				JSONObject item = responseArray.getJSONObject(i);
				products.add(item.getInt("id"));
			}

			String food_id;
			for(Integer id : products) {
				//2a. For each item, check whether this id already exists in the database
				//TODO: Run a new search of the FoodItem table for this id
				food_id = "g" + id;
				Connection con = null;
				Statement stmt = null;
				ResultSet rs = null;
				int flag = 1; //default - item
				try {
					//Registering JDBC driver
					Class.forName("com.mysql.jdbc.Driver");

					//Open a connection
					con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

					String query = "SELECT * from ingredients " +
							"WHERE api_id = '" + food_id + "' ;";
					stmt = con.createStatement();
					rs = stmt.executeQuery(query);

					if (!rs.next())
						flag = 0;   //item does not exist

				} catch (SQLException se) {
					se.printStackTrace();
				} catch (Exception e) {
					//Handle errors for Class.forName
					e.printStackTrace();
				} finally {
					//finally block used to close resources
					try {
						if (stmt != null)
							con.close();
					} catch (SQLException se) {
					}// do nothing
					try {
						if (con != null)
							con.close();
					} catch (SQLException se) {
						se.printStackTrace();
					}
				}
				//2b. If the item does not already exist, run a new grocery nutrition fact search on that id
				if (flag == 0) {

					URL itemUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/products/" + id);
					HttpURLConnection itemRequest = (HttpURLConnection) itemUrl.openConnection();
					setRequestHeaders(itemRequest);
					String itemResponse = getResponse(itemRequest);
					itemRequest.disconnect();

					//2c. From that item's nutrition facts, add a new FoodItem to the database
					JSONObject groceryItem = new JSONObject(itemResponse);
					String foodName = groceryItem.getString("title");
					String serving = groceryItem.optString("serving_size");
					double servingValue;
					String servingUnit;
					if (serving == null || serving.indexOf(' ') == -1) {
						servingValue = 1.0;
						servingUnit = "serving";
					} else {
						servingValue = Double.parseDouble(serving.substring(0, serving.indexOf(' ')));
						servingUnit = serving.substring(serving.indexOf(' ')).trim();
					}

					JSONObject nutrition = groceryItem.getJSONObject("nutrition");
					double cals = (double) nutrition.optInt("calories");
					String sFat = nutrition.optString("fat");
					double fat;
					if (sFat == null || sFat.length() == 0) {
						fat = 0.0;
					} else {
						fat = Double.parseDouble(sFat.replace("g", ""));
					}
					String sCarbs = nutrition.optString("carbs");
					double carbs;
					if (sCarbs == null || sCarbs.length() == 0) {
						carbs = 0.0;
					} else {
						carbs = Double.parseDouble(sCarbs.replace("g", ""));
					}
					String sProt = nutrition.optString("protein");
					double prot;
					if (sProt == null || sProt.length() == 0) {
						prot = 0.0;
					} else {
						prot = Double.parseDouble(sProt.replace("g", ""));
					}

					//TODO: Add a new item to the database using the stats above
					//Connection con = null;
					//Statement stmt = null;
					if(cals > 0.0) {
						try {
							//Registering JDBC driver
							Class.forName("com.mysql.jdbc.Driver");

							//Open a connection
							con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

							//adding new item to database
							String minservamt = "" + servingValue;
							String query = "INSERT INTO ingredients(contents, cals, carbs, prots, fats, minservamt, servingname, api_id) " +
									"VALUES ('" + foodName + "' , " + cals + " , " + carbs + " , " + prot + " , " + fat + " , '" + minservamt + "' , '" + servingUnit + "' , '" + food_id + "');";
							stmt = con.createStatement();
							stmt.executeUpdate(query);


						} catch (SQLException se) {
							se.printStackTrace();
						} catch (Exception e) {
							//Handle errors for Class.forName
							e.printStackTrace();
						} finally {
							//finally block used to close resources
							try {
								if (stmt != null)
									con.close();
							} catch (SQLException se) {
							}// do nothing
							try {
								if (con != null)
									con.close();
							} catch (SQLException se) {
								se.printStackTrace();
							}
						}

						System.out.print("Added grocery item '" + foodName + "' (" + id + ") {Serving = " + servingValue + " " + servingUnit + "| cals: " + cals + "/carbs: " + carbs + "/prot: " + prot + "/fat: " + fat + "}\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setRequestHeaders(HttpURLConnection request){
		try{
			request.setRequestMethod("GET");
			request.setRequestProperty("X-Mashape-Key", "I9PreUdvmWmsh0OhhD1PwuzQv3WWp10RqDOjsn1ssqzgLdjC98");
			request.setRequestProperty("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String getResponse(HttpURLConnection request){
		String line = "";
		StringBuffer response = new StringBuffer();
		
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			while((line = in.readLine()) != null) response.append(line);
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return response.toString();
	}
	
}
