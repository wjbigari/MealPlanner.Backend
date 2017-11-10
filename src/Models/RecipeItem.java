package Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RecipeItem implements Serializable {
    private FoodItem foodItem;
    private int numServings;

    @Override
    public String toString(){
        return foodItem.toString();
    }

    //This class should not use a no-arg constructor
    public RecipeItem(FoodItem food, int servings){
        this.foodItem = food;
        this.numServings = servings;
    }
    public RecipeItem(JSONObject in){
        this.foodItem = new FoodItem(in.optString("foodItem"));
        this.numServings = in.optInt("numServings");
    }

    //Getters
    public FoodItem getFoodItem(){return this.foodItem;}
    public int getNumServings(){return this.numServings;}
    //Setters
    public void setFoodItem(FoodItem food){this.foodItem = food;}
    public void setNumServings(int servs){this.numServings = servs;}

    //Serialization function; de-serialization performed via constructor
    public JSONObject toJson() throws JSONException {
        JSONObject out = new JSONObject();
        out.put("foodItem", this.getFoodItem().toJson().toString());
        out.put("numServings", this.getNumServings());
        return out;
    }
}
