package com.company.DatabaseOps;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteFavoriteOp extends DatabaseOp {
    private String username;
    private int foodid;
    private JSONObject responseObject;
    public DeleteFavoriteOp(JSONObject jrequest) {
        super(jrequest);
        this.foodid = jrequest.getInt("foodid");
        this.username = jrequest.getString("username");
    }

    @Override
    public JSONObject performOp() throws SQLException {
        this.responseObject = new JSONObject();
        try {
            deleteFromDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return responseObject;
    }

    private void deleteFromDatabase() throws SQLException, ClassNotFoundException {
        Connection con = null;
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");
        try(PreparedStatement ps = con.prepareStatement("DELETE FROM favtfood WHERE username = ? AND foodid = ?;")){
            ps.setString(1, username);
            ps.setInt(2, foodid);
            int sint = ps.executeUpdate();
            if(sint > 0){
                responseObject.put("success", true);
            }else{
                responseObject.put("success", false);
            }
        }
    }
}
