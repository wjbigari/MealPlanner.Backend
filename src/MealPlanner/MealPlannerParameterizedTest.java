package MealPlanner;

import static org.junit.Assert.*;
import static org.junit.runners.Parameterized.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import Models.Constraints;
import Models.FoodItem;
import Models.MealItem;
import Models.MealPlannerRec;
import Models.MealPlannerRequest;
import Models.MealItem.Meal;

/**
 * MealPlannerParameterizedTest
 * @author Nick Bialk
 *
 * This is a full-factorial test suite to test the performance of the meal-planning algorithm, using the JUnit4 Parameterized
 *   testing framework to dynamically generate test cases out of various combinations of input parameters.
 * The suite tests each of the 8 Constraints fields -- min/max cals, min/max carbs, min/max prot, min/max fat -- at 4 different levels:
 * 	 explicitly 0, a low value, a high value, and leaving the field blank.  Every valid and meaningful combination
 *   of values for the 8 fields is tested for accuracy (goal: within 5% of specified range) and time efficiency (goal: under 3 seconds)
 */
@RunWith(Parameterized.class)
public class MealPlannerParameterizedTest {
	@Parameter(0)
	public int minCal;
	@Parameter(1)
	public int maxCal;
	@Parameter(2)
	public int minCarb;
	@Parameter(3)
	public int maxCarb;
	@Parameter(4)
	public int minProt;
	@Parameter(5)
	public int maxProt;
	@Parameter(6)
	public int minFat;
	@Parameter(7)
	public int maxFat;
	
	@Parameters
	public static Collection<Object[]> data(){
		//Levels for each of the four main factors -- note -1 represents an explicit '0' in the field, whereas 0 represents a blank field
		int[] calLevels = {-1, 0, 1000, 5000};
		int[] carbLevels = {-1, 0, 75, 375};
		int[] protLevels = {-1, 0, 75, 375};
		int[] fatLevels = {-1, 0, 45, 225};
		//Populate the set of design points by using every combination of valid nutrient ranges
		// e.g. [1000-3000 cals, 0-75g carbs, 75-75g prot, 225-225g fat] would represent one design point
		// With the levels as listed above this will generate 10000 distinct design points
		ArrayList<ArrayList<Integer>> designPoints = new ArrayList<>();
		for(int maxCalIndex = 0; maxCalIndex < calLevels.length; maxCalIndex++){
			for(int minCalIndex = 0; minCalIndex <= maxCalIndex; minCalIndex++){
				for(int maxCarbIndex = 0; maxCarbIndex < carbLevels.length; maxCarbIndex++){
					for(int minCarbIndex = 0; minCarbIndex <= maxCarbIndex; minCarbIndex++){
						for(int maxProtIndex = 0; maxProtIndex < protLevels.length; maxProtIndex++){
							for(int minProtIndex = 0; minProtIndex <= maxProtIndex; minProtIndex++){
								for(int maxFatIndex = 0; maxFatIndex < fatLevels.length; maxFatIndex++){
									for(int minFatIndex = 0; minFatIndex <= maxFatIndex; minFatIndex++){
										ArrayList<Integer> point = new ArrayList<>();
										point.add(calLevels[minCalIndex]);
										point.add(calLevels[maxCalIndex]);
										point.add(carbLevels[minCarbIndex]);
										point.add(carbLevels[maxCarbIndex]);
										point.add(protLevels[minProtIndex]);
										point.add(protLevels[maxProtIndex]);
										point.add(fatLevels[minFatIndex]);
										point.add(fatLevels[maxFatIndex]);
										designPoints.add(point);
									}
								}
							}
						}
					}
				}
			}
		}
		//Populate JUnit's Parameterized.class data as a 2-dimensional Object array using the collected design points
		Object[][] data = new Object[designPoints.size()][8];
		for(int i = 0; i < data.length; i++){
			for(int j = 0; j < 8; j++){
				data[i][j] = designPoints.get(i).get(j);
			}
		}
		return Arrays.asList(data);
	}
	
	//Test MealItems
	private static FoodItem carbTest = new FoodItem("carb", 1, 1, "serving(s)", 60, 15, 0, 0);
	private static FoodItem protTest = new FoodItem("prot", 2, 1, "serving(s)", 60, 0, 15, 0);
	private static FoodItem fatTest = new FoodItem("fat", 3, 1, "serving(s)", 63, 0, 0, 7);
	private static FoodItem cpTest = new FoodItem("carb-prot", 4, 1, "serving(s)", 64, 8, 8, 0);
	private static FoodItem cfTest = new FoodItem("carb-fat", 5, 1, "serving(s)", 68, 8, 0, 4);
	private static FoodItem pfTest = new FoodItem("prot-fat", 6, 1, "serving(s)", 68, 0, 8, 4);
	
	//Global fields for design point testing
	private ArrayList<MealItem> testMeal;
	private Constraints constraints;
	private boolean ignoreCals;
	private boolean ignoreCarbs;
	private boolean ignoreProt;
	private boolean ignoreFat;
	
	@BeforeClass
	public static void setUp(){
		MealPlanner.setDebugMode(true);
	}
	
