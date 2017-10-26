package models;

import org.json.JSONException;
import org.json.JSONObject;

public class MealItem {
	//Enumerable type to easily identify the meal this FoodItem is a part of
	public enum Meal{BREAKFAST, LUNCH, DINNER}
	//Fields for qualifying the FoodItem in context of the meal it's in
	private FoodItem foodItem;
	private boolean isLocked;
	private int numServings;
	private Meal meal;

	//Four available constructors - with the FoodItem and Meal specified; with both plus the Locked state;
	//  with all fields specified; and with a JsonObject provided
	public MealItem(FoodItem food, boolean locked, int servings, MealItem.Meal meal){
		this.foodItem = food;
		this.isLocked = locked;
		this.numServings = servings;
		this.meal = meal;
	}
	public MealItem(FoodItem food, boolean locked, MealItem.Meal meal){
		this(food, locked, 0, meal);
	}
	public MealItem(FoodItem food, MealItem.Meal meal){
		this(food, false, 0, meal);
	}
	public MealItem(JSONObject mealItemJson){
		this(new FoodItem(), Meal.DINNER);
		this.fromJson(mealItemJson);
	}
	

	public MealItem() {
		// TODO Auto-generated constructor stub
	}
	//Getters
	public FoodItem getFoodItem() {
		return foodItem;
	}
	public boolean isLocked() {
		return isLocked;
	}
	public int getNumServings() {
		return numServings;
	}
	public Meal getMeal() {
		return meal;
	}

	//Setters
	public void setFoodItem(FoodItem item) {
		this.foodItem = item;
	}
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public void setNumServings(int numServings) {
		this.numServings = numServings;
	}
	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	//JSON serialization and de-serialization functions
	public JSONObject toJson() throws JSONException{
		JSONObject out = new JSONObject();
		out.put("foodItem", this.getFoodItem().toJson());
		out.put("isLocked", this.isLocked);
		out.put("numServings", this.getNumServings());
		out.put("meal", this.getMeal().toString());
		return out;
	}
	
	public void fromJson(JSONObject in){
		this.setFoodItem(new FoodItem(in.optJSONObject("foodItem")));
		this.setLocked(in.optBoolean("isLocked"));
		this.setNumServings(in.optInt("numServings"));
		switch(in.optString("meal")){
		case "BREAKFAST":
			this.setMeal(Meal.BREAKFAST);
			break;
		case "LUNCH":
			this.setMeal(Meal.LUNCH);
			break;
		case "DINNER": 
		default: //Badly formatted "meal" fields default to DINNER
			this.setMeal(Meal.DINNER);
			break;
		}
		
	}
}
