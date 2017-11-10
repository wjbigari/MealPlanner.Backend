package Models;

import org.json.JSONException;
import org.json.JSONObject;

public interface MealItemContent {
    //Getters
    public String getName();
    public int getFoodId();
    public double getServingValue();
    public String getServingUnit();
    public double getCalPerServing();
    public double getGramsCarbPerServing();
    public double getGramsProtPerServing();
    public double getGramsFatPerServing();
    public double getInternalCoefficient();

    //Setters
    public void setName(String name);
    public void setFoodId(int id);
    public void setServingValue(double val);
    public void setServingUnit(String unit);
    public void setInternalCoefficient(double coefficient);

    //Functions
    public long getCalsCarbPerServing();
    public long getCalsProtPerServing();
    public long getCalsFatPerServing();
    public String getServingSize();
    public JSONObject toJson() throws JSONException;
}
