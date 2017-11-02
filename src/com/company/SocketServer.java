package com.company;

import Models.DBFoodItem;

import Models.FoodItem;
import Models.MealItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import com.mysql.jdbc.Driver;

public class SocketServer extends Thread {


    protected Socket socket;
    public JSONObject responseList;
    protected SocketServer(Socket socket) {
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        System.out.println("For a Meal Item Search Request");
        start();
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String request;
            request = br.readLine();
            System.out.println("Message received:" + request);
            responseList = getSearchQueryFromDatabase(request);
            String responseAsString = responseList.toString();
            out.write(responseAsString.getBytes());

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
        	e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private JSONObject getSearchQueryFromDatabase(String request) throws SQLException, ClassNotFoundException, JSONException {
        JSONObject returnObject = new JSONObject();
        JSONArray searchArray = new JSONArray();
        System.out.println("searching for request: " + request);
        String content=null, info=null,sname=null,samount=null;
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

            int id;
            String query = "select foodid,contents, nutritionalinfo, minservamt, servingname from ingredients where contents LIKE '%" + request+ "%';";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                content = rs.getString("contents");
                info = rs.getString("nutritionalinfo");
                samount = rs.getString("minservamt");
                sname = rs.getString("servingname");
                id = rs.getInt("foodid");
                String[] cInfo = info.split("\\|");


                FoodItem i1 = new FoodItem(content,
                        Integer.parseInt(cInfo[0]),
                        Integer.parseInt(cInfo[1]),
                        Integer.parseInt(cInfo[2]),
                        Integer.parseInt(cInfo[3]));
                i1.setFoodId(id);
                i1.setServingValue(Integer.parseInt(samount));
                i1.setServingUnit(sname);
                JSONObject obj1 = i1.toJson();
                System.out.println(i1.toString());
                searchArray.put(obj1.toString());
            }
        }
        catch(SQLException e){
                e.printStackTrace();
        }
        finally{
                if(stmt != null)
                {
                    stmt.close();
                }
        }

        //FoodItem i2 = new FoodItem("chicken", "30|12|15|84");

       // searchArray.put(i2.toString());
        returnObject.put("search", searchArray.toString());

        return returnObject;
    }
}