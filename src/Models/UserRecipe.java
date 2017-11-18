package Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UserRecipe implements Serializable, MealItemContent {

	//Values used for identifying the food item
    private String name;
    private int foodId;
    //Values used for storing recipe information
    private ArrayList<RecipeItem> ingredients;
    private int numPortions; //Number of servings in the recipe total, not a single serving amount
    private String portionName;
    private String prepInstructions;
    //Part of the balancing algorithm - not for use outside the MealPlanner
    private double internalCoefficient;


    //Constructors - any fields that are not explicitly set will be set to default values
    public UserRecipe(String foodName, int id, ArrayList<RecipeItem> ing, int portions, String portionName, String instructions){
        this.name = foodName;
        this.foodId = id;
        this.ingredients = ing;
        this.numPortions = portions;
        this.portionName = portionName;
        this.prepInstructions = instructions;
    }
    public UserRecipe(){
        this.name = "default";
        this.foodId = -1;
        this.ingredients = new ArrayList<RecipeItem>();
        this.numPortions = 1;
        this.portionName = "portion(s)";
        this.prepInstructions = "";
    }
    public UserRecipe(String recipeName){
        this();
        this.name = recipeName;
    }
    public UserRecipe(int id){
        this();
        this.foodId = id;
    }
    public UserRecipe(String recipeName, int id){
        this();
        this.name = recipeName;
        this.foodId = id;
    }

    public UserRecipe(JSONObject in) throws JSONException {
        System.out.println(in.toString());
        this.name = in.optString("uname");
        this.foodId = in.optInt("foodId");
        this.ingredients = new ArrayList<RecipeItem>();
        JSONArray ingIn = new JSONArray(in.getString("ingredients"));
        for(int i = 0; i < ingIn.length(); i++){
            String ing = ingIn.getString(i);
            System.out.println(ing);
            this.ingredients.add(new RecipeItem(new JSONObject(ing)));
        }
        this.numPortions = in.optInt("numPortions");
        this.portionName = in.optString("portionName");
        this.prepInstructions = in.optString("prepInstructions");
        this.internalCoefficient = in.optDouble("internalCoefficient");
    }

    //Getters
    public String getName() {
        return name;
    }

    public int getFoodId() {
        return foodId;
    }

    public double getServingValue() {
        return 1.0;
    }

    public String getServingUnit() {
        return portionName;
    }

    public double getCalPerServing() {
        double cals = 0.0;
        for(RecipeItem item : ingredients){
            cals += (item.getNumServings() * item.getFoodItem().getCalPerServing());
        }
        return cals / this.numPortions;
    }

    public double getGramsCarbPerServing() {
        double carbs = 0.0;
        for(RecipeItem item : ingredients){
            carbs += (item.getNumServings() * item.getFoodItem().getGramsCarbPerServing());
        }
        return carbs / this.numPortions;
    }

    public double getGramsProtPerServing() {
        double prot = 0.0;
        for(RecipeItem item : ingredients){
            prot += (item.getNumServings() * item.getFoodItem().getGramsProtPerServing());
        }
        return prot / this.numPortions;
    }

    public double getGramsFatPerServing() {
        double fat = 0.0;
        for(RecipeItem item : ingredients){
            fat += (item.getNumServings() * item.getFoodItem().getGramsFatPerServing());
        }
        return fat / this.numPortions;
    }

    public String getPrepInstructions(){
        return this.prepInstructions;
    }

    public ArrayList<RecipeItem> getIngredients(){
        return this.ingredients;
    }

    public int getNumPortions(){
        return this.numPortions;
    }

    public double getInternalCoefficient() {
        return internalCoefficient;
    }

    //Setters
    public void setInternalCoefficient(double d)
    {
        this.internalCoefficient = d;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public void setServingValue(double servingValue) {
        this.numPortions = (int)Math.round(servingValue);
    }

    public void setServingUnit(String servingUnit) {
        this.portionName = servingUnit;
    }

    public void setPrepInstructions(String inst){
        this.prepInstructions = inst;
    }

    public void setIngredients(ArrayList<RecipeItem> ings){
        this.ingredients = ings;
    }

    public void setNumPortions(int num){
        this.numPortions = num;
    }


    //Derived Getters - returns a useful modifier over or combination of fields
    public long getCalsCarbPerServing(){return Math.round(this.getGramsCarbPerServing() * 4);}
    public long getCalsProtPerServing(){return Math.round(this.getGramsProtPerServing() * 4);}
    public long getCalsFatPerServing(){return Math.round(this.getGramsFatPerServing() * 9);}
    public String getServingSize(){return this.getServingValue() + " " + this.getServingUnit();}

    //Recipe-specific functions
    public void addRecipeItem(FoodItem food, int numServ){
        boolean match = false;
        for(RecipeItem item : ingredients){
            if(item.getFoodItem().equals(food)) {
                item.setNumServings(item.getNumServings() + numServ);
                match = true;
            }
        }
        if(!match) ingredients.add(new RecipeItem(food, numServ));
    }


    public JSONObject toJson() throws JSONException{
        JSONObject out = new JSONObject();
        out.put("uname", this.name);
        out.put("foodId", this.foodId);
        JSONArray ing = new JSONArray();
        for(RecipeItem item : this.ingredients){
            ing.put(item.toJson().toString());
        }
        out.put("ingredients", ing.toString());
        out.put("numPortions", this.numPortions);
        out.put("portionName", this.portionName);
        out.put("prepInstructions", this.prepInstructions);
        return out;
    }
    //Equals override checks whether all relevant fields of this FoodItem object are the same as the FoodItem object passed in
    @Override
    public boolean equals(Object o){
        if(!(o instanceof UserRecipe)) return false;
        UserRecipe other = (UserRecipe)o;
        return this.ingredients.containsAll(other.getIngredients())
                && other.getIngredients().containsAll(this.ingredients)
                && this.numPortions == other.getNumPortions();
    }
}
