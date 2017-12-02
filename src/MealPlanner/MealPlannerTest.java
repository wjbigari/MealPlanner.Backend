package MealPlanner;

import Models.*;
import Models.MealItem.Meal;
import junit.framework.TestCase;

import java.util.ArrayList;


public class MealPlannerTest extends TestCase{
	//Factors
	private ArrayList<Integer> cals;
	private ArrayList<Integer> carbs;
	private ArrayList<Integer> prot;
	private ArrayList<Integer> fat;
	//Levels
	private static final int ZERO = -1;
	private static final int BLANK = 0;
	private static final int loCal = 1000;
	private static final int hiCal = 3000;
	private static final int loCarb = 75;
	private static final int hiCarb = 225;
	private static final int loProt = 75;
	private static final int hiProt = 225;
	private static final int loFat = 45;
	private static final int hiFat = 135;
	//Test MealItems
	private static FoodItem carbTest = new FoodItem("carb", 1, 1, "serving(s)", 60, 15, 0, 0);
	private static FoodItem protTest = new FoodItem("prot", 2, 1, "serving(s)", 60, 0, 15, 0);
	private static FoodItem fatTest = new FoodItem("fat", 3, 1, "serving(s)", 63, 0, 0, 7);
	private static FoodItem cpTest = new FoodItem("carb-prot", 4, 1, "serving(s)", 64, 8, 8, 0);
	private static FoodItem cfTest = new FoodItem("carb-fat", 5, 1, "serving(s)", 68, 8, 0, 4);
	private static FoodItem pfTest = new FoodItem("prot-fat", 6, 1, "serving(s)", 68, 0, 8, 4);
	//Test MPrequest parameters
	private ArrayList<MealItem> testMeal;
	private Constraints constraints;
	private MealPlannerRequest request;
	private boolean ignoreCals;
	private boolean ignoreCarbs;
	private boolean ignoreProt;
	private boolean ignoreFat;

	@Override
	public void setUp() throws Exception{
		MealPlanner.setDebugMode(true);
		cals = new ArrayList<>();
		cals.add(ZERO);
		cals.add(BLANK);
		cals.add(loCal);
		cals.add(hiCal);
		carbs = new ArrayList<>();
		carbs.add(ZERO);
		carbs.add(BLANK);
		carbs.add(loCarb);
		carbs.add(hiCarb);
		prot = new ArrayList<>();
		prot.add(ZERO);
		prot.add(BLANK);
		prot.add(loProt);
		prot.add(hiProt);
		fat = new ArrayList<>();
		fat.add(ZERO);
		fat.add(BLANK);
		fat.add(loFat);
		fat.add(hiFat);
		super.setUp();
	}
	
