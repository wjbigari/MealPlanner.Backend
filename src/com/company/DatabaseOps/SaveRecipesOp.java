package com.company.DatabaseOps;

import Models.RecipeItem;
import Models.UserRecipe;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class SaveRecipesOp extends DatabaseOp{
    private String username;
    private UserRecipe userRecipe;
    private JSONObject responseObject;

    public SaveRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");
        this.userRecipe = new UserRecipe(new JSONObject(jobject.getString("recipe")));
    }
    @Override
    public JSONObject performOp() throws SQLException {
        responseObject = new JSONObject();
        updateDatabase();
        responseObject.put("response", "Successfully added Recipe");
        return responseObject;
    }

    private void updateDatabase() {
        //TODO Yash store user recipe in database using the given username
        Connection con = null;
        Statement stmt = null;
        try {
            System.out.println("Trying to save user recipe");
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

            //executing insert query for userrecipe
            stmt = con.createStatement();

            try (PreparedStatement ps = con.prepareStatement("INSERT INTO userrecipe ( username, recipename, instructions, numPortions, servingUnit, cals, carbs, prots, fats) VALUES( ?,?,?,?,?,?,?,?,?);",Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setString(2, userRecipe.getName());
                ps.setString(3, userRecipe.getPrepInstructions());
                ps.setInt(4, userRecipe.getNumPortions());
                ps.setString(5, userRecipe.getServingUnit());
                ps.setDouble(6, userRecipe.getCalPerServing());
                ps.setDouble(7, userRecipe.getCalsCarbPerServing());
                ps.setDouble(8, userRecipe.getCalsProtPerServing());
                ps.setDouble(9, userRecipe.getCalsFatPerServing());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();

//            String query = "INSERT INTO userrecipe ( username, recipename, instructions, numPortions, servingUnit, cals, carbs, prots, fats) " +
//                    "VALUES('" + username + "' , '" + this.userRecipe.getName() + "' , '" + this.userRecipe.getPrepInstructions() + "', " +
//                    " '" + this.userRecipe.getNumPortions() + "' , '" + this.userRecipe.getServingUnit() + "' , '" + this.userRecipe.getCalPerServing() + "' , " +
//                    " '" + this.userRecipe.getGramsCarbPerServing() + "' , '" + this.userRecipe.getGramsProtPerServing() + "' , '" + this.userRecipe.getGramsFatPerServing() + "');";
//            System.out.println(query);
//            stmt.executeUpdate(query);
//            System.out.println("User Recipe added successfully");
//
//            //fetching the latest added userrecipe
//            String latestRecipeId = "SELECT recipeid from userrecipe ORDER BY recipeid DESC LIMIT 1 ;";
//            System.out.println(latestRecipeId);
//            stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(latestRecipeId);
                rs.next();
                int recipeidint = rs.getInt(1);
                System.out.println(recipeidint);
                ArrayList<RecipeItem> recipeItemArrayList = userRecipe.getIngredients();    //to store ingredients of each recipe based on username
                String query;
                for (int i = 0; i < recipeItemArrayList.size(); i++) {
                    int fid = recipeItemArrayList.get(i).getFoodItem().getFoodId();         //foodId of fooditem
                    String numservs = "" + recipeItemArrayList.get(i).getNumServings();     //num of servings of foodItem in recipe
                    stmt = con.createStatement();

                    query = "INSERT INTO recipeitem " +
                            "VALUES(" + rs.getInt(1) + " , " + fid + " , '" + numservs + "');";
                    System.out.println(query);
                    stmt.executeUpdate(query);
                }
                System.out.println("RecipeItems added");
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
