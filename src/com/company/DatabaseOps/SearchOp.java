package com.company.DatabaseOps;

import Models.FoodItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class SearchOp extends DatabaseOp{
    public SearchOp(JSONObject jObject){
        super(jObject);
    }

    @Override
    public JSONObject performOp() throws SQLException {
        String request = this.jobject.getString("search");
        JSONObject returnObject = new JSONObject();
        JSONArray searchArray = new JSONArray();
        System.out.println("searching for request: " + request + ".");
        String content=null, info=null,sname=null;
        double cals,carbs,prots,fats, samount;
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

            int id;
            String query = "select foodid,contents, cals,carbs,prots, fats, minservamt, servingname from ingredients where contents LIKE '%" + request + "%' LIMIT 5;";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                id = rs.getInt("foodid");
                content = rs.getString("contents");
                //info = rs.getString("nutritionalinfo");
                cals = rs.getDouble("cals");
                carbs = rs.getDouble("carbs");
                prots = rs.getDouble("prots");
                fats = rs.getDouble("fats");
                samount = Double.parseDouble(rs.getString("minservamt"));
                sname = rs.getString("servingname");


                FoodItem i1 = new FoodItem(content, id, (int)samount, sname, (int)cals,carbs,prots, fats);
                System.out.println("adding food item " + content + "to list.");
                JSONObject obj1 = i1.toJson();
                System.out.println(i1.toString());
                searchArray.put(obj1.toString());


            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("problem here");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();

        }finally{
            if(stmt != null)
            {
                stmt.close();
            }
        }

        returnObject.put("search", searchArray.toString());

        return returnObject;
    }

}
