package MealPlanner;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import Models.Constraints;
import Models.MealItem;
import Models.MealPlannerRec;
import Models.MealPlannerRequest;


public class MealPlanner {
	//Run statistics, for use with the dynamic function builder
	private static int totalRuns = 0;
	private static double multiplier = 0;
	//Global flags for enabling/disabling fitness function checks
	private static boolean checkCals = true;
	private static boolean checkCarbs = true;
	private static boolean checkProt = true;
	private static boolean checkFat = true;
	private static boolean noLocked = true;
	
	//MAIN HANDLER -- All MealPlannerRequest processing should be run through this function
	public static MealPlannerRec createMealPlan(MealPlannerRequest request) throws InterruptedException{
		MealPlannerRec result = new MealPlannerRec();
		
		//1. Adjust the passed in Constraints to fill in empty fields, fix negative values, and disable fitness checks for empty ranges
		Constraints con = request.getConstraints();
		Constraints adjustedConstraints = new Constraints(con.getMinCals(), con.getMaxCals(), con.getMinCarbs(), con.getMaxCarbs(), con.getMinProt(), con.getMaxProt(), con.getMinFat(), con.getMaxFat());		
		initializeConstraints(adjustedConstraints);

		//2. Iterate over the MealItems, identify Locked items with a set quantity, move them to the final list and adjust the Constraints;
		//  if a locked item is found with no set quantity, enable the Locked flag penalty for the fitness function
		ArrayList<MealItem> adjustedItems = new ArrayList<>();
		noLocked = true;
		for(MealItem item : request.getMealItems()){
			if(item.isLocked() && item.getNumServings() > 0){
				handleLockedQuantity(item, request.getConstraints());
				result.addItemToRec(item);
			}else{
				if(item.isLocked() && item.getNumServings() == 0) noLocked = false;
				adjustedItems.add(item);
			}
		}

		//3. Send the Request's adjusted Constraints and list of MealItems to the main algorithm, get back the adjusted counts
		ArrayList<MealItem> output = selectMeals(adjustedConstraints, adjustedItems);

		//4. Iterate over the algorithm output list and add non-zero quantity items to the final MealPlannerRec
		for(MealItem item : output){
			result.addItemToRec(item);
		}
		
		return result;
	}
	
	//Helper method for use with the first step of the Meal Planner request handling.  For each of the four ranges, performs the following:
	//  - if both Min and Max values are blank, trips the flag to ignore this range in all fitness checks
	//  - if Min is empty but Max is not, set Min to a value slightly smaller than Max
	//  - if Max is empty but Min is not, set Max to a value slightly larger than Min
	//  - if either Min or Max is a negative value, set it to 0 (a user explicitly set this field to 0 rather than leaving it blank)
	private static void initializeConstraints(Constraints con){
		if(con.getMinCals() == 0 && con.getMaxCals() == 0) checkCals = false;
		else checkCals = true;
		if(con.getMinCals() == 0) con.setMinCals(con.getMaxCals() * 0.95);
		if(con.getMaxCals() == 0) con.setMaxCals(con.getMinCals() * 1.05);
		if(con.getMinCals() < 0) con.setMinCals(0);
		if(con.getMaxCals() < 0) con.setMaxCals(0);
		if(con.getMinCarbs() == 0 && con.getMaxCarbs() == 0) checkCarbs = false;
		else checkCarbs = true;
		if(con.getMinCarbs() == 0) con.setMinCarbs(con.getMaxCarbs() * 0.9);
		if(con.getMaxCarbs() == 0) con.setMaxCarbs(con.getMinCarbs() * 1.1);
		if(con.getMinCarbs() < 0) con.setMinCarbs(0);
		if(con.getMaxCarbs() < 0) con.setMaxCarbs(0);
		if(con.getMinProt() == 0 && con.getMaxProt() == 0) checkProt = false;
		else checkProt = true;
		if(con.getMinProt() == 0) con.setMinProt(con.getMaxProt() * 0.9);
		if(con.getMaxProt() == 0) con.setMaxProt(con.getMinProt() * 1.1);
		if(con.getMinProt() < 0) con.setMinProt(0);
		if(con.getMaxProt() < 0) con.setMaxProt(0);
		if(con.getMinFat() == 0 && con.getMaxFat() == 0) checkFat = false;
		else checkFat = true;
		if(con.getMinFat() == 0) con.setMinFat(con.getMaxFat() * 0.9);
		if(con.getMaxFat() == 0) con.setMaxFat(con.getMinFat() * 1.1);
		if(con.getMinFat() < 0) con.setMinFat(0);
		if(con.getMaxFat() < 0) con.setMaxFat(0);
	}
	
