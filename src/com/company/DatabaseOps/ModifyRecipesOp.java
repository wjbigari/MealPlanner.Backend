package com.company.DatabaseOps;

import Models.UserRecipe;
import org.json.JSONObject;

import java.sql.SQLException;

public class ModifyRecipesOp extends DatabaseOp {
    private String username;
    private UserRecipe userRecipe;
    public ModifyRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");
        this.userRecipe = new UserRecipe(new JSONObject(jobject.getString("recipe")));
    }
    @Override
    public JSONObject performOp() throws SQLException {
        performDatabaseOp();
        return null;
    }

    private void performDatabaseOp() {
    }

}
