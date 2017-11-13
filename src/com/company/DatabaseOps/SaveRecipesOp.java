package com.company.DatabaseOps;

import Models.RecipeItem;
import Models.UserRecipe;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

public class SaveRecipesOp extends DatabaseOp{
    private String username;
    private UserRecipe userRecipe;
    private JSONObject responseObject;

    public SaveRecipesOp(JSONObject jObject){
        super(jObject);
        this.username = jObject.getString("username");
        this.userRecipe = new UserRecipe(jobject.getString("userRecipe"));

    }
    @Override
    public JSONObject performOp() throws SQLException {

        try{
            updateDatabase();
        }catch(SQLException e){
            e.printStackTrace();
        }
        responseObject.put("response", "Successfully added Recipe");
        return responseObject;
    }

    private void updateDatabase() {
        //TODO Yash store user recipe in database using the given username
        ArrayList<RecipeItem> recipeItemArrayList  = userRecipe.getIngredients();
        recipeItemArrayList.get(0).getFoodItem().getFoodId();
    }
}
