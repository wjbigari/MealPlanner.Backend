package MealPlanner;

import java.util.ArrayList;

import org.json.JSONException;

import Models.Constraints;
import Models.FoodItem;
import Models.MealItem;
import Models.MealPlannerRequest;
import Models.MealPlannerRec;

public class Driver {
	
	public static void main(String args[]) throws InterruptedException, JSONException{
		//Name, calories, protein, fat, carbs
		//FoodItem pizza = new FoodItem("Pizza", 168, 7, 6, 20);
		FoodItem pineapple = new FoodItem("Pineapple", 1, 2, "oz.", 82, 1, 0, 21);
		FoodItem bread = new FoodItem("Bread", 2, 1, "slice(s)", 60, 3, 1, 12);
		FoodItem milk = new FoodItem("Milk", 3, 8, "fluid oz.", 61, 3, 3, 5);
		FoodItem cheese = new FoodItem("Cheese", 4, 2, "oz.", 69, 4, 6, 0);
		FoodItem broccoli = new FoodItem("Broccoli", 5, 1, "cup(s)", 31, 3, 0, 6);
		FoodItem spinach = new FoodItem("Spinach", 6, 1, "cup(s)", 7, 1, 0, 1);
		FoodItem potato = new FoodItem("Potato", 7, 1, "potato(es)", 278, 7, 0, 63);
		FoodItem banana = new FoodItem("Banana", 8, 1, "banana(s)", 200, 2, 1, 51);
		FoodItem egg = new FoodItem("Egg", 9, 1, "egg(s)", 70, 7, 5, 0);
		FoodItem yogurt = new FoodItem("Yogurt", 10, 4, "oz.", 80, 15, 0, 6);
		FoodItem proteinPowder = new FoodItem("Protein Powder", 11, 1, "tbsp.", 120, 25, 1, 3);

		//MealItem m_pizza = new MealItem(pizza, MealItem.Meal.LUNCH);
		MealItem m_pineapple = new MealItem(pineapple, MealItem.Meal.LUNCH);
		MealItem m_bread = new MealItem(bread, MealItem.Meal.LUNCH);
		MealItem m_milk = new MealItem(milk, MealItem.Meal.DINNER);
		MealItem m_cheese = new MealItem(cheese, MealItem.Meal.DINNER);
		MealItem m_broccoli = new MealItem(broccoli, MealItem.Meal.DINNER);
		MealItem m_spinach = new MealItem(spinach, MealItem.Meal.LUNCH);
		MealItem m_potato = new MealItem(potato, MealItem.Meal.DINNER);
		MealItem m_banana = new MealItem(banana, MealItem.Meal.BREAKFAST);
		MealItem m_egg = new MealItem(egg, MealItem.Meal.BREAKFAST);
		MealItem m_yogurt = new MealItem(yogurt, MealItem.Meal.LUNCH);
		MealItem m_proteinPowder = new MealItem(proteinPowder, MealItem.Meal.BREAKFAST);

		//Food[] foods = {pizza, pineapple, bread, milk};
		ArrayList<MealItem> meal = new ArrayList<>();
		meal.add(m_milk); meal.add(m_bread); meal.add(m_pineapple); //meal.add(m_pizza);
		meal.add(m_cheese); meal.add(m_broccoli); meal.add(m_spinach); meal.add(m_potato);
		meal.add(m_banana); meal.add(m_egg); meal.add(m_yogurt); meal.add(m_proteinPowder);


		Constraints c = new Constraints(
				2000, 2700,			//Min Cals, Max Cals
				150, 175,			//Min Carbs, Max Carbs (in g)
				175, 190,			//Min Prot, Max Prot  (in g)
				60, 85);			//Min Fat, Max Fat  (in g)

		MealPlannerRequest request = new MealPlannerRequest(meal, c);
		MealPlannerRec rec = MealPlanner.createMealPlan(request);
		
		System.out.print(rec.toString());

	}
}