	//Helper method for use with the second step of the Meal Planner request handling - for each field of the Constraints, if that field
	// has a positive value, reduce it by the number of servings specified for the Locked item multiplied by the Locked item's corresponding field value
	private static void handleLockedQuantity(MealItem mealItem, Constraints constraints) {
		constraints.setMinCals(Math.max(constraints.getMinCals() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalPerServing()), 0));
		constraints.setMaxCals(Math.max(constraints.getMaxCals() - (mealItem.getNumServings() * mealItem.getFoodItem().getCalPerServing()), 0));
		constraints.setMinCarbs(Math.max(constraints.getMinCarbs() - (mealItem.getNumServings() * mealItem.getFoodItem().getGramsCarbPerServing()), 0));
		constraints.setMaxCarbs(Math.max(constraints.getMaxCarbs() - (mealItem.getNumServings() * mealItem.getFoodItem().getGramsCarbPerServing()), 0));
		constraints.setMinProt(Math.max(constraints.getMinProt() - (mealItem.getNumServings() * mealItem.getFoodItem().getGramsProtPerServing()), 0));
		constraints.setMaxProt(Math.max(constraints.getMaxProt() - (mealItem.getNumServings() * mealItem.getFoodItem().getGramsProtPerServing()), 0));
		constraints.setMinFat(Math.max(constraints.getMinFat() - (mealItem.getNumServings() * mealItem.getFoodItem().getGramsFatPerServing()), 0));
		constraints.setMaxFat(Math.max(constraints.getMaxFat() - (mealItem.getNumServings() * mealItem.getFoodItem().getGramsFatPerServing()), 0));
	}
	
	//Helper method to set the terms of the simulated annealing function according to Constraints and number of MealItems
	private static void setGlobalVariables(int foodCount, Constraints c) {
		totalRuns = (int)(250 - (Math.pow(foodCount, 2) * 0.1) - ((c.getMaxCals() + c.getMinCals())/100));
		if (totalRuns < 30) totalRuns = 30;
		multiplier = 0.997 - (Math.pow(foodCount, 2) * 0.000025) - ((c.getMaxCals() + c.getMinCals())/750000);
		if (multiplier < 0.8) multiplier = 0.8;
	}

	public static ArrayList<MealItem> selectMeals(Constraints c, ArrayList<MealItem> meal_Items) throws InterruptedException{
		long startTime = System.currentTimeMillis();
		MealItem[] items = new MealItem[meal_Items.size()];
		for(int i = 0; i < items.length; ++i) items[i] = meal_Items.get(i);
		setGlobalVariables(items.length, c);
		
		MealConfig bestDish = null;
		double lowestOverallCost = Double.MAX_VALUE;

		for(int i = 0; i < totalRuns; i++){
			//Build a random dish to start the current run
			MealConfig currentDish = generateRandomMeal(items, c);
			ArrayList<MealConfig> neighbors;
			double lowestCost = Double.MAX_VALUE;
			//Repeatedly loop while lowering the temperature to select the best dish for this run
			double temp = 1.0;
			while(temp >= 0.05){
				temp *= multiplier;
				neighbors = getNeighbors(currentDish);
				//Pick a random value [0...1] - if the random value is lower than the current temperature,
				// switch the current dish with one of its neighbors randomly
				if(Math.random() < temp){
					currentDish = neighbors.get(ThreadLocalRandom.current().nextInt(neighbors.size()));
					lowestCost = currentDish.getFitness();
				}
				//If the random value is not lower, then find the neighboring dish with the lowest
				// fitness cost among all neighbors and make it the new current dish
				else {
					for(MealConfig neighbor : neighbors){
						double neighborCost = neighbor.getFitness();
						if(neighborCost < lowestCost){
							currentDish = neighbor;
							lowestCost = neighborCost;
						}
					}
				}
			}
			//Finish by performing a hill climb to the local optimum for the fitness function
			boolean hillClimb = true;
			while(hillClimb){
				hillClimb = false;
				neighbors = getNeighbors(currentDish);
				for(MealConfig neighbor : neighbors){
					double neighborCost = neighbor.getFitness();
					if(neighborCost < lowestCost){
						currentDish = neighbor;
						lowestCost = neighborCost;
						hillClimb = true;
					}
				}
			}
			//If the best dish from this run has the lowest overall fitness score, set it as the new best dish
			if(lowestCost < lowestOverallCost){
				bestDish = currentDish;
				lowestOverallCost = lowestCost;
			}
		}
		//Return with the best overall dish from among all of the runs
		ArrayList<MealItem> result = new ArrayList<>();
		if(bestDish != null){
			for(int i = 0; i < bestDish.items.length; ++i){
				result.add(bestDish.items[i]);
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.print("Run complete in " + (endTime - startTime) + " ms\nBest dish fitness: " + lowestOverallCost + "\n\n");
		return result;
	}
	
	//Create a new randomized meal by adding random MealItems to the MealConfig until every nutrient goal average has been satisfied;
	//  this will generate a non-zero-size meal so long as at least one of the Constraints ranges has an average value of greater than 0,
	//  and if all fields are set to open range or have a 0 min/max the configuration will correctly return with nothing in the MealConfig
	public static MealConfig generateRandomMeal(MealItem[] items, Constraints c) {
		MealConfig config = new MealConfig(c, items);
		while((checkCals && config.totalCals < ((c.getMinCals() + c.getMaxCals()) / 2))
				|| (checkCarbs && config.totalCarbs < ((config.minCalsCarb + config.maxCalsCarb) / 2))
				|| (checkProt && config.totalProt < ((config.minCalsProt + config.maxCalsProt) / 2))
				|| (checkFat && config.totalFat < ((config.minCalsFat + config.maxCalsFat) / 2))){
			config.addItem(ThreadLocalRandom.current().nextInt(items.length));
		}
		return config;
	}
	
	//Creates a list of "neighboring" MealConfigs from the current config, which can be one of 3 possibilities:
	//  - a config with exactly 1 serving more of a single MealItem
	//  - a config with exactly 1 serving fewer of a single MealItem
	//  - a config that swaps exactly 1 serving of a single MealItem for 1 serving of a different MealItem
	//  Note: this results in O(n^2) neighbors where n is the number of unique MealItems, so try to keep the count down!
	public static ArrayList<MealConfig> getNeighbors(MealConfig current){
		ArrayList<MealConfig> neighbors = new ArrayList<>();
		MealConfig next;
		//For each unique MealItem, create the list of neighbor configurations based on that item
		for(int i = 0; i < current.items.length; ++i){
			//Add a neighbor with 1 more serving of this MealItem
			next = current.clone();
			next.addItem(i);
			neighbors.add(next);
			if(current.items[i].getNumServings() > 0){
				//Add a neighbor with 1 less serving of this MealItem (if applicable)
				next = current.clone();
				next.removeItem(i);
				neighbors.add(next);
				//For each other unique MealItem, swap 1 serving of this item for 1 serving of that item (if applicable)
				for(int j = 0; j < current.items.length; ++j){
					if(i != j){
						next = current.clone();
						next.removeItem(i);
						next.addItem(j);
						neighbors.add(next);
					}
				}
			}
		}
		return neighbors;
	}

	//The MealConfig class is used exclusively inside of the MealPlanner for hosting one possible MealPlannerRec,
	// and includes the fitness function for determining the configuration's fitness per the given Constraints,
	// and a host of derived statistics to help save time when creating neighbor states and running the fitness function
	protected static class MealConfig{
		protected Constraints con;
		protected MealItem[] items;
		//Derived values for this MealConfig for use with the fitness function
		protected int numItems;
		protected int totalServings;
		protected int numBreakServ;
		protected int numLunchServ;
		protected int numDinnerServ;
		protected double totalCals;
		protected double totalCarbs;
		protected double totalProt;
		protected double totalFat;
		//Derived Constraints values for use with the fitness function
		protected double minCalsCarb;
		protected double maxCalsCarb;
		protected double minCalsProt;
		protected double maxCalsProt;
		protected double minCalsFat;
		protected double maxCalsFat;
		
		public MealConfig(){
			//Empty constructor - meant to be used with the clone function and ONLY with the clone function
		}
		
		//Constructor with arguments - pass in a Constraints and an array of MealItems and it will populate all other fields
		public MealConfig(Constraints c, MealItem[] it){
			con = c;
			items = it;
			for(int i = 0; i < it.length; ++i){
				if(it[i].getNumServings() > 0){
					MealItem item = it[i];
					int serv = item.getNumServings();
					numItems += 1;
					totalServings += serv;
					switch(item.getMeal()){
						case BREAKFAST:
							numBreakServ += serv;
							break;
						case LUNCH:
							numLunchServ += serv;
							break;
						case DINNER:
							numDinnerServ += serv;
							break;
					}
					totalCals += item.getFoodItem().getCalPerServing() * serv;
					totalCarbs += item.getFoodItem().getCalsCarbPerServing() * serv;
					totalProt += item.getFoodItem().getCalsProtPerServing() * serv;
					totalFat += item.getFoodItem().getCalsFatPerServing() * serv;
				}
			}
			minCalsCarb = c.getMinCarbs() * 4;
			maxCalsCarb = c.getMaxCarbs() * 4;
			minCalsProt = c.getMinProt() * 4;
			maxCalsProt = c.getMaxProt() * 4;
			minCalsFat = c.getMinFat() * 9;
			maxCalsFat = c.getMaxFat() * 9;
		}
		
		//Returns a single-level deep clone of this MealConfig (having duplicate pointers to MealItemContent is okay since it isn't being changed)
		public MealConfig clone(){
			MealConfig result = new MealConfig();
			result.con = new Constraints(con.getMinCals(), con.getMaxCals(), con.getMinCarbs(), con.getMaxCarbs(), con.getMinProt(), con.getMaxProt(), con.getMinFat(), con.getMaxFat());
			MealItem[] itemsCopy = new MealItem[items.length];
			for(int i = 0; i < itemsCopy.length; ++i){
				MealItem toCopy = items[i];
				itemsCopy[i] = new MealItem(toCopy.getFoodItem(), toCopy.isLocked(), toCopy.getNumServings(), toCopy.getMeal());
			}
			result.items = itemsCopy;
			result.numItems = numItems;
			result.totalServings = totalServings;
			result.numBreakServ = numBreakServ;
			result.numLunchServ = numLunchServ;
			result.numDinnerServ = numDinnerServ;
			result.totalCals = totalCals;
			result.totalCarbs = totalCarbs;
			result.totalProt = totalProt;
			result.totalFat = totalFat;
			result.minCalsCarb = minCalsCarb;
			result.maxCalsCarb = maxCalsCarb;
			result.minCalsProt = minCalsProt;
			result.maxCalsProt = maxCalsProt;
			result.minCalsFat = minCalsFat;
			result.maxCalsFat = maxCalsFat;
			return result;
		}
		
		//Adds one serving of the MealItem at the specified index of the items array and updates all related fields
		public void addItem(int index){
			MealItem item = items[index];
			if(item.getNumServings() == 0) numItems += 1;
			item.setNumServings(item.getNumServings() + 1);
			totalServings += 1;
			switch(item.getMeal()){
				case BREAKFAST:
					numBreakServ += 1;
					break;
				case LUNCH:
					numLunchServ += 1;
					break;
				case DINNER:
					numDinnerServ += 1;
					break;
			}
			totalCals += item.getFoodItem().getCalPerServing();
			totalCarbs += item.getFoodItem().getCalsCarbPerServing();
			totalProt += item.getFoodItem().getCalsProtPerServing();
			totalFat += item.getFoodItem().getCalsFatPerServing();
		}
		
		//Removes one serving of the MealItem at the specified index of the items array and updates all related fields
		public boolean removeItem(int index){
			MealItem item = items[index];
			if(item.getNumServings() == 0) return false;
			item.setNumServings(item.getNumServings() - 1);
			if(item.getNumServings() == 0) numItems -= 1;
			totalServings -= 1;
			switch(item.getMeal()){
				case BREAKFAST:
					numBreakServ -= 1;
					break;
				case LUNCH:
					numLunchServ -= 1;
					break;
				case DINNER:
					numDinnerServ -= 1;
					break;
			}
			totalCals -= item.getFoodItem().getCalPerServing();
			totalCarbs -= item.getFoodItem().getCalsCarbPerServing();
			totalProt -= item.getFoodItem().getCalsProtPerServing();
			totalFat -= item.getFoodItem().getCalsFatPerServing();
			return true;
		}
		
		//Calculates the fitness value for this MealConfig, consisting of a series of checks for distance from goal values
		public double getFitness(){
			double fitness = 0.0;
			//Create a baseline fitness value equal to the sum of the squared distances of each Config nutrient from the goal range;
			//  all further penalties (except the Locked items penalty) will quickly scale from this initial value
			if(checkCals){
				if(totalCals < con.getMinCals()) fitness += Math.pow(totalCals - con.getMinCals(), 2);
				else if(totalCals > con.getMaxCals()) fitness += Math.pow(totalCals - con.getMaxCals(), 2);
			}
			if(checkCarbs){
				if(totalCarbs < minCalsCarb) fitness += Math.pow(totalCarbs - minCalsCarb, 2);
				else if(totalCarbs > maxCalsCarb) fitness += Math.pow(totalCarbs - maxCalsCarb, 2);
			}
			if(checkProt){
				if(totalProt < minCalsProt) fitness += Math.pow(totalProt - minCalsProt, 2);
				else if(totalProt > maxCalsProt) fitness += Math.pow(totalProt - maxCalsProt, 2);
			}
			if(checkFat){
				if(totalFat < minCalsFat) fitness += Math.pow(totalFat - minCalsFat, 2);
				else if(totalFat > maxCalsFat) fitness += Math.pow(totalFat - maxCalsFat, 2);
			}
			
			//Add a penalty multiplier for item diversity - penalize more than 5 servings of any particular item
			for(int i = 0; i < items.length; ++i){
				if(items[i].getNumServings() > 5){
					for(int j = 0; j < items[i].getNumServings() - 5; ++j) fitness = (fitness + 0.1) * 1.05;
				}
			}			
			
			//Add a penalty multiplier for food diversity - penalize anything outside the range of 6-10 unique food items
			int diversity = Math.abs(numItems - 8);
			if(diversity > 2){
				for(int i = 0; i < diversity; ++i) fitness = (fitness + 0.1) * 1.05;
			}
			
			//Add a penalty multiplier for meal diversity - goal is for each meal to be within 2 servings of 1/3rd of the total servings
			int goalServs = totalServings / 3;
			int breakDiff = Math.abs(goalServs - numBreakServ);
			if(breakDiff > 2){
				for(int i = 0; i < breakDiff; ++i) fitness = (fitness + 0.1) * 1.05;
			}
			int lunchDiff = Math.abs(goalServs - numLunchServ);
			if(lunchDiff > 2){
				for(int i = 0; i < lunchDiff; ++i) fitness = (fitness + 0.1) * 1.05;
			}
			int dinnerDiff = Math.abs(goalServs - numDinnerServ);
			if(dinnerDiff > 2){
				for(int i = 0; i < dinnerDiff; ++i) fitness = (fitness + 0.1) * 1.05;
			}
			
			//Add a tremendously large penalty for any Locked item that has 0 total servings
			if(!noLocked){
				for(int i = 0; i < items.length; ++i){
					if(items[i].isLocked() && items[i].getNumServings() == 0) fitness += (Double.MAX_VALUE / 30);
				}
			}
			
			return fitness;
		}
		
	}//End Class MealConfig
	
}
