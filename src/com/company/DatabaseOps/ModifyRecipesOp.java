package com.company.DatabaseOps;

import Models.RecipeItem;
import Models.UserRecipe;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;

public class ModifyRecipesOp extends DatabaseOp {
    private String username;
    private UserRecipe userRecipe;
    public ModifyRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");
        this.userRecipe = new UserRecipe(new JSONObject(jobject.getString("recipe")));
    }
    @Override
    public JSONObject performOp() throws SQLException {
        performDatabaseOp();
        return null;
    }

    private void performDatabaseOp() {
        Connection con = null;
        Statement stmt = null;
        try {
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

            //modifying from userrecipe table
            stmt = con.createStatement();
            String selectRecipe = "SELECT recipeid, username, recipename, instructions, numPortions FROM userrecipe " +
                                   "WHERE recipeid = " + this.userRecipe.getFoodId() + " AND username = '" + this.username + "' ;";
            ResultSet recipe = stmt.executeQuery(selectRecipe);

            //to check if numPortions, instructions and recipename don't match with results from selectRecipe
            while(recipe.next()){

                //updating numPortions
                if(this.userRecipe.getNumPortions() != recipe.getInt("numPortions")){
                    String updatePortions = "UPDATE userrecipe " +
                                            "SET numPortions = " + this.userRecipe.getNumPortions() + ";";
                    stmt.executeUpdate(updatePortions);
                }

                //updating instructions
                if(this.userRecipe.getPrepInstructions() != recipe.getString("instructions")){
                    String updateInstructions = "UPDATE userrecipe " +
                                                "SET instructions = '" + this.userRecipe.getPrepInstructions() + "' ;";
                    stmt.executeUpdate(updateInstructions);
                }

                //updating recipename
                if(this.userRecipe.getName() != recipe.getString("recipename")){
                    String updateName = "UPDATE userrecipe "+
                                        "SET recipename = '" + this.userRecipe.getName() + "' ;";
                    stmt.executeUpdate(updateName);
                }

                //updating the recipeitem
                String selectRecipeItem = "SELECT foodid, numServings FROM recipeitem " +
                                            "WHERE recipeid = '" + this.userRecipe.getFoodId() + "';";
                ResultSet item = stmt.executeQuery(selectRecipeItem);

                //TODO for each recipeitem update numServings, remove foodid (add new foodid)
                ArrayList<RecipeItem> recipeItemArrayList = this.userRecipe.getIngredients();

                while(item.next()){

                }

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