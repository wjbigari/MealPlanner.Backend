package com.company.DatabaseOps;

import Models.MealPlannerRec;
import org.json.JSONObject;

import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class InsertMealHistoryOp extends DatabaseOp {
    private String username;
    private MealPlannerRec mealPlan;
    private Calendar date;
    public InsertMealHistoryOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
        this.mealPlan = new MealPlannerRec(new JSONObject(jrequest.getString("mealPlan")));
        if(jrequest.has("month")){
            date.set(jrequest.getInt("year"),jrequest.getInt("month"),jrequest.getInt("year"));
        }else{
            date= Calendar.getInstance();
        }
    }

    @Override
    public JSONObject performOp() throws SQLException {

        try {
            storeMealPlan();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private void storeMealPlan() throws SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        Class.forName("com.mysql.jdbc.Driver");

        //Open a connection
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
        stmt = con.createStatement();
        String query = "INSERT INTO mealhistory(username, mealname, date) VALUES('"+ this.username + "', '" + + "')";
        stmt.executeUpdate(query);
    }
}
