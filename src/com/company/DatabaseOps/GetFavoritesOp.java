package com.company.DatabaseOps;

import Models.FoodItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

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
        return responseObject;
    }

    private void getFromDatabase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con, con1;
        //Open a connection
        int id;
        String content, sname;
        double cals,carbs, prots, fats, samount;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");
        con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");
        JSONArray foodList = new JSONArray();
        ResultSet rs, rs1;
        try(PreparedStatement ps = con.prepareStatement("SELECT * FROM favtfood WHERE username = ?;")){
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                id = rs.getInt("foodid");

                try(PreparedStatement ps1 = con1.prepareStatement("SELECT * FROM ingredients WHERE foodid = ?")){
                    ps1.setInt(1, id);
                    rs1 = ps1.executeQuery();
                    rs1.next();
                    content = rs1.getString("contents");
                    //info = rs.getString("nutritionalinfo");
                    cals = rs1.getDouble("cals");
                    carbs = rs1.getDouble("carbs");
                    prots = rs1.getDouble("prots");
                    fats = rs1.getDouble("fats");
                    samount = Double.parseDouble(rs1.getString("minservamt"));
                    sname = rs1.getString("servingname");
                    FoodItem i1 = new FoodItem(content, id, (int)samount, sname, (int)cals,carbs,prots, fats);
                    i1.setFavorite();
                    foodList.put(i1.toJson().toString());
                }
            }
        }
        responseObject.put("foodlist", foodList.toString());
    }
}
