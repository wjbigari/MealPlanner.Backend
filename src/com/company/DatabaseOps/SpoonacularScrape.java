package com.company.DatabaseOps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpoonacularScrape {
	
	private static final int NUM_RESULTS = 10;
	
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
			
			for(Integer id : ingredients){
				//2a. For each item, check whether this id already exists in the database
				//TODO: Run a new search of the FoodItem table for this id
				
				//2b. If the item does not already exist, run a new ingredient search on that id
				URL itemUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/ingredients/" + id + "/information?amount=1&unit=serving");
				HttpURLConnection itemRequest = (HttpURLConnection)itemUrl.openConnection();
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
				HttpURLConnection unitRequest = (HttpURLConnection)unitUrl.openConnection();
				setRequestHeaders(unitRequest);
				String unitResponse = getResponse(unitRequest);
				unitRequest.disconnect();
				JSONObject conversion = new JSONObject(unitResponse);
				double servingValue = conversion.getDouble("targetAmount");
				String servingUnit = conversion.getString("targetUnit");
				
				//TODO: Add a new item to the database using the stats above
				if(servingValue > 1.0){
					
					System.out.print("Added ingredient '" + foodName + "' (" + id + ") {Serving = " + servingValue + " " + servingUnit + "| cals: " + cals + "/carbs: " + carbs + "/prot: " + prot + "/fat: " + fat + "}\n");
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
			
			for(Integer id : products){
				//2a. For each item, check whether this id already exists in the database
				//TODO: Run a new search of the FoodItem table for this id
				
				//2b. If the item does not already exist, run a new grocery nutrition fact search on that id
				URL itemUrl = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/food/products/" + id);
				HttpURLConnection itemRequest = (HttpURLConnection)itemUrl.openConnection();
				setRequestHeaders(itemRequest);
				String itemResponse = getResponse(itemRequest);
				itemRequest.disconnect();
				
				//2c. From that item's nutrition facts, add a new FoodItem to the database
				JSONObject groceryItem = new JSONObject(itemResponse);
				String foodName = groceryItem.getString("title");
				String serving = groceryItem.optString("serving_size");
				double servingValue;
				String servingUnit;
				if(serving == null || serving.indexOf(' ') == -1){
					servingValue = 1.0;
					servingUnit = "serving";
				}else{
					servingValue = Double.parseDouble(serving.substring(0, serving.indexOf(' ')));
					servingUnit = serving.substring(serving.indexOf(' ')).trim();
				}
				JSONObject nutrition = groceryItem.getJSONObject("nutrition");
				double cals = (double)nutrition.getInt("calories");
				String sFat = nutrition.optString("fat");
				double fat;
				if(sFat == null || sFat.length() == 0){
					fat = 0.0;
				}else{
					fat = Double.parseDouble(sFat.replace("g", ""));
				}
				String sCarbs = nutrition.optString("carbs");
				double carbs;
				if(sCarbs == null || sCarbs.length() == 0){
					carbs = 0.0;
				}else{
					carbs = Double.parseDouble(sCarbs.replace("g", ""));
				}
				String sProt = nutrition.optString("protein");
				double prot;
				if(sProt == null || sProt.length() == 0){
					prot = 0.0;
				}else{
					prot = Double.parseDouble(sProt.replace("g", ""));
				}
				
				//TODO: Add a new item to the database using the stats above
				
				System.out.print("Added grocery item '" + foodName + "' (" + id + ") {Serving = " + servingValue + " " + servingUnit + "| cals: " + cals + "/carbs: " + carbs + "/prot: " + prot + "/fat: " + fat + "}\n");
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
