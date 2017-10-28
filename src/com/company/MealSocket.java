package com.company;
import Models.MealPlannerRec;
import Models.MealPlannerRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;

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
            System.out.println(mprec.toJson().toString(2));
            out.write(responseString.getBytes());
        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (JSONException ex) {
        	ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