	public void testAll() throws InterruptedException{
		int testIndex = 1;
		for(int maxCalIndex = 0; maxCalIndex < cals.size(); maxCalIndex++){
			for(int minCalIndex = 0; minCalIndex <= maxCalIndex; minCalIndex++){
				for(int maxCarbIndex = 0; maxCarbIndex < carbs.size(); maxCarbIndex++){
					for(int minCarbIndex = 0; minCarbIndex <= maxCarbIndex; minCarbIndex++){
						for(int maxProtIndex = 0; maxProtIndex < prot.size(); maxProtIndex++){
							for(int minProtIndex = 0; minProtIndex <= maxProtIndex; minProtIndex++){
								for(int maxFatIndex = 0; maxFatIndex < fat.size(); maxFatIndex++){
									for(int minFatIndex = 0; minFatIndex <= maxFatIndex; minFatIndex++){
										System.out.println("Test Point " + testIndex);
										request = this.setUpDesignPoint(cals.get(minCalIndex), cals.get(maxCalIndex), carbs.get(minCarbIndex), carbs.get(maxCarbIndex), prot.get(minProtIndex), prot.get(maxProtIndex), fat.get(minFatIndex), fat.get(maxFatIndex));
										verifyDesignPoint(request);
										testIndex++;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
//	public void testLo() throws InterruptedException{
//		request = this.setUpDesignPoint(loCal, loCal, loCarb, loCarb, loProt, loProt, loFat, loFat);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);		
//	}
//	
//	public void testHi() throws InterruptedException{
//		request = this.setUpDesignPoint(hiCal, hiCal, hiCarb, hiCarb, hiProt, hiProt, hiFat, hiFat);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);		
//	}
//	
//	public void testRange() throws InterruptedException{
//		request = this.setUpDesignPoint(loCal, hiCal, loCarb, hiCarb, loProt, hiProt, loFat, hiFat);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);
//	}
//	
//	public void testCals() throws InterruptedException{
//		for(int calLevel : cals){
//			request = this.setUpDesignPoint(calLevel, calLevel, 0, 0, 0, 0, 0, 0);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//		}
//	}
//	
//	public void testCalsRange() throws InterruptedException{
//		request = this.setUpDesignPoint(loCal, hiCal, 0, 0, 0, 0, 0, 0);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);
//	}
//	
//	public void testCarbs() throws InterruptedException{
//		for(int carbLevel : carbs){
//			request = this.setUpDesignPoint(0, 0, carbLevel, carbLevel, 0, 0, 0, 0);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//
//		}		
//	}
//	
//	public void testCarbsOnly() throws InterruptedException{
//		for(int carbLevel : carbs){
//			request = this.setUpDesignPoint(0, 0, carbLevel, carbLevel, -1, -1, -1, -1);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//
//		}	
//	}
//	
//	public void testCarbRange() throws InterruptedException{
//		request = this.setUpDesignPoint(0, 0, loCarb, hiCarb, 0, 0, 0, 0);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);
//	}
//	
//	public void testProt() throws InterruptedException{
//		for(int protLevel : prot){
//			request = this.setUpDesignPoint(0, 0, 0, 0, protLevel, protLevel, 0, 0);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//		}		
//	}
//	
//	public void testProtOnly() throws InterruptedException{
//		for(int protLevel : prot){
//			request = this.setUpDesignPoint(0, 0, -1, -1, protLevel, protLevel, -1, -1);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//		}	
//	}
//	
//	public void testProtRange() throws InterruptedException{
//		request = this.setUpDesignPoint(0, 0, 0, 0, loProt, hiProt, 0, 0);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);
//	}
//	
//	public void testFat() throws InterruptedException{
//		for(int fatLevel : fat){
//			request = this.setUpDesignPoint(0, 0, 0, 0, 0, 0, fatLevel, fatLevel);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//		}		
//	}
//	
//	public void testFatOnly() throws InterruptedException{
//		for(int fatLevel : fat){
//			request = this.setUpDesignPoint(0, 0, -1, -1, -1, -1, fatLevel, fatLevel);
//			System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//			this.verifyDesignPoint(request);
//		}
//	}
//	
//	public void testFatRange() throws InterruptedException{
//		request = this.setUpDesignPoint(0, 0, 0, 0, 0, 0, loFat, hiFat);
//		System.out.println("Testing design point: " + constraints.getMinCals() + "/" + constraints.getMaxCals() + "/" + constraints.getMinCarbs() + "/" + constraints.getMaxCarbs() + "/" + constraints.getMinProt() + "/" + constraints.getMaxProt() + "/" + constraints.getMinFat() + "/" + constraints.getMaxFat());
//		this.verifyDesignPoint(request);
//	}
//	
	//Helper method - sets up the MealPlannerRequest with the proper initial values for this design point
	private MealPlannerRequest setUpDesignPoint(int minCal, int maxCal, int minCarb, int maxCarb, int minProt, int maxProt, int minFat, int maxFat){
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
		return new MealPlannerRequest(testMeal, constraints);
	}
	
	//Verify that the request was processed in less than 3 seconds, and within 5% margin of error on each applicable constraint
	private void verifyDesignPoint(MealPlannerRequest request) throws InterruptedException{
		//Run the Meal Planner algorithm
		long startTime = System.currentTimeMillis();
		MealPlannerRec response = null;
		response = MealPlanner.createMealPlan(request);
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
}