	@Test
	public void testDesignPoint() throws InterruptedException{
		setUpDesignPoint(minCal, maxCal, minCarb, maxCarb, minProt, maxProt, minFat, maxFat);
		MealPlannerRequest request = new MealPlannerRequest(testMeal, constraints);
		long startTime = System.currentTimeMillis();
		MealPlannerRec response = MealPlanner.createMealPlan(request);
		long endTime = System.currentTimeMillis();
		
		if(response == null) return; //MealPlanner is set to return a null MealPlannerRec if it is in debug mode and the parameters are invalid
		System.out.print("Best dish-- Cals: " + response.getTotalCals() + " / Carbs: " + response.getTotalCarbs() + " / Prot: " + response.getTotalProt() + " / Fat: " + response.getTotalFat() + "\n");
		
		//Verify that the design point completed in a timely manner
		assertTrue("Test completed in " + (endTime - startTime) + " milliseconds, expected less than 3 seconds",
				endTime - startTime < 3000);
		//Verify that the total Calories is within an acceptable error bound of the goal range (if applicable)
		if(!ignoreCals){
			if(request.getConstraints().getMinCals() >= 0){
				double minCalsGoal = Math.max(request.getConstraints().getMinCals(), 0) * 0.95 - 25;
				assertTrue("Total Cals " + response.getTotalCals() + ", expected greater than " + minCalsGoal,
						response.getTotalCals() >= minCalsGoal);
			}		
			if(request.getConstraints().getMaxCals() >= 0){
				double maxCalsGoal = Math.max(request.getConstraints().getMaxCals(), 0) * 1.05 + 25;
				assertTrue("Total Cals " + response.getTotalCals() + ", expected less than " + maxCalsGoal,
						response.getTotalCals() <= maxCalsGoal);
			}
		}
		//Verify that the total Carbs is within an acceptable error bound of the goal range (if applicable)
		if(!ignoreCarbs){
			if(request.getConstraints().getMinCarbs() >= 0){
				double minCarbsGoal = Math.max(request.getConstraints().getMinCarbs(), 0) * 0.95 - 10;
				assertTrue("Total Carbs " + response.getTotalCarbs() + ", expected greater than " + minCarbsGoal,
						response.getTotalCarbs() >= minCarbsGoal);
			}		
			if(request.getConstraints().getMaxCarbs() >= 0){
				double maxCarbsGoal = Math.max(request.getConstraints().getMaxCarbs(), 0) * 1.05 + 10;
				assertTrue("Total Carbs " + response.getTotalCarbs() + ", expected less than " + maxCarbsGoal,
						response.getTotalCarbs() <= maxCarbsGoal);
			}
		}
		//Verify that the total Prot is within an acceptable error bound of the goal range (if applicable)
		if(!ignoreProt){
			if(request.getConstraints().getMinProt() >= 0){
				double minProtGoal = Math.max(request.getConstraints().getMinProt(), 0) * 0.95 - 10;
				assertTrue("Total Prot " + response.getTotalProt() + ", expected greater than " + minProtGoal,
						response.getTotalProt() >= minProtGoal);
			}		
			if(request.getConstraints().getMaxProt() >= 0){
				double maxProtGoal = Math.max(request.getConstraints().getMaxProt(), 0) * 1.05 + 10;
				assertTrue("Total Prot " + response.getTotalProt() + ", expected less than " + maxProtGoal,
						response.getTotalProt() <= maxProtGoal);
			}
		}
		//Verify that the total Fat is within an acceptable error bound of the goal range (if applicable)
		if(!ignoreFat){
			if(request.getConstraints().getMinFat() >= 0){
				double minFatGoal = Math.max(request.getConstraints().getMinFat(), 0) * 0.95 - 5;
				assertTrue("Total Fat " + response.getTotalFat() + ", expected greater than " + minFatGoal,
						response.getTotalCals() >= minFatGoal);
			}		
			if(request.getConstraints().getMaxFat() >= 0){
				double maxFatGoal = Math.max(request.getConstraints().getMaxFat(), 0) * 1.05 + 5;
				assertTrue("Total Fat " + response.getTotalFat() + ", expected less than " + maxFatGoal,
						response.getTotalFat() <= maxFatGoal);
			}
		}
		System.out.print("Design point successfully verified!\n\n");
	}
	
	//Helper method - sets up the Constraints with the specified parameters and rebuilds the test Meal Pools
	private void setUpDesignPoint(int loCal, int hiCal, int loCarb, int hiCarb, int loProt, int hiProt, int loFat, int hiFat){
		constraints = new Constraints(minCal, maxCal, minCarb, maxCarb, minProt, maxProt, minFat, maxFat);
		if(minCal == 0 && maxCal == 0) ignoreCals = true;
		else ignoreCals = false;
		if(minCarb == 0 && maxCarb == 0) ignoreCarbs = true;
		else ignoreCarbs = false;
		if(minProt == 0 && maxProt == 0) ignoreProt = true;
		else ignoreProt = false;
		if(minFat == 0 && maxFat == 0) ignoreFat = true;
		else ignoreFat = false;
		testMeal = new ArrayList<>();
		testMeal.add(new MealItem(carbTest, Meal.BREAKFAST));
		testMeal.add(new MealItem(protTest, Meal.LUNCH));
		testMeal.add(new MealItem(fatTest, Meal.DINNER));
		testMeal.add(new MealItem(cpTest, Meal.DINNER));
		testMeal.add(new MealItem(cfTest, Meal.LUNCH));
		testMeal.add(new MealItem(pfTest, Meal.BREAKFAST));
	}
	
	@AfterClass
	public static void tearDown(){
		MealPlanner.setDebugMode(false);
	}
}
