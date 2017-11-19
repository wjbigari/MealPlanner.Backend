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
    private JSONObject responseObject;
    private UserRecipe userRecipe;
    public ModifyRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");
        this.userRecipe = new UserRecipe(new JSONObject(jobject.getString("userRecipe")));
    }
    @Override
    public JSONObject performOp() throws SQLException {
        performDatabaseOp();
        responseObject = new JSONObject();
        responseObject.put("response", "Recipe was modified successfully");
        return responseObject;
    }

    private void performDatabaseOp() {
        Connection con = null;
        Statement stmt1 = null, stmt = null;
        try {
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

            //modifying from userrecipe table
            stmt1 = con.createStatement();
            String selectRecipe = "SELECT recipeid, username, recipename, instructions, numPortions FROM userrecipe " +
                                   "WHERE recipeid = " + this.userRecipe.getFoodId() + " AND username = '" + this.username + "' ;";
            ResultSet recipe = stmt1.executeQuery(selectRecipe);

            stmt = con.createStatement();
            //to check if numPortions, instructions and recipename don't match with results from selectRecipe
            while(recipe.next()){
                //updating numPortions
                if(this.userRecipe.getNumPortions() != (recipe.getInt("numPortions"))){
                    String updatePortions = "UPDATE userrecipe " +
                                            "SET numPortions = " + this.userRecipe.getNumPortions() + " WHERE username = '" + this.username + "';";
                    stmt.executeUpdate(updatePortions);
                    System.out.println("numPortions updated");
                }

                //updating instructions
                if(!this.userRecipe.getPrepInstructions().equals(recipe.getString("instructions"))){
                    String updateInstructions = "UPDATE userrecipe " +
                                                "SET instructions = '" + this.userRecipe.getPrepInstructions() + "' WHERE username = '" + this.username + "';";
                    stmt.executeUpdate(updateInstructions);
                    System.out.println("instructions updated");
                }

                //updating recipename
                if(!this.userRecipe.getName().equals(recipe.getString("recipename"))){
                    String updateName = "UPDATE userrecipe "+
                                        "SET recipename = '" + this.userRecipe.getName() + "' WHERE username = '" + this.username + "';";
                    stmt.executeUpdate(updateName);
                    System.out.println("recipe name updated updated");
                }

                //updating the recipeitem
               /* String selectRecipeItem = "SELECT foodid, numServings FROM recipeitem " +
                                            "WHERE recipeid = '" + this.userRecipe.getFoodId() + "';";
                ResultSet item = stmt.executeQuery(selectRecipeItem);

                //TODO for each recipeitem update numServings, remove foodid (add new foodid)


                while(item.next()){

                } */
               //deleting from recipe item table
               String deleteRecipeItem = "DELETE FROM recipeitem WHERE recipeid = " + this.userRecipe.getFoodId() +";";
               stmt.executeUpdate(deleteRecipeItem);

                ArrayList<RecipeItem> recipeItemArrayList = this.userRecipe.getIngredients();
               //inserting updated record into recipeitem table
                for(int i = 0; i < recipeItemArrayList.size(); i++){
                    int fid = recipeItemArrayList.get(i).getFoodItem().getFoodId();         //foodId of fooditem
                    String numservs = "" + recipeItemArrayList.get(i).getNumServings();     //num of servings of foodItem in recipe

                    String updatedRecipeItem = "INSERT INTO recipeitem " +
                            "VALUES(" + this.userRecipe.getFoodId() + " , " + fid + " , '" + numservs + "');";
                    stmt.executeUpdate(updatedRecipeItem);

                }
                System.out.println("Recipe items updated successfully");

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
