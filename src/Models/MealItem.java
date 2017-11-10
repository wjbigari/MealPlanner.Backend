package Models;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static Models.MealItem.Meal.BREAKFAST;
import static Models.MealItem.Meal.DINNER;
import static Models.MealItem.Meal.LUNCH;

public class MealItem implements Serializable {
	//Enumerable type to easily identify the meal this FoodItem is a part of
	public enum Meal{BREAKFAST, LUNCH, DINNER}
	//Fields for qualifying the FoodItem in context of the meal it's in
	private MealItemContent content; //either a FoodItem or a UserRecipe
	private boolean isLocked;
	private int numServings;
	private Meal meal;

	@Override
	public String toString(){
		return content.toString();
	}

	//Three available constructors - with the FoodItem and Meal specified; with both plus the Locked state; and with all fields specified
	public MealItem(MealItemContent item, Meal meal){
		this.content = item;
		this.isLocked = false;
		this.numServings = 0;
		this.meal = meal;
	}
	public MealItem(MealItemContent item, boolean locked, MealItem.Meal meal){
		this(item, meal);
		this.isLocked = locked;
	}
	public MealItem(MealItemContent item, boolean locked, int servings, MealItem.Meal meal){
		this(item, locked, meal);
		this.numServings = servings;
	}
	public MealItem(JSONObject fromObject) throws JSONException {
		JSONObject mealContent = new JSONObject(fromObject.optString("content"));
//        if(mealContent.getString("ingredients").equals("")){
		this.content = new FoodItem(mealContent);
//        }else{
//            this.content = new UserRecipe(mealContent);
//        }
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
	public MealItemContent getFoodItem() {
		return content;
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
	public JSONObject toJson() throws JSONException{
		JSONObject returnObject = new JSONObject();
		returnObject.put("content", this.content.toJson().toString());
		returnObject.put("meal", this.meal.name());
		returnObject.put("isLocked", this.isLocked);
		returnObject.put("numServings", this.numServings);
		return returnObject;
	}

	//Equals override checks whether a MealItem contains the same fields as another food item
	@Override
	public boolean equals(Object o){
		if(!(o instanceof MealItem)) return false;
		MealItem other = (MealItem)o;
		return this.equalFoodItemAndMeal(other) && this.isLocked() == other.isLocked() && this.numServings == other.numServings;
	}
	//A similar check, but only checks whether the FoodItem and Meal are the same
	public boolean equalFoodItemAndMeal(MealItem other){
		return this.getFoodItem().equals(other.getFoodItem()) && this.getMeal().compareTo(other.getMeal()) == 0;
	}
}
