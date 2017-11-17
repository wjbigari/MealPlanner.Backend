package com.company.DatabaseOps;

import Models.FoodItem;
import Models.RecipeItem;
import Models.UserRecipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;

public class GetRecipesOp extends DatabaseOp {
    private String username;
    private JSONObject responseObject;

    public GetRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");

    }
    @Override
    public JSONObject performOp() throws SQLException {
        responseObject =new JSONObject();
        grabFromDatabase();

        return responseObject;
    }


    public void grabFromDatabase() {
        JSONArray arrayList = new JSONArray();
        //TODO Yash, use username to grab all UserRecipes and store them a JSONArray list

        //members from userRecipe
        int recipeId, numPortions;
        String foodName, servingUnit, instructions;
        ArrayList<RecipeItem> ingredients = new ArrayList<RecipeItem>();

        //jdbc members
        Connection con = null;
        Statement stmt = null;
        try {
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

            //int count=0;    //counter for no of fooditems/ingredients in the recipe..... to be used for recipeitems array list
            //selecting from userRecipe table based on username
            stmt = con.createStatement();
            String query = "SELECT * FROM userrecipe WHERE username = '" + this.username + "';";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                recipeId = rs.getInt("recipeid");
                foodName = rs.getString("recipename");
                instructions = rs.getString("instructions");
                numPortions = rs.getInt("numPortions");
                servingUnit = rs.getString("servingUnit");

                //members for recipeitem
                int fid, numServings;

                //selecting from recipeItems table based on recipeId
                Statement recipeitem = null;
                Connection con2 = null;
                con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
                try{
                    recipeitem = con2.createStatement();
                    String query2 = "SELECT foodid, numServings FROM recipeitem WHERE recipeid = " + recipeId + ";";
                    ResultSet rs2 = recipeitem.executeQuery(query2);
                    while(rs2.next()){
                        fid = rs2.getInt("foodid");
                        numServings = Integer.parseInt(rs2.getString("numServings"));

                        //members for ingredients/foodItem
                        double cals, carbs, prots, fats, value;
                        String contents, unit;

                        //selecting from ingredients table based on foodid
                        Statement fooditem = null;
                        Connection con3 = null;
                        con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
                        try{
                            fooditem = con3.createStatement();
                            //this query fetches just one record
                            String query3 = "SELECT contents, cals, carbs, prots, fats, minservamt, servingname FROM ingredients WHERE foodid = " + fid + ";";
                            ResultSet rs3 = fooditem.executeQuery(query3);
                           // while(rs3.next()){
                                contents = rs3.getString("contents");
                                cals = rs3.getDouble("cals");
                                carbs = rs3.getDouble("carbs");
                                prots = rs3.getDouble("prots");
                                fats = rs3.getDouble("fats");
                                value = Double.parseDouble(rs3.getString("minservamt"));
                                unit = rs.getString("servingname");

                                //adding fooditems and numServings into ingredients (arraylist)
                                ingredients.add(new RecipeItem(new FoodItem(contents, fid, value, unit, cals, carbs, prots, fats), numServings));
                           //}
                        }catch(SQLException se){
                            //Handle errors for JDBC
                            se.printStackTrace();
                        }catch(Exception e){
                            //Handle errors for Class.forName
                            e.printStackTrace();
                        }finally {
                            //finally block used to close resources
                            try {
                                if (fooditem != null)
                                    con3.close();
                            } catch (SQLException se) {
                            }// do nothing
                            try {
                                if (con3 != null)
                                    con3.close();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        } //end finally
                    }

                }catch(SQLException se){
                    //Handle errors for JDBC
                    se.printStackTrace();
                }catch(Exception e){
                    //Handle errors for Class.forName
                    e.printStackTrace();
                }finally {
                    //finally block used to close resources
                    try {
                        if (recipeitem!= null)
                            con2.close();
                    } catch (SQLException se) {
                    }// do nothing
                    try {
                        if (con2 != null)
                            con2.close();
                    } catch (SQLException se) {
                        se.printStackTrace();
                    }
                } //end finally
                // TODO the below logic should go inside for loop for each database entry

                UserRecipe trecipe = new UserRecipe(foodName, recipeId, ingredients, numPortions, servingUnit, instructions);
                //make new with constructor
                arrayList.put(trecipe.toJson().toString());

            }
            responseObject.put("recipeList", arrayList.toString());
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally {
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
        } //end finally
    }

}
