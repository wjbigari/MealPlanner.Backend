package com.company;

import Models.Constraints;
import Models.MealItem;
import Models.MealPlannerRec;
import Models.MealPlannerRequest;

import org.json.JSONArray;
import org.json.JSONException;
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
    private String requestString = "";
    private MealPlannerRequest mprequest;
    private MealPlannerRec mprec;
    private String responseString;

    public MealSocket(Socket socket){
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
        System.out.println("For a Meal Plan Request");
        start();
    }
    public void run() {
        BufferedReader in = null;
        OutputStream out = null;
        try {
            System.out.println("got here");
            out = socket.getOutputStream();


            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));

            requestString = in.readLine();

            System.out.println("got here3");
            System.out.println("JSON OBJECT RECEIVED");



            //parse object, grab constraints and mealItems JSON Objects
            System.out.println(requestString);
            mprequest = new MealPlannerRequest(new JSONObject(requestString));
            //call MealPlanner
            mprec = MealPlanner.createMealPlan(mprequest);
            System.out.println("PLAN GENERATED");
            System.out.println(mprec);
            responseString = mprec.toJson().toString();
            out.write(responseString.getBytes());
        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (JSONException ex) {
        	ex.printStackTrace();
        } catch (InterruptedException ex) {
        	ex.printStackTrace();
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
