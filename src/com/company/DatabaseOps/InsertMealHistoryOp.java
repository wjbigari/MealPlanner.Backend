package com.company.DatabaseOps;

import Models.MealPlannerRec;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class InsertMealHistoryOp extends DatabaseOp {
    private String username;
    private MealPlannerRec mealPlan;
    public InsertMealHistoryOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
        this.mealPlan = new MealPlannerRec(new JSONObject(jrequest.getString("mealPlan")));
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
        String query = "INSERT INTO mealhistory(meal)";
        stmt.executeUpdate(query);
    }
}
