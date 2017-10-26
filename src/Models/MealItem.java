package Models;


import org.json.JSONObject;

import static Models.MealItem.Meal.BREAKFAST;
import static Models.MealItem.Meal.DINNER;
import static Models.MealItem.Meal.LUNCH;

public class MealItem {
	//Enumerable type to easily identify the meal this FoodItem is a part of
	public enum Meal{BREAKFAST, LUNCH, DINNER}
	//Fields for qualifying the FoodItem in context of the meal it's in
	private FoodItem foodItem;
	private boolean isLocked;
	private int numServings;
	private Meal meal;

	//Three available constructors - with the FoodItem and Meal specified; with both plus the Locked state; and with all fields specified
	public MealItem(FoodItem food, MealItem.Meal meal){
		this.foodItem = food;
		this.isLocked = false;
		this.numServings = 0;
		this.meal = meal;
	}
	public MealItem(FoodItem food, boolean locked, MealItem.Meal meal){
		this(food, meal);
		this.isLocked = locked;
	}
	public MealItem(FoodItem food, boolean locked, int servings, MealItem.Meal meal){
		this(food, locked, meal);
		this.numServings = servings;
	}
    public MealItem(JSONObject fromObject){
	    String foodItemString = fromObject.getString("foodItem");
	    this.foodItem = new FoodItem(new JSONObject(foodItemString));
	    this.isLocked = fromObject.getBoolean("isLocked");
	    this.numServings = fromObject.getInt("numServings");
	    String mealString = fromObject.getString("meal");
	    switch(mealString.toUpperCase()){
            case "BREAKFAST":
                this.meal = BREAKFAST;
                break;
            case "LUNCH":
                this.meal = LUNCH;
                break;
            case "DINNER":
                this.meal = DINNER;
                break;
        }
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
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
	public void setNumServings(int numServings) {
		this.numServings = numServings;
	}
	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	//TODO: Set up JSON serialization/deserialization functions
    public JSONObject toJSON(){
	    JSONObject returnObject = new JSONObject();
	    returnObject.put("foodItem", this.foodItem.toJSON().toString());
	    returnObject.put("enum", this.meal.name());
	    returnObject.put("isLocked", this.isLocked);
	    returnObject.put("numServings", this.numServings);
	    return returnObject;
    }
}
