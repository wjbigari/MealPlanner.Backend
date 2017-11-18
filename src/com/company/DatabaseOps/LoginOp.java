package com.company.DatabaseOps;

import com.company.DatabaseOps.DatabaseOp;
import org.json.JSONObject;

import java.sql.*;

public class LoginOp extends DatabaseOp {
    private String username;
    private String password;
    private JSONObject responseObject;

    public LoginOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
        this.password = jrequest.getString("password");
    }

    @Override
    public JSONObject performOp() throws SQLException {
        try {
            login();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.responseObject;
    }

    private void login() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        Statement stmt = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

        stmt = con.createStatement();
        String query = "SELECT * FROM userprofile WHERE username = '"+ this.username +
                "' AND password = '" + this.password + "';";

        ResultSet rs = stmt.executeQuery(query);
        boolean login = false;
        if(rs.next()){
            if(rs.getString("password").equals(this.password) && rs.getString("username").equals(this.username)){
                login = true;
            }
        }
        if (login){
            responseObject.put("login", true);
        }else{
            responseObject.put("login", false);
        }

    }
}
