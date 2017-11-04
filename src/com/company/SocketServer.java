package com.company;
import com.company.DatabaseOps.DatabaseOp;
import com.company.DatabaseOps.InsertUserOp;
import com.company.DatabaseOps.SearchOp;
import com.company.DatabaseOps.UpdateUserOp;
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
            DatabaseOp databaseOp = parseOperation(JSONRequest);

            if(databaseOp != null){
                response = databaseOp.performOp();
            }else{
                response.put("response", "unknown request type.");
                System.out.println("Unknown request type.");
            }

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (JSONException e) {
        	e.printStackTrace();
        	response = new JSONObject();
        	response.put("response", "failure performing Database Operation");
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            try {
                String responseAsString = response.toString();
                System.out.print("response object sent");
                out.write(responseAsString.getBytes());
                in.close();
                out.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private DatabaseOp parseOperation(JSONObject jrequest){
        DatabaseOp databaseOp = null;
        switch(jrequest.getString("option")){
            case "search":
                databaseOp = new SearchOp(jrequest);
                break;
            case "insertUser":
                databaseOp = new InsertUserOp(jrequest);
                break;
            case "updateUser":
                databaseOp = new UpdateUserOp(jrequest);
                break;
        }
        return databaseOp;
    }
}