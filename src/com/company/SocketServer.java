package com.company;

import Models.FoodItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.*;

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

    private JSONObject getSearchQueryFromDatabase(String request) throws SQLException, ClassNotFoundException {
        JSONObject returnObject = new JSONObject();
        JSONArray searchArray = new JSONArray();
        System.out.println("searching for request: " + request);
        //TODO - Yash - make query call, for each response add an object to searchArray
        String content=null, info=null;
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");


            String query = "select contents, nutritionalinfo from ingredients where contents LIKE '%" + request+ "%';";
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                content = rs.getString("contents");
                info = rs.getString("nutritionalinfo");
                FoodItem i1 = new FoodItem(content, info);
                searchArray.put(i1.toString());
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