package Models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MealPlannerRec {
	private ArrayList<MealItem> breakfastItems;
	private ArrayList<MealItem> lunchItems;
	private ArrayList<MealItem> dinnerItems;
	
	private int totalCals;
	private int totalCarbs;
	private int totalProt;
	private int totalFat;
	
	//Empty constructor creates a blank recommendation with no statistics
	public MealPlannerRec(){
		breakfastItems = new ArrayList<>();
		lunchItems = new ArrayList<>();
		dinnerItems = new ArrayList<>();
		totalCals = 0;
		totalCarbs = 0;
		totalProt = 0;
		totalFat = 0;
	}
	//Constructor with a provided list of items will build a recommendation out of that list
	public MealPlannerRec(ArrayList<MealItem> allItems){
		this();
		for(MealItem item : allItems){
			this.addItemToRec(item);
		}
	}
	//Constructor with provided JSON will convert that JSON into a MealPlannerRec instance
	public MealPlannerRec(JSONObject recJson) throws JSONException{
		this();
		this.fromJson(recJson);
	}
	
	//Helper method for adding a MealItem to this MealPlanner recommendation
	public void addItemToRec(MealItem item){
		if(item.getNumServings() > 0){
			switch(item.getMeal()){
			case BREAKFAST:
				breakfastItems.add(item);
				break;
			case LUNCH:
				lunchItems.add(item);
				break;
			case DINNER:
				dinnerItems.add(item);
				break;
			}
			totalCals += (item.getNumServings() * item.getFoodItem().getCalPerServing());
			totalCarbs += (item.getNumServings() * item.getFoodItem().getGramsCarbPerServing());
			totalProt += (item.getNumServings() * item.getFoodItem().getGramsProtPerServing());
			totalFat += (item.getNumServings() * item.getFoodItem().getGramsFatPerServing());
		}
	}

	
	//JSON serialization and de-serialization functions
	public JSONObject toJson() throws JSONException{
		JSONObject out = new JSONObject();
		for(MealItem item : this.breakfastItems){
			out.accumulate("breakfastItems", item.toJson());
		}
		for(MealItem item : this.lunchItems){
			out.accumulate("lunchItems", item.toJson());
		}
		for(MealItem item : this.dinnerItems){
			out.accumulate("dinnerItems", item.toJson());
		}
		out.put("totalCals", this.totalCals);
		out.put("totalCarbs", this.totalCarbs);
		out.put("totalProt", this.totalProt);
		out.put("totalFat", this.totalFat);
		return out;
	}
	
	public void fromJson(JSONObject in) throws JSONException{
		this.breakfastItems.clear();
		this.lunchItems.clear();
		this.dinnerItems.clear();
		JSONArray items = in.optJSONArray("breakfastItems");
		for(int i = 0; i < items.length(); i++){
			breakfastItems.add(new MealItem(items.optJSONObject(i)));
		}
		items = in.optJSONArray("lunchItems");
		for(int i = 0; i < items.length(); i++){
			lunchItems.add(new MealItem(items.optJSONObject(i)));
		}
		 items = in.optJSONArray("dinnerItems");
		for(int i = 0; i < items.length(); i++){
			dinnerItems.add(new MealItem(items.optJSONObject(i)));
		}
		this.totalCals = in.optInt("totalCals");
		this.totalCarbs = in.optInt("totalCarbs");
		this.totalProt = in.optInt("totalProt");
		this.totalFat = in.optInt("totalFat");
	}
	
	//toString function to help with visualization of the structure of a meal recommendation
	@Override
	public String toString(){
		String result = "";
		result += "Breakfast Items\n";
		result += "***************\n";
		for(MealItem item : breakfastItems){
			result += item.getFoodItem().getName() + " - " + (item.getNumServings() * item.getFoodItem().getServingValue()) + " " + item.getFoodItem().getServingUnit() + "\n";
		}
		result += "\n";
		result += "Lunch Items\n";
		result += "***********\n";
		for(MealItem item : lunchItems){
			result += item.getFoodItem().getName() + " - " + (item.getNumServings() * item.getFoodItem().getServingValue()) + " " + item.getFoodItem().getServingUnit() + "\n";
		}
		result += "\n";
		result += "Dinner Items\n";
		result += "************\n";
		for(MealItem item : dinnerItems){
			result += item.getFoodItem().getName() + " - " + (item.getNumServings() * item.getFoodItem().getServingValue()) + " " + item.getFoodItem().getServingUnit() + "\n";
		}
		result += "\n";
		result += "Daily Summary:\n";
		result += "**************\n";
		result += "Total Calories - " + this.totalCals + "\n";
		result += "Total Carbs - " + this.totalCarbs + "\n";
		result += "Total Protein - " + this.totalProt + "\n";
		result += "Total Fat - " + this.totalFat + "\n";
		return result;
	}
}
