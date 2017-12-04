package com.company.DatabaseOps;

import Models.*;
import org.json.JSONObject;

import java.sql.*;
import java.util.Calendar;

public class AddToFavoriesOp extends DatabaseOp {
    private String username;
    private FoodItem mealItem;
    private UserRecipe recipe;
    private JSONObject responseObject;

    private boolean isrecipe = false;
    public AddToFavoriesOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
        if(jrequest.getBoolean("isrecipe")){
            isrecipe = true;
            recipe = new UserRecipe(new JSONObject(jrequest.getString("favorite")));
        }else{
            isrecipe = false;
            mealItem = new FoodItem(new JSONObject(jrequest.getString("favorite")));
        }
    }

    @Override
    public JSONObject performOp() throws SQLException {
        responseObject = new JSONObject();

        try{
            if(isrecipe){
                storeFoodItem();
            }else{
                storeRecipe();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return responseObject;
    }

    private void storeFoodItem() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        //Open a connection
        ResultSet rs;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO favtfood(username, foodid) VALUES( ?,?);")){
            ps.setString(1, username);
            ps.setInt(2, mealItem.getFoodId());
            ps.executeUpdate();
            responseObject.put("response", "added FoodIngredient: " + mealItem.getName() + " to "+ username+"'s favorites successfully");

        }
    }

    private void storeRecipe() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");//Open a connection
        ResultSet rs;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO favtrecipe(username, recipeid) VALUES( ?,?);")){
            ps.setString(1, username);
            ps.setInt(2, recipe.getFoodId());
            ps.executeUpdate();
            responseObject.put("response", "added Recipe: " + recipe.getName() + " to "+ username+"'s favorites successfully");
        }
    }
}
