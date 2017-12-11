package MealPlanner;

import java.util.ArrayList;

import org.json.JSONException;

import Models.Constraints;
import Models.FoodItem;
import Models.MealItem;
import Models.MealPlannerRequest;
import Models.MealPlannerRec;
import Models.UserRecipe;

public class Driver {
	
	public static void main(String args[]) throws InterruptedException, JSONException{
		//Name, calories, protein, fat, carbs
		//FoodItem pizza = new FoodItem("Pizza", 168, 7, 6, 20);
		//FoodItem pineapple = new FoodItem("Pineapple", 1, 2, "oz.", 82, 21, 1, 0);
		//FoodItem bread = new FoodItem("Bread", 2, 1, "slice(s)", 60, 12, 3, 1);
		//FoodItem milk = new FoodItem("Milk", 3, 8, "fluid oz.", 61, 5, 3, 3);
		//FoodItem cheese = new FoodItem("Cheese", 4, 2, "oz.", 69, 0, 4, 6);
		//FoodItem broccoli = new FoodItem("Broccoli", 5, 1, "cup(s)", 31, 6, 3, 0);
		//FoodItem spinach = new FoodItem("Spinach", 6, 1, "cup(s)", 7, 1, 1, 0);
		//FoodItem potato = new FoodItem("Potato", 7, 1, "potato(es)", 278, 63, 7, 0);
		//FoodItem banana = new FoodItem("Banana", 8, 1, "banana(s)", 200, 51, 2, 1);
		//FoodItem egg = new FoodItem("Egg", 9, 1, "egg(s)", 70, 0, 7, 5);
		//FoodItem yogurt = new FoodItem("Yogurt", 10, 4, "oz.", 80, 6, 15, 0);
		//FoodItem proteinPowder = new FoodItem("Protein Powder", 11, 1, "tbsp.", 120, 3, 25, 1);
		FoodItem chicken = new FoodItem("Chicken", 12, 3, "oz.", 100, 0, 21, 2);
		//FoodItem beef = new FoodItem("Beef", 13, 4, "oz.", 130, 0, 24, 4);
		//FoodItem almonds = new FoodItem("Almonds", 14, 1, "cup(s)", 828, 31, 30, 71);
		//FoodItem salmon = new FoodItem("Salmon", 15, 4, "oz.", 133, 0, 23, 4);
		//FoodItem pbj = new FoodItem("PB&J", 16, 1, "sandwich(es)", 370, 24, 12, 16);
		//FoodItem cheesecake = new FoodItem("Cheesecake", 17, 1, "slice(s)", 257, 20, 4, 18);
		//FoodItem whiskey = new FoodItem("Whiskey", 18, 2, "fluid oz.", 385, 44, 16, 16);
		//FoodItem bacon = new FoodItem("Bacon", 19, 2, "strip(s)", 431, 25, 15, 16);
		//FoodItem gatorade = new FoodItem("Gatorade", 20, 1, "cup(s)", 57, 14, 0, 0);
		
//		UserRecipe baconEggs = new UserRecipe("Bacon and Eggs", 1001);
//		baconEggs.setNumPortions(2);
//		baconEggs.addRecipeItem(bacon, 2);
//		baconEggs.addRecipeItem(egg, 4);
//
//		UserRecipe energyShake = new UserRecipe("Energy Shake", 1002);
//		energyShake.setNumPortions(1);
//		energyShake.addRecipeItem(milk, 2);
//		energyShake.addRecipeItem(proteinPowder, 1);
//		energyShake.addRecipeItem(banana, 1);
//

		//MealItem m_pizza = new MealItem(pizza, MealItem.Meal.LUNCH);
		MealItem m_chicken = new MealItem(chicken, MealItem.Meal.DINNER);
		MealItem m_chicken2 = new MealItem(chicken, MealItem.Meal.LUNCH);
		MealItem m_chicken3 = new MealItem(chicken, MealItem.Meal.BREAKFAST);
//
//		MealItem m_pineapple = new MealItem(pineapple, MealItem.Meal.LUNCH);
//		MealItem m_bread = new MealItem(bread, MealItem.Meal.LUNCH);
//		MealItem m_milk = new MealItem(milk, MealItem.Meal.BREAKFAST);
//		MealItem m_cheese = new MealItem(cheese, MealItem.Meal.DINNER);
//		MealItem m_broccoli = new MealItem(broccoli, MealItem.Meal.DINNER);
//		MealItem m_spinach = new MealItem(spinach, MealItem.Meal.LUNCH);
//		MealItem m_potato = new MealItem(potato, MealItem.Meal.DINNER);
//		MealItem m_banana = new MealItem(banana, MealItem.Meal.BREAKFAST);
//		MealItem m_egg = new MealItem(egg, MealItem.Meal.BREAKFAST);
//		MealItem m_yogurt = new MealItem(yogurt, MealItem.Meal.LUNCH);
//		MealItem m_proteinPowder = new MealItem(proteinPowder, MealItem.Meal.BREAKFAST);
//
//		MealItem m_beef = new MealItem(beef, MealItem.Meal.DINNER);
//		MealItem m_almonds = new MealItem(almonds, MealItem.Meal.LUNCH);
//		MealItem m_salmon = new MealItem(salmon, MealItem.Meal.DINNER);
//		MealItem m_pbj = new MealItem(pbj, MealItem.Meal.LUNCH);
//		MealItem m_cheesecake = new MealItem(cheesecake, MealItem.Meal.DINNER);
//		MealItem m_whiskey = new MealItem(whiskey, MealItem.Meal.BREAKFAST);
//		MealItem m_bacon = new MealItem(bacon, MealItem.Meal.BREAKFAST);
//		MealItem m_gatorade = new MealItem(gatorade, MealItem.Meal.LUNCH);
//
//		MealItem m_baconEggs = new MealItem(baconEggs, MealItem.Meal.BREAKFAST);
//		MealItem m_energyShake = new MealItem(energyShake, MealItem.Meal.LUNCH);
//

		//Food[] foods = {pizza, pineapple, bread, milk};
		ArrayList<MealItem> meal = new ArrayList<>();
		meal.add(m_chicken);
		meal.add(m_chicken2);
		meal.add(m_chicken3);
//		meal.add(m_milk); meal.add(m_bread); meal.add(m_pineapple); //meal.add(m_pizza);
//		meal.add(m_cheese); meal.add(m_broccoli); meal.add(m_spinach); meal.add(m_potato);
//		meal.add(m_banana); meal.add(m_egg); meal.add(m_yogurt); meal.add(m_proteinPowder);
//		meal.add(m_chicken); meal.add(m_beef); meal.add(m_almonds); meal.add(m_salmon);
//		meal.add(m_pbj); meal.add(m_cheesecake); meal.add(m_whiskey); meal.add(m_bacon); meal.add(m_gatorade);
//		meal.add(m_baconEggs); meal.add(m_energyShake);


		Constraints c = new Constraints(
				2200, 2300,			//Min Cals, Max Cals
				150, 170,			//Min Carbs, Max Carbs (in g)
				150, 210,			//Min Prot, Max Prot  (in g)
				80, 85);			//Min Fat, Max Fat  (in g)

		MealPlannerRequest request = new MealPlannerRequest(meal, c);
		//System.out.println(request.toJson().toString(2));
		MealPlanner.setDebugMode(true);
		MealPlannerRec rec = MealPlanner.createMealPlan(request);
		
		System.out.print(rec.toString());
		//System.out.println("\n\n\n\n\n\n\n" + rec.toJson().toString(2));

	}
}
