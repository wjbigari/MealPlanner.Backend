package models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealPlannerRequest {
	private ArrayList<MealItem> mealItems;
	private Constraints constraints;
	
	public MealPlannerRequest(ArrayList<MealItem> items, Constraints con){
		mealItems = items;
		constraints = con;
	}
	public MealPlannerRequest(JSONObject mpRequestJson){
		mealItems = new ArrayList<>();
		constraints = new Constraints();
		
	}
	
	//Getters
	public ArrayList<MealItem> getMealItems() {
		return mealItems;
	}
	public Constraints getConstraints() {
		return constraints;
	}
	//Setters
	public void setMealItems(ArrayList<MealItem> mealItems) {
		this.mealItems = mealItems;
	}
	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}
	
	//JSON serialization and de-serialization functions
	public JSONObject toJson() throws JSONException{
		JSONObject out = new JSONObject();
		for(MealItem item : this.getMealItems()){
			out.accumulate("mealItems", item.toJson());
		}
		out.put("constraints", this.getConstraints().toJson());
		return out;
	}
	
	public void fromJson(JSONObject in){
		JSONArray items = in.optJSONArray("mealItems");
		mealItems.clear();
		for(int i = 0; i < items.length(); i++){
			mealItems.add(new MealItem(items.optJSONObject(i)));
		}
		this.setConstraints(new Constraints(in.optJSONObject("constraints")));
	}
}
