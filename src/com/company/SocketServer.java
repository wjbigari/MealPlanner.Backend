package com.company;
import com.company.DatabaseOps.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
//import com.mysql.jdbc.Driver;

public class SocketServer extends Thread {


    protected Socket socket;
    public JSONObject response;
    protected SocketServer(Socket socket) {
        this.socket = socket;
        System.out.println("New client connected from " + socket.getInetAddress().getHostAddress());
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
            response = new JSONObject();
            if(databaseOp != null){
                response = databaseOp.performOp();
            }else{
                response = new JSONObject();
                response.put("response", "unknown request type.");
                System.out.println("Unknown request type.");
            }
            String responseAsString = response.toString();
            System.out.println("response object sent to "  + socket.getInetAddress().getHostAddress());
            out.write(responseAsString.getBytes());
            in.close();
            out.close();
            socket.close();

        } catch (IOException ex) {
            System.out.println("Unable to get streams from client");
        } catch (JSONException e) {
        	e.printStackTrace();
        	response.put("response", "failure performing Database Operation");
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private DatabaseOp parseOperation(JSONObject jrequest){
        DatabaseOp databaseOp = null;
        String consoleString = "Invalid request from ";
        switch(jrequest.getString("option")){
            case "search":{
                consoleString = "performing food item search for ";
                databaseOp = new SearchOp(jrequest);
                break;}
            case "insertUser":{
                consoleString = "inserting User from ";
                databaseOp = new InsertUserOp(jrequest);
                break;}
            case "updateUser":{
                consoleString = "updating User from ";
                databaseOp = new UpdateUserOp(jrequest);
                break;}
            case "addRecipe":
                consoleString = "adding recipe from ";
                databaseOp = new SaveRecipesOp(jrequest);
                break;
            case "getRecipes":
                consoleString = "getting recipes for ";
                databaseOp = new GetRecipesOp(jrequest);
                break;
            case "deleteRecipe":
                consoleString = "deleting recipe item for ";
                databaseOp = new DeleteRecipeOp(jrequest);
                break;
            case "modifyRecipe":
                consoleString = "altering recipe for ";
                databaseOp = new ModifyRecipesOp(jrequest);
                break;
            case "updateGoals":
                consoleString = "updating goals for ";
                databaseOp = new UpdateGoalsOp(jrequest);
                break;
            case "login":
                consoleString = "logging in for ";
                databaseOp = new LoginOp(jrequest);
                break;
        }
        System.out.println(consoleString + socket.getInetAddress().getHostAddress());
        return databaseOp;
    }
}