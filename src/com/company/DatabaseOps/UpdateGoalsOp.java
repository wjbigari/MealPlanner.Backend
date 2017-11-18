package com.company.DatabaseOps;

import Models.Constraints;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateGoalsOp extends DatabaseOp {
    private Constraints constraints;
    private String username;
    public UpdateGoalsOp(JSONObject jrequest) {
        super(jrequest);
        constraints = new Constraints(new JSONObject(jrequest.getString("constraints")));
        username = jrequest.getString("username");
    }

    @Override
    public JSONObject performOp() {
        updateDatabase();
        JSONObject responseObject = new JSONObject();
        responseObject.put("response", "Constraints successfully updated.");
        return responseObject;
    }
    public void updateDatabase() {

        Connection con = null;
        Statement stmt = null;
        //Open a connection
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

            String query;
            stmt = con.createStatement();
            query = "UPDATE constraints " +
                    "SET mincals = '" + this.constraints.getMinCals() + "' , maxcals = '" + this.constraints.getMaxCals() + "' , " +
                    "mincarbs = '" + this.constraints.getMinCarbs() + "' , maxcarbs = '" + this.constraints.getMaxCarbs() + "' , " +
                    "minprot = '" + this.constraints.getMinProt() + "' , maxprot = '" + this.constraints.getMaxProt() + "' , " +
                    "minfat = '" + this.constraints.getMinFat() + "' , maxfat = '" + this.constraints.getMaxFat() + "' " +
                    "WHERE username = '" + username + "' ;";

            stmt.executeUpdate(query);
            System.out.println("Constraints updated successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
