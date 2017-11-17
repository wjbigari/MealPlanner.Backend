package com.company.DatabaseOps;

import org.json.JSONObject;

import java.sql.SQLException;

public class DeleteRecipeOp extends DatabaseOp{
    private int recipeId;

    public DeleteRecipeOp(JSONObject jObject){
        super(jObject);
        this.recipeId = jObject.getInt("recipeId");
    }

    @Override
    public JSONObject performOp() throws SQLException {
        performDatabaseOp();
        return null;
    }
    private void performDatabaseOp(){

    }
}
