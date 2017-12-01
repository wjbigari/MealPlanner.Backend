package Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Constraints {
	//Fields for constraints set by the user; all nutrient fields are to be stored as Calories, not as Grams
	private double minCals;
	private double maxCals;
	private double minCarbs;
	private double maxCarbs;
	private double minProt;
	private double maxProt;
	private double minFat;
	private double maxFat;
	
	//This constructor sets all fields of the constraints according to the values passed in
	public Constraints(double calMin, double calMax, double carbMin, double carbMax, double protMin, double protMax, double fatMin, double fatMax){
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
		this(2000, 2100, 150, 170, 150, 170, 80, 85);
	}

	//Constructor for creating a new constraints list out of a JSON Object
	public Constraints(JSONObject constraintsJson){
		this();
		this.fromJson(constraintsJson);
	}

	//Getters
	public double getMinCals() {
		return minCals;
	}

	public double getMaxCals() {
		return maxCals;
	}

	public double getMinCarbs() {
		return minCarbs;
	}

	public double getMaxCarbs() {
		return maxCarbs;
	}

	public double getMinProt() {
		return minProt;
	}

	public double getMaxProt() {
		return maxProt;
	}

	public double getMinFat() {
		return minFat;
	}

	public double getMaxFat() {
		return maxFat;
	}

	//Setters
	public void setMinCals(double minCals) {
		this.minCals = minCals;
	}

	public void setMaxCals(double maxCals) {
		this.maxCals = maxCals;
	}

	public void setMinCarbs(double minCarbs) {
		this.minCarbs = minCarbs;
	}

	public void setMaxCarbs(double maxCarbs) {
		this.maxCarbs = maxCarbs;
	}

	public void setMinProt(double minProt) {
		this.minProt = minProt;
	}

	public void setMaxProt(double maxProt) {
		this.maxProt = maxProt;
	}

	public void setMinFat(double minFat) {
		this.minFat = minFat;
	}

	public void setMaxFat(double maxFat) {
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
