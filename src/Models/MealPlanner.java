//package Server;
//
//
//
//import java.util.ArrayList;
//
//public class MealPlanner {
//
//	//TODO: Change the return type to a JSON-formatted String
//	public ArrayList<MealItem> createMealPlan(ArrayList<MealItem> mealItems, Constraints constraints){
//		ArrayList<MealItem> finalItems = new ArrayList<>();
//
//		//1. Iterate over the MealItems, identify Locked items with a set quantity, move them to the final list and adjust the Constraints
//		for(MealItem item : mealItems){
//			if(item.isLocked() && item.getNumServings() > 0){
//				adjustConstraints(item, constraints);
//				mealItems.remove(item);
//				finalItems.add(item);
//			}
//		}
//		//2. TODO: Refactor out the fitness function and instead use a Factory class to build one dynamically using the contents of the Constraints
//
//
//		//3. TODO: Send a request over to the main algorithm using the adjusted list of MealItems and the adjusted Constraints
//
//
//		//4. Iterate over the MealItems, identify all items with a non-zero quantity, and move them to the final list
//		for(MealItem item : mealItems){
//			if(item.getNumServings() > 0){
//				finalItems.add(item);
//			}
//		}
//		//5. TODO: Serialize the finalItems list into JSON format before returning
//		return finalItems;
//	}
//
//	public MealItem[] createMealArray(ArrayList<MealItem> meal) {
//		MealItem[] ret = new MealItem[meal.size()];
//		for (int i = 0 ; i < meal.size(); i++) {
//			ret[i] = meal.get(i);
//		}
//		return ret;
//	}
//
//	public ArrayList<MealItem> selectMeals(Constraints c, ArrayList<MealItem> meal_Items) throws InterruptedException{
//		MealItem[] items = createMealArray(meal_Items);
//
////		int iterations = 0;
////		while(true){
////			if (iterations > 1024) break;
////			breakfast = breakfast.getBestNeighbor(items, c);
////			//breakfast.printMeal(c);
////			//Thread.sleep(10);
////			iterations++;
////		}
////		breakfast.printMeal(c);
//
//		ArrayList<Object> results = new ArrayList<Object>();
//		for(int i = 0; i < 30; i++){
//			double temp = 1.0;
//			ArrayList<MealItem> currentDish = generateRandomMeal(items, c);
//			while(temp >= 0.05){
//				temp *= 0.97;
//				//Thread.sleep(30);
//				//Pick a random value [0...1] - if the random value is lower than the current temperature,
//				// switch the current dish to one of its neighbors randomly
//				if(Math.random() < temp){
//					currentDish = getRandomNeighbor(items, c, currentDish);
//					//dishHistory.add(randomNeighbor);
//					//currentDish = randomNeighbor;
//				}
//				//If the random value is not lower than the current temperature, instead find the best
//				// neighbor from the current configuration, and update current dish via total cost comparison
//				else{
//					currentDish = getBestNeighbor(items, c, currentDish);
//					//if(updateTotalCals(bestNeighbor) < updateTotalCals(currentDish))
//					//	currentDish = bestNeighbor;
//				}
//				//results.add(currentDish);
//			}
//			//Add the best Dish from this run to the set of best Dishes
//			results.add(currentDish);
//		}
//		//printMeal(c, getBestDish(results,c));
//		return getBestDish(results,c); //Return the best Dish out of all runs
//	}
//
//	public ArrayList<MealItem> getRandomNeighbor(MealItem[] items, Constraints c, ArrayList<MealItem> meal) {
//		int totalCals = updateTotalCals(meal);
//		if ((Math.random() > 0.5 || totalCals > c.getMinCals()) && items.length > 1) {
//			MealItem m = meal.get(0);
//			meal.remove(m);
//		}
//		else {
//			meal.add(items[(int) (Math.random() * items.length)]);
//		}
//		//printMeal(c, meal);
//		return meal;
//	}
//
//	public void printMeal(Constraints c, ArrayList<MealItem> meal) {
//		System.out.println("{");
//		ArrayList<Integer> countItems = new ArrayList<>();
//		ArrayList<String> foods = new ArrayList<>();
//		for (int i = 0; i < meal.size(); i++) {
//			if (!foods.contains(meal.get(i).getFoodItem().getName()))
//			{
//				foods.add(meal.get(i).getFoodItem().getName());
//				countItems.add(1);
//			}
//			else {
//				int index = foods.indexOf(meal.get(i).getFoodItem().getName());
//				countItems.set(index, countItems.get(index)+1);
//			}
//		}
//		for (int i = 0 ; i < foods.size(); i++){
//			System.out.println("  " + countItems.get(i) + " " + foods.get(i));
//		}
//
//		System.out.println();
//		System.out.println("  Fitness = " + getDishFitness(meal, c));
//		System.out.println("  Calories = " + updateTotalCals(meal));
//		System.out.println("  Fat = " + getTotalFat(meal));
//		System.out.println("  Protein = " + getTotalProtein(meal));
//		System.out.println("  Carbs = " + getTotalCarbs(meal));
//		System.out.println("}");
//	}
//
//	private int getTotalFat(ArrayList<MealItem> meal) {
//		int total = 0;
//		for (int i = 0; i < meal.size(); i++) {
//			total += meal.get(i).getFoodItem().getGramsFatPerServing();
//		}
//		return total;
//	}
//	private int getTotalProtein(ArrayList<MealItem> meal) {
//		int total = 0;
//		for (int i = 0; i < meal.size(); i++) {
//			total += meal.get(i).getFoodItem().getGramsProtPerServing();
//		}
//		return total;
//	}
//	private int getTotalCarbs(ArrayList<MealItem> meal) {
//		int total = 0;
//		for (int i = 0; i < meal.size(); i++) {
//			total += meal.get(i).getFoodItem().getGramsCarbPerServing();
//		}
//		return total;
//	}
//
//	@SuppressWarnings("unchecked")
//	private ArrayList<MealItem> getBestDish(ArrayList<Object> results, Constraints c) {
//		double best = 0;
//		int index = 0;
//		double score = 0;
//		for (int i = 0 ; i < results.size(); i++){
//			score = getDishFitness((ArrayList<MealItem>) results.get(i), c);
//			if (score > best) {
//				best = score;
//				index = i;
//			}
//		}
//		//printMeal(c, (ArrayList<MealItem>) results.get(index));
//		return (ArrayList<MealItem>) results.get(index);
//	}
//
//	public ArrayList<MealItem> generateRandomMeal(MealItem[] items, Constraints c) {
//		ArrayList<MealItem> meal = new ArrayList();
//		//meal.clear();
//		int totalCals = 0;
//		while (totalCals < c.getMinCals()) {
//			meal.add(items[(int) (Math.random() * items.length)]);
//			totalCals = updateTotalCals(meal);
//		}
//		return meal;
//	}
//
//	//Meal Plan selector helper method
//	private ArrayList<MealItem> getBestNeighbor(MealItem[] items, Constraints c, ArrayList<MealItem> meal) {
//		//Gets best neighbor but prioritizes adding a food item, to remove the possiblity of endless looping with very few items
//		MealItem best = items[0];
//		boolean added = false;
//		int totalCals = 0;
//		double bestScore = 0;
//		double score = 0;
//		for (int i = 0; i < items.length; i++) {
//			meal.add(items[i]);
//			totalCals = updateTotalCals(meal);
//			score = getDishFitness(meal, c);
//			if (score > bestScore && totalCals < c.getMaxCals()) {
//				bestScore = score;
//				best = items[i];
//				added = true;
//			}
//			meal.remove(items[i]);
//		}
//		if (added) {
//			meal.add(best);
//			return meal;
//		}
//		MealItem backup = null;
//		if (meal.size() > 1) {
//			for (int i = 0; i < meal.size(); i++) {
//				backup = meal.get(i);
//				meal.remove(i);
//				score = getDishFitness(meal, c);
//				if (score > bestScore) {
//					bestScore = score;
//					added = false;
//					best = backup;
//				}
//				meal.add(backup);
//			}
//		}
//		if (added)
//			meal.add(best);
//		else
//			meal.remove(best);
//
//		//printMeal(c, meal);
//		return meal;
//	}
//
//	private int updateTotalCals(ArrayList<MealItem> meal) {
//		int totalCals = 0;
//		for (int i = 0; i < meal.size(); i++) {
//			totalCals += meal.get(i).getFoodItem().getCalPerServing();
//		}
//		return totalCals;
//	}
//
//	public double getDishFitness(ArrayList<MealItem> meal, Constraints c) {
//		FoodItem dish = new FoodItem("Dish1", 0, 0, 0, 0);
//		for (int i = 0; i < meal.size(); i++) {
//			dish.setCalPerServing(dish.getCalPerServing() + meal.get(i).getFoodItem().getCalPerServing());
//			dish.setGramsCarbPerServing(dish.getGramsCarbPerServing() + meal.get(i).getFoodItem().getGramsCarbPerServing());
//			dish.setGramsFatPerServing(dish.getGramsFatPerServing() + meal.get(i).getFoodItem().getGramsFatPerServing());
//			dish.setGramsProtPerServing(dish.getGramsProtPerServing() + meal.get(i).getFoodItem().getGramsProtPerServing());
//		}
//		if (meal.size() > 0)
//			dish.setInternalCoefficient(1.00 * Math.pow(1.01, countElement(meal, getPopularElement(meal))));
//		//return getFoodFitness(dish, c) * (dish.getCalories()/c.getMaxCalories());
//		double ret = getFoodFitness(dish, c) / dish.getInternalCoefficient();
//		//extra double for testing purposes
//		return ret;
//	}
//
//	private double getFoodFitness(FoodItem food, Constraints c) {
//		double fatFitness = food.getCalsFatPerServing() / (double)food.getCalPerServing();
//		double proteinFitness = food.getCalsProtPerServing() / (double)food.getCalPerServing();
//		double carbFitness = food.getCalsCarbPerServing() / (double)food.getCalPerServing();
//
//		double fatConstraint = (9*((double)c.getMaxFat()+c.getMinFat())/2) / ((c.getMaxCals()+(double)c.getMinCals())/2);
//		double proConstraint = (4*((double)c.getMaxProt()+c.getMinProt())/2) / ((c.getMaxCals()+(double)c.getMinCals())/2);
//		double carConstraint = (4*((double)c.getMaxCarbs()+c.getMinCarbs())/2) / ((c.getMaxCals()+(double)c.getMinCals())/2);
//
//		double fitnessTotal = 1.0;
//		fitnessTotal -= (Math.abs(fatConstraint - fatFitness));
//		fitnessTotal -= (Math.abs(proConstraint - proteinFitness));
//		fitnessTotal -= (Math.abs(carConstraint - carbFitness));
//
////		double fitnessTotal = (( ((c.getMaxProt()+c.getMinProt()) /2 ) *4)/(double)((c.getMaxCals())+c.getMinCals())/2) * proteinFitness;
////		fitnessTotal += ((((c.getMaxFat()+c.getMinFat()) /2 ) *9)/(double)((c.getMaxCals())+c.getMinCals())/2) * fatFitness;
////		fitnessTotal += ((((c.getMaxCarbs()+c.getMinCarbs()) /2 ) *4)/(double)((c.getMaxCals())+c.getMinCals())/2) * carbFitness;
////		fitnessTotal += 1;
////		fitnessTotal *= fitnessTotal;
//
//		return fitnessTotal;
//	}
//
//	public int countElement(ArrayList<MealItem> meal, MealItem elem){
//		int count = 0;
//		for (int i = 0 ; i < meal.size(); i++){
//			if (meal.get(i).getFoodItem().getName().equals(elem.getFoodItem().getName()))
//				count++;
//		}
//		return count;
//	}
//
//	public MealItem getPopularElement(ArrayList<MealItem> meal)
//	{
//	  if (meal.size() == 0) return null;
//	  int count = 1;
//	  int tempCount;
//	  MealItem popular = meal.get(0);
//	  MealItem temp = null;
//	  for (int i = 0; i < (meal.size() - 1); i++)
//	  {
//	    temp = meal.get(i);
//	    tempCount = 0;
//	    for (int j = 1; j < meal.size(); j++)
//	    {
//	      if (temp.getFoodItem().getName().equals(meal.get(j).getFoodItem().getName()))
//	        tempCount++;
//	    }
//	    if (tempCount > count)
//	    {
//	      popular = temp;
//	      count = tempCount;
//	    }
//	  }
//	  return popular;
//	}
//
//	//Helper method for use with the first step of the Meal Planner request handling - for each field of the Constraints, if that field
//	// has a positive value, reduce it by the number of servings specified for the Locked item multiplied by the Locked item's corresponding field value
//	private void adjustConstraints(MealItem mealItem, Constraints constraints){
//		if(constraints.getMinCals() > 0) constraints.setMinCals(Math.max(constraints.getMinCals() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalPerServing()), 0));
//		if(constraints.getMaxCals() > 0) constraints.setMaxCals(Math.max(constraints.getMaxCals() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalPerServing()), 0));
//		if(constraints.getMinCarbs() > 0) constraints.setMinCarbs(Math.max(constraints.getMinCarbs() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalsCarbPerServing()), 0));
//		if(constraints.getMaxCarbs() > 0) constraints.setMaxCarbs(Math.max(constraints.getMaxCarbs() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalsCarbPerServing()), 0));
//		if(constraints.getMinProt() > 0) constraints.setMinProt(Math.max(constraints.getMinProt() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalsProtPerServing()), 0));
//		if(constraints.getMaxProt() > 0) constraints.setMaxProt(Math.max(constraints.getMaxProt() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalsProtPerServing()), 0));
//		if(constraints.getMinFat() > 0) constraints.setMinFat(Math.max(constraints.getMinFat() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalsFatPerServing()), 0));
//		if(constraints.getMaxFat() > 0) constraints.setMaxFat(Math.max(constraints.getMaxFat() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalsFatPerServing()), 0));
//	}
//}
