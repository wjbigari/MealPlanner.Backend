package com.company;

import Models.Constraints;
import Models.MealItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import MealPlanner.MealPlanner;

public class MealSocket extends Thread {
    private Socket socket;
    private JSONArray requestList, responseList;
    private String requestString = "";
    private ArrayList<MealItem> mealList;
    private  ArrayList<MealItem> returnMealList;
    private JSONObject requestJSON, constraintsJSON;
    protected MealPlanner mp;
    private Constraints mealConstraints;
    private String responseAsString;

    public MealSocket(Socket socket){
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        System.out.println("For a Meal Plan Request");
        start();
    }
    public void run() {

        InputStream in = null;
        OutputStream out = null;
        try {
            System.out.println("got here");
            in = socket.getInputStream();
            out = socket.getOutputStream();
            System.out.println("got here1");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String requestLine;
            System.out.println("got here2");
            while(br.ready()){
                requestLine = br.readLine();
                System.out.println("got here2.25");
                requestString+= requestLine;
                System.out.println(requestLine);
            };
            System.out.println("got here3");
            System.out.println("JSON OBJECT RECEIVED");
            //parse object, grab constraints and mealItems JSON Objects
            System.out.println(requestString);
            requestJSON = new JSONObject(requestString);
            constraintsJSON = new JSONObject(requestJSON.getString("constraints"));
            System.out.println("CONSTRAINTS JSON PARSED");
            requestList = new JSONArray(requestJSON.getString("mealList"));
            System.out.println("MEALLIST JSON PARSED");
            mealList = new ArrayList<MealItem>();
            for(int i = 0; i< requestList.length(); i++){
                JSONObject mealItemJSON = new JSONObject(requestList.getString(i));
                mealList.add(new MealItem(mealItemJSON));
            }
            System.out.println("MEALS LIST MADE");
            mealConstraints = new Constraints(constraintsJSON);
            //call MealPlanner

            mp = new MealPlanner();
            responseList = new JSONArray();
            returnMealList = mp.createMealPlan(mealList, mealConstraints);
            System.out.println("PLANNER STARTED");
            for(int i =0; i < returnMealList.size(); i++){
                responseList.put(returnMealList.get(i).toJSON());
            }
            responseAsString = responseList.toString();
            out.write(responseAsString.getBytes());
        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }catch(NullPointerException ex){
                ex.printStackTrace();
            }
        }
    }

}
