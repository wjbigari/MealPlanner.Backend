package Models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealPlannerRequest {
	private ArrayList<MealItem> mealList;
	private Constraints constraints;
	
	public MealPlannerRequest(ArrayList<MealItem> items, Constraints con){
		mealList = items;
		constraints = con;
	}
	public MealPlannerRequest(JSONObject mpRequestJson){
		mealList = new ArrayList<>();
		constraints = new Constraints();
		
	}
	
	//Getters
	public ArrayList<MealItem> getMealItems() {
		return mealList;
	}
	public Constraints getConstraints() {
		return constraints;
	}
	//Setters
	public void setMealItems(ArrayList<MealItem> mealItems) {
		this.mealList = mealItems;
	}
	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}
	
	//JSON serialization and de-serialization functions
	public JSONObject toJson() throws JSONException{
		JSONObject out = new JSONObject();
		for(MealItem item : this.getMealItems()){
			out.accumulate("mealList", item.toJson());
		}
		out.put("constraints", this.getConstraints().toJson());
		return out;
	}
	
	public void fromJson(JSONObject in) throws JSONException{
		mealList.clear();
		JSONArray items = in.optJSONArray("mealList");
		if(items != null){
			for(int i = 0; i < items.length(); i++){
				mealList.add(new MealItem(items.optJSONObject(i)));
			}
		}
		JSONObject item = in.optJSONObject("constraints");
		if(item != null){
			this.setConstraints(new Constraints(item));
		}
	}
}
