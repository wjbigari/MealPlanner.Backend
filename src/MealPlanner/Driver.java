//package Server;
//
//
//
//import java.util.ArrayList;
//
//public class Driver {
//	public static void main(String args[]) throws InterruptedException{
//		//Name, calories, protein, fat, carbs
//		//FoodItem pizza = new FoodItem("Pizza", 168, 7, 6, 20);
//		FoodItem pineapple = new FoodItem("Pineapple", 82, 1, 0, 21);
//		FoodItem bread = new FoodItem("Bread", 60, 3, 1, 12);
//		FoodItem milk = new FoodItem("Milk", 61, 3, 3, 5);
//		FoodItem cheese = new FoodItem("Milk", 69, 4, 6, 0);
//		FoodItem broccoli = new FoodItem("Broccoli", 31, 3, 0, 6);
//		FoodItem spinach = new FoodItem("Spinach", 7, 1, 0, 1);
//		FoodItem potato = new FoodItem("Potato", 278, 7, 0, 63);
//		FoodItem banana = new FoodItem("Banana", 200, 2, 1, 51);
//		FoodItem egg = new FoodItem("Egg", 70, 7, 5, 0);
//		FoodItem yogurt = new FoodItem("Yogurt", 80, 15, 0, 6);
//		FoodItem proteinPowder = new FoodItem("Protein Powder", 120, 25, 1, 3);
//
//		//MealItem m_pizza = new MealItem(pizza, MealItem.Meal.LUNCH);
//		MealItem m_pineapple = new MealItem(pineapple, MealItem.Meal.LUNCH);
//		MealItem m_bread = new MealItem(bread, MealItem.Meal.LUNCH);
//		MealItem m_milk = new MealItem(milk, MealItem.Meal.LUNCH);
//		MealItem m_cheese = new MealItem(cheese, MealItem.Meal.LUNCH);
//		MealItem m_broccoli = new MealItem(broccoli, MealItem.Meal.LUNCH);
//		MealItem m_spinach = new MealItem(spinach, MealItem.Meal.LUNCH);
//		MealItem m_potato = new MealItem(potato, MealItem.Meal.LUNCH);
//		MealItem m_banana = new MealItem(banana, MealItem.Meal.LUNCH);
//		MealItem m_egg = new MealItem(egg, MealItem.Meal.LUNCH);
//		MealItem m_yogurt = new MealItem(yogurt, MealItem.Meal.LUNCH);
//		MealItem m_proteinPowder = new MealItem(proteinPowder, MealItem.Meal.LUNCH);
//
//		//Food[] foods = {pizza, pineapple, bread, milk};
//		ArrayList<MealItem> meal = new ArrayList();
//		meal.add(m_milk); meal.add(m_bread); meal.add(m_pineapple); //meal.add(m_pizza);
//		meal.add(m_cheese); meal.add(m_broccoli); meal.add(m_spinach); meal.add(m_potato);
//		meal.add(m_banana); meal.add(m_egg); meal.add(m_yogurt); meal.add(m_proteinPowder);
//
//
//		MealPlanner mp = new MealPlanner();
//		Constraints c = new Constraints(
//				2200, 2400,
//				250, 320,
//				100, 130,
//				80, 120);
//		//Calories = 2200 - 2400
//		//Carbs = 250 - 320
//		//Protein = 100 - 130
//		//Fat = 80 - 120
//
//
//		ArrayList<MealItem> Meal = mp.selectMeals(c, meal);
//		//System.out.println("********************");
//
//		mp.printMeal(c, Meal);
//	}
//}
