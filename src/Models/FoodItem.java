package Models;


import org.json.JSONObject;

public class FoodItem {
	//Values used for identifying the food item
	private String name;
	private int foodId;
	//Values used for representing serving size
	private int servingValue;
	private String servingUnit;
	//Main values used by the meal planner for balancing
	private int calPerServing;
	private int gramsCarbPerServing;
	private int gramsProtPerServing;
	private int gramsFatPerServing;
	private double internalCoefficient;


	//Constructors - any fields that are not explicitly set will be set to default values
	public FoodItem(String foodName, int id, int value, String unit, int cals, int carbs, int prot, int fat){
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
	public FoodItem(String name, int calories, int protein, int fat, int carbs) {
		this.name = name;
		this.calPerServing = calories;
		this.gramsProtPerServing = protein;
		this.gramsFatPerServing = fat;
		this.gramsCarbPerServing = carbs;
	}

	public FoodItem(JSONObject fromObject){
	    this.name = fromObject.getString("name");
        this.foodId = fromObject.getInt("foodId");
        this.servingValue = fromObject.getInt("servingValue");
        this.servingUnit = fromObject.getString("servingUnit");
        this.calPerServing = fromObject.getInt("calPerServing");
        this.gramsCarbPerServing = fromObject.getInt("gramsCarbPerServing");
        this.gramsProtPerServing = fromObject.getInt("gramsProtPerServing");
        this.gramsFatPerServing = fromObject.getInt("gramsFatPerServing");
        this.internalCoefficient = fromObject.getDouble("internalCoefficient");
    }

	//Getters
	public String getName() {
		return name;
	}

	public int getFoodId() {
		return foodId;
	}

	public int getServingValue() {
		return servingValue;
	}

	public String getServingUnit() {
		return servingUnit;
	}

	public int getCalPerServing() {
		return calPerServing;
	}

	public int getGramsCarbPerServing() {
		return gramsCarbPerServing;
	}

	public int getGramsProtPerServing() {
		return gramsProtPerServing;
	}

	public int getGramsFatPerServing() {
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

	public void setServingValue(int servingValue) {
		this.servingValue = servingValue;
	}

	public void setServingUnit(String servingUnit) {
		this.servingUnit = servingUnit;
	}

	public void setCalPerServing(int calPerServing) {
		this.calPerServing = calPerServing;
	}

	public void setGramsCarbPerServing(int gramsCarbPerServing) {
		this.gramsCarbPerServing = gramsCarbPerServing;
	}

	public void setGramsProtPerServing(int gramsProtPerServing) {
		this.gramsProtPerServing = gramsProtPerServing;
	}

	public void setGramsFatPerServing(int gramsFatPerServing) {
		this.gramsFatPerServing = gramsFatPerServing;
	}

	//Derived Getters - returns a useful modifier over or combination of fields
	public int getCalsCarbPerServing(){return this.getGramsCarbPerServing() * 4;}
	public int getCalsProtPerServing(){return this.getGramsProtPerServing() * 4;}
	public int getCalsFatPerServing(){return this.getGramsFatPerServing() * 9;}
	public String getServingSize(){return this.getServingValue() + " " + this.getServingUnit();}


    public JSONObject toJSON(){
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
}
