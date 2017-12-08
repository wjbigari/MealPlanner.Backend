package com.company.DatabaseOps;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetFavoritesOp extends DatabaseOp {
    private String username;
    private JSONObject responseObject;

    public GetFavoritesOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
    }


    @Override
    public JSONObject performOp() throws SQLException {
        responseObject = new JSONObject();
        try {
            getFromDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private void getFromDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con;
        //Open a connection
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

    }
}
