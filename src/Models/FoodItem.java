package Models;


import org.json.JSONException;
import org.json.JSONObject;

public class FoodItem implements MealItemContent {
	//Values used for identifying the food item
	private String name;
	private int foodId;
	//Values used for representing serving size
	private double servingValue;
	private String servingUnit;
	//Main values used by the meal planner for balancing
	private double calPerServing;
	private double gramsCarbPerServing;
	private double gramsProtPerServing;
	private double gramsFatPerServing;
	//Part of the balancing algorithm - not for use outside the MealPlanner
	private double internalCoefficient;


	//Constructors - any fields that are not explicitly set will be set to default values
	public FoodItem(String foodName, int id, int value, String unit, int cals, double carbs, double prot, double fat){
		this.name = foodName;
		this.foodId = id;
		this.servingValue = value;
		this.servingUnit = unit;
		this.calPerServing = cals;
		this.gramsCarbPerServing = carbs;
		this.gramsProtPerServing = prot;
		this.gramsFatPerServing = fat;
	}
	public FoodItem(){
		this.name = "default";
		this.foodId = -1;
		this.servingValue = -1;
		this.servingUnit = "default";
		this.calPerServing = -1;
		this.gramsCarbPerServing = -1;
		this.gramsProtPerServing = -1;
		this.gramsFatPerServing = -1;
	}
	public FoodItem(String foodName){
		this();
		this.name = foodName;
	}
	public FoodItem(int id){
		this();
		this.foodId = id;
	}
	public FoodItem(String foodName, int id){
		this();
		this.name = foodName;
		this.foodId = id;
	}
	public FoodItem(String name, int calories, double protein, double fat, double carbs) {
		this.name = name;
		this.calPerServing = calories;
		this.gramsProtPerServing = protein;
		this.gramsFatPerServing = fat;
		this.gramsCarbPerServing = carbs;
	}

	public FoodItem(JSONObject fromObject) throws JSONException{
	    this.name = fromObject.optString("name");
        this.foodId = fromObject.optInt("foodId");
        this.servingValue = fromObject.optInt("servingValue");
        this.servingUnit = fromObject.optString("servingUnit");
        this.calPerServing = fromObject.optInt("calPerServing");
        this.gramsCarbPerServing = fromObject.optInt("gramsCarbPerServing");
        this.gramsProtPerServing = fromObject.optInt("gramsProtPerServing");
        this.gramsFatPerServing = fromObject.optInt("gramsFatPerServing");
        this.internalCoefficient = fromObject.optDouble("internalCoefficient");
    }

	//Getters
	public String getName() {
		return name;
	}

	public int getFoodId() {
		return foodId;
	}

	public double getServingValue() {
		return servingValue;
	}

	public String getServingUnit() {
		return servingUnit;
	}

	public double getCalPerServing() {
		return calPerServing;
	}

	public double getGramsCarbPerServing() {
		return gramsCarbPerServing;
	}

	public double getGramsProtPerServing() {
		return gramsProtPerServing;
	}

	public double getGramsFatPerServing() {
		return gramsFatPerServing;
	}

	public double getInternalCoefficient() {
		return internalCoefficient;
	}

	//Setters
	public void setInternalCoefficient(Double d)
	{
		internalCoefficient = d;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public void setServingUnit(String servingUnit) {
		this.servingUnit = servingUnit;
	}

	public void setCalPerServing(double calPerServing) {
		this.calPerServing = calPerServing;
	}

	public void setGramsCarbPerServing(double gramsCarbPerServing) {
		this.gramsCarbPerServing = gramsCarbPerServing;
	}

	public void setGramsProtPerServing(double gramsProtPerServing) {
		this.gramsProtPerServing = gramsProtPerServing;
	}

	public void setGramsFatPerServing(double gramsFatPerServing) {
		this.gramsFatPerServing = gramsFatPerServing;
	}
	
	public void setServingValue(double val) {
		this.servingValue = val;
	}
	
	public void setInternalCoefficient(double coefficient) {
		this.internalCoefficient = coefficient;
	}

	//Derived Getters - returns a useful modifier over or combination of fields
	public long getCalsCarbPerServing(){return (long)(this.getGramsCarbPerServing() * 4);}
	public long getCalsProtPerServing(){return (long)(this.getGramsProtPerServing() * 4);}
	public long getCalsFatPerServing(){return (long)(this.getGramsFatPerServing() * 9);}
	public String getServingSize(){return this.getServingValue() + " " + this.getServingUnit();}


    public JSONObject toJson() throws JSONException{
	    JSONObject returnObject = new JSONObject();
	    returnObject.put("name", this.name);
        returnObject.put("foodId", this.foodId);
        returnObject.put("servingValue", this.servingValue);
        returnObject.put("servingUnit", this.servingUnit);
        returnObject.put("calPerServing", this.calPerServing);
        returnObject.put("gramsCarbPerServing", this.gramsCarbPerServing);
        returnObject.put("gramsFatPerServing", this.gramsFatPerServing);
        returnObject.put("gramsProtPerServing", this.gramsProtPerServing);
        returnObject.put("internalCoefficient", this.internalCoefficient);
        return returnObject;
    }
    
    //Equals override checks whether all relevant fields of this FoodItem object are the same as the FoodItem object passed in
    @Override
    public boolean equals(Object o){
    	if(!(o instanceof FoodItem)) return false;
    	FoodItem other = (FoodItem)o;
    	return this.getName().equals(other.getName()) && this.getFoodId() == other.getFoodId() && this.getCalPerServing() == other.getCalPerServing()
    			&& this.getGramsCarbPerServing() == other.getGramsCarbPerServing() && this.getGramsProtPerServing() == other.getGramsProtPerServing()
    			&& this.getGramsFatPerServing() == other.getGramsFatPerServing() && this.getServingSize().equals(other.getServingSize());
    }
}
