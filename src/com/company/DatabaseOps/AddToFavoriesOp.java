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
                storeRecipe();
            }else{
                storeFoodItem();
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
        Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
        try (PreparedStatement ps = con1.prepareStatement("SELECT * FROM favtfood WHERE username = ? AND foodid= ?;")){
            ps.setString(1, username);
            ps.setInt(2, mealItem.getFoodId());
            rs = ps.executeQuery();
            if(!rs.next()){
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
                try (PreparedStatement ps1 = con.prepareStatement("INSERT INTO favtfood(username, foodid) VALUES( ?,?);")){
                    ps1.setString(1, username);
                    ps1.setInt(2, mealItem.getFoodId());
                    ps1.executeUpdate();
                    responseObject.put("response", "added " + mealItem.getName() + " to "+ username+"'s favorites successfully");
                    responseObject.put("success", true);
                }
                con.close();
            }else{
                responseObject.put("response", mealItem.getName() + " is already one of "+ username+"'s favorite food items");
                responseObject.put("success", false);
            }
        }
        con1.close();

    }

    private void storeRecipe() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");//Open a connection
        ResultSet rs;
        Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
        try (PreparedStatement ps = con1.prepareStatement("SELECT * FROM favtrecipe WHERE username = ? AND recipeid = ?;")){
            ps.setString(1, username);
            ps.setInt(2, recipe.getFoodId());
            rs = ps.executeQuery();
            if(!rs.next()){
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
                try (PreparedStatement ps1 = con.prepareStatement("INSERT INTO favtrecipe(username, recipeid) VALUES( ?,?);")){
                    ps1.setString(1, username);
                    ps1.setInt(2, recipe.getFoodId());
                    ps1.executeUpdate();
                    responseObject.put("response", "added " + recipe.getName() + " to "+ username+"'s favorites successfully");
                    responseObject.put("success", true);
                }
                con.close();
            }else{
                responseObject.put("response", recipe.getName() + " is already one of "+ username+"'s favorite recipes");
                responseObject.put("success", false);
            }
            con1.close();
        }


    }
}
