package com.company.DatabaseOps;

import Models.UserRecipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class GetRecipesOp extends DatabaseOp {
    private String username;
    private JSONObject responseObject;

    public GetRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");

    }
    @Override
    public JSONObject performOp() throws SQLException {
        grabFromDatabase();

        return responseObject;
    }


    public void grabFromDatabase(){
        JSONArray arrayList = new JSONArray();
        //TODO Yash, use username to grab all UserRecipes and store them a JSONArray list



        // TODO the below logic should go inside for loop for each database entry

        UserRecipe trecipe = null;
        //make new with constructor
        arrayList.put(trecipe.toJson().toString());


        responseObject.put("recipeList", arrayList.toString());
    }

}
