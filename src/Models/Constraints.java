package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Constraints {
	//Fields for constraints set by the user; all nutrient fields are to be stored as Calories, not as Grams
	private int minCals;
	private int maxCals;
	private int minCarbs;
	private int maxCarbs;
	private int minProt;
	private int maxProt;
	private int minFat;
	private int maxFat;
	
	//This constructor sets all fields of the constraints according to the values passed in
	public Constraints(int calMin, int calMax, int carbMin, int carbMax, int protMin, int protMax, int fatMin, int fatMax){
		minCals = calMin;
		maxCals = calMax;
		minCarbs = carbMin;
		maxCarbs = carbMax;
		minProt = protMin;
		maxProt = protMax;
		minFat = fatMin;
		maxFat = fatMax;
	}
	//No-arg constructor sets all fields to 0; when using this constructor any fields used must be set via Setter methods
	public Constraints(){
		this(0, 0, 0, 0, 0, 0, 0, 0);
	}
	//Constructor for creating a new constraints list out of a JSON Object
	public Constraints(JSONObject constraintsJson){
		this();
		this.fromJson(constraintsJson);
	}

	//Getters
	public int getMinCals() {
		return minCals;
	}

	public int getMaxCals() {
		return maxCals;
	}

	public int getMinCarbs() {
		return minCarbs;
	}

	public int getMaxCarbs() {
		return maxCarbs;
	}

	public int getMinProt() {
		return minProt;
	}

	public int getMaxProt() {
		return maxProt;
	}

	public int getMinFat() {
		return minFat;
	}

	public int getMaxFat() {
		return maxFat;
	}

	//Setters
	public void setMinCals(int minCals) {
		this.minCals = minCals;
	}

	public void setMaxCals(int maxCals) {
		this.maxCals = maxCals;
	}

	public void setMinCarbs(int minCarbs) {
		this.minCarbs = minCarbs;
	}

	public void setMaxCarbs(int maxCarbs) {
		this.maxCarbs = maxCarbs;
	}

	public void setMinProt(int minProt) {
		this.minProt = minProt;
	}

	public void setMaxProt(int maxProt) {
		this.maxProt = maxProt;
	}

	public void setMinFat(int minFat) {
		this.minFat = minFat;
	}

	public void setMaxFat(int maxFat) {
		this.maxFat = maxFat;
	}
	
	//JSON serialization and de-serialization functions
	public JSONObject toJson() throws JSONException{
		JSONObject out = new JSONObject();
		out.put("minCals", this.getMinCals());
		out.put("maxCals", this.getMaxCals());
		out.put("minCarbs", this.getMinCarbs());
		out.put("maxCarbs", this.getMaxCarbs());
		out.put("minProt", this.getMinProt());
		out.put("maxProt", this.getMaxProt());
		out.put("minFat", this.getMinFat());
		out.put("maxFat", this.getMaxFat());
		return out;
	}
	
	public void fromJson(JSONObject in){
		this.setMinCals(in.optInt("minCals"));
		this.setMaxCals(in.optInt("maxCals"));
		this.setMinCarbs(in.optInt("minCarbs"));
		this.setMaxCarbs(in.optInt("maxCarbs"));
		this.setMinProt(in.optInt("minProt"));
		this.setMaxProt(in.optInt("maxProt"));
		this.setMinFat(in.optInt("minFat"));
		this.setMaxFat(in.optInt("maxFat"));
	}
}
