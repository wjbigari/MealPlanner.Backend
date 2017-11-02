package com.company;
import com.company.DatabaseOps.DatabaseOp;
import com.company.DatabaseOps.SearchOp;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.*;

public class SocketServer extends Thread {


    protected Socket socket;
    public JSONObject response;
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
            JSONObject JSONRequest = new JSONObject(request);
            DatabaseOp databaseOp = null;
            switch(JSONRequest.getString("option")){
                case "search":
                    databaseOp = new SearchOp(JSONRequest);
                    break;

            }
            if(databaseOp != null){
                response = databaseOp.performOp();
                String responseAsString = response.toString();
                System.out.print("response object sent");
                out.write(responseAsString.getBytes());
            }else{
                System.out.println("Unknown request type.");
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (JSONException e) {
        	e.printStackTrace();
        } catch (SQLException e) {
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
}