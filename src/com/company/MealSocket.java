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
            mprequest = new MealPlannerRequest(new JSONObject(requestString));
            //call MealPlanner
            mprec = MealPlanner.createMealPlan(mprequest);
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
