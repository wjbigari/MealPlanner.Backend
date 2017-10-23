package Server;



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
}
