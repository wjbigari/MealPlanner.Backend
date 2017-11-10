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
	private static int loCal = 1000;
	private static int hiCal = 3000;
	private static int loCarb = 75;
	private static int hiCarb = 225;
	private static int loProt = 75;
	private static int hiProt = 225;
	private static int loFat = 45;
	private static int hiFat = 135;
	//Test MealItems
	private static FoodItem carbTest = new FoodItem("carb", 1, 1, "serving(s)", 20, 5, 0, 0);
	private static FoodItem protTest = new FoodItem("prot", 1, 1, "serving(s)", 20, 0, 5, 0);
	private static FoodItem fatTest = new FoodItem("fat", 1, 1, "serving(s)", 18, 0, 0, 2);
	private static MealItem cTestMeal = new MealItem(carbTest, Meal.BREAKFAST);
	private static MealItem pTestMeal = new MealItem(protTest, Meal.BREAKFAST);
	private static MealItem fTestMeal = new MealItem(fatTest, Meal.BREAKFAST);
	//Test MPrequest parameters
	private ArrayList<MealItem> testMeal;
	private Constraints constraints;
	private MealPlannerRequest request;
	private MealPlannerRec response;

	@Override
	public void setUp() throws Exception{
		testMeal = new ArrayList<>();
		testMeal.add(cTestMeal);
		testMeal.add(pTestMeal);
		testMeal.add(fTestMeal);
		constraints = new Constraints();
		cals = new ArrayList<>();
		cals.add(loCal);
		cals.add(hiCal);
		carbs = new ArrayList<>();
		carbs.add(loCarb);
		carbs.add(hiCarb);
		prot = new ArrayList<>();
		prot.add(loProt);
		prot.add(hiProt);
		fat = new ArrayList<>();
		fat.add(loFat);
		fat.add(hiFat);
		super.setUp();
	}
	
	public void testLo() throws InterruptedException{
		this.setUpDesignPoint(loCal, loCal, loCarb, loCarb, loProt, loProt, loFat, loFat);
		this.verifyDesignPoint();		
	}
	
	public void testHi() throws InterruptedException{
		this.setUpDesignPoint(hiCal, hiCal, hiCarb, hiCarb, hiProt, hiProt, hiFat, hiFat);
		this.verifyDesignPoint();		
	}
	
	public void testRange() throws InterruptedException{
		this.setUpDesignPoint(loCal, hiCal, loCarb, hiCarb, loProt, hiProt, loFat, hiFat);
		this.verifyDesignPoint();
	}
	
	public void testCals() throws InterruptedException{
		for(int calLevel : cals){
			this.setUpDesignPoint(calLevel, calLevel, (calLevel/12), (calLevel/12), (calLevel/12), (calLevel/12), (calLevel/27), (calLevel/27));
			this.verifyDesignPoint();
		}
	}
	
	public void testCalsOpen() throws InterruptedException{
		for(int calLevel : cals){
			this.setUpDesignPoint(calLevel, calLevel, 0, 0, 0, 0, 0, 0);
			this.verifyDesignPoint();
		}
	}
	
	public void testCalsRange() throws InterruptedException{
		this.setUpDesignPoint(loCal, hiCal, (loCal/12), (hiCal/12), (loCal/12), (hiCal/12), (loCal/27), (hiCal/27));
		this.verifyDesignPoint();
	}
	
	public void testCarbs() throws InterruptedException{
		for(int carbLevel : carbs){
			this.setUpDesignPoint((carbLevel * 4), (carbLevel * 4), carbLevel, carbLevel, -1, -1, -1, -1);
			this.verifyDesignPoint();

		}		
	}
	
	public void testCarbsOpen() throws InterruptedException{
		for(int carbLevel : carbs){
			this.setUpDesignPoint(0, 0, carbLevel, carbLevel, 0, 0, 0, 0);
			this.verifyDesignPoint();
		}
	}
	
	public void testCarbRange() throws InterruptedException{
		this.setUpDesignPoint((loCarb * 4), (hiCarb * 4), loCarb, hiCarb, -1, -1, -1, -1);
		this.verifyDesignPoint();
	}
	
	public void testProt() throws InterruptedException{
		for(int protLevel : prot){
			this.setUpDesignPoint((protLevel * 4), (protLevel * 4), -1, -1, protLevel, protLevel, -1, -1);
			this.verifyDesignPoint();
		}		
	}
	
	public void testProtOpen() throws InterruptedException{
		for(int protLevel : prot){
			this.setUpDesignPoint(0, 0, 0, 0, protLevel, protLevel, 0, 0);
			this.verifyDesignPoint();
		}
	}
	
	public void testProtRange() throws InterruptedException{
		this.setUpDesignPoint((loProt * 4), (hiProt * 4), -1, -1, loProt, hiProt, -1, -1);
		this.verifyDesignPoint();
	}
	
	public void testFat() throws InterruptedException{
		for(int fatLevel : fat){
			this.setUpDesignPoint((fatLevel * 9), (fatLevel * 9), -1, -1, -1, -1, fatLevel, fatLevel);
			this.verifyDesignPoint();
		}		
	}
	
	public void testFatOpen() throws InterruptedException{
		for(int fatLevel : fat){
			this.setUpDesignPoint(0, 0, 0, 0, 0, 0, fatLevel, fatLevel);
			this.verifyDesignPoint();
		}
	}
	
	public void testFatRange() throws InterruptedException{
		this.setUpDesignPoint((loFat * 9), (hiFat * 9), -1, -1, -1, -1, loFat, hiFat);
		this.verifyDesignPoint();
	}
	
	//Helper method - sets up the MealPlannerRequest with the proper initial values for this design point
	private void setUpDesignPoint(int minCal, int maxCal, int minCarb, int maxCarb, int minProt, int maxProt, int minFat, int maxFat){
		constraints.setMinCals(minCal);
		constraints.setMaxCals(maxCal);
		constraints.setMinCarbs(minCarb);
		constraints.setMaxCarbs(maxCarb);
		constraints.setMinProt(minProt);
		constraints.setMaxProt(maxProt);
		constraints.setMinFat(minFat);
		constraints.setMaxFat(maxFat);
		for(MealItem item : testMeal){
			item.setNumServings(0);
		}
		request = new MealPlannerRequest(testMeal, constraints);
	}
	
	//Verify that the request was processed in less than 3 seconds, and within 5% margin of error on each applicable constraint
	private void verifyDesignPoint() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		response = MealPlanner.createMealPlan(request);
		long endTime = System.currentTimeMillis();
		assertTrue("Test completed in " + (endTime - startTime) + " milliseconds, expected less than 3 seconds",
				endTime - startTime < 3000);
		if(request.getConstraints().getMinCals() > 0){
			double minCalsGoal = request.getConstraints().getMinCals() * 0.95 - 10;
			assertTrue("Total Cals " + response.getTotalCals() + ", expected greater than " + minCalsGoal,
					response.getTotalCals() >= minCalsGoal);
		}		
		if(request.getConstraints().getMaxCals() != 0){
			double maxCalsGoal = Math.abs(request.getConstraints().getMaxCals()) * 1.05 + 10;
			assertTrue("Total Cals " + response.getTotalCals() + ", expected less than " + maxCalsGoal,
					response.getTotalCals() <= maxCalsGoal);
		}
		if(request.getConstraints().getMinCarbs() > 0){
			double minCarbsGoal = request.getConstraints().getMinCarbs() * 0.95 - 5;
			assertTrue("Total Carbs " + response.getTotalCarbs() + ", expected greater than " + minCarbsGoal,
					response.getTotalCarbs() >= minCarbsGoal);
		}
		if(request.getConstraints().getMaxCarbs() != 0){
			double maxCarbsGoal = Math.abs(request.getConstraints().getMaxCarbs()) * 1.05 + 5;
			assertTrue("Total Carbs " + response.getTotalCarbs() + ", expected less than " + maxCarbsGoal,
					response.getTotalCarbs() <= maxCarbsGoal);
		}
		if(request.getConstraints().getMinProt() > 0){
			double minProtGoal = request.getConstraints().getMinProt() * 0.95 - 5;
			assertTrue("Total Prot " + response.getTotalProt() + ", expected greater than " + minProtGoal,
					response.getTotalProt() >= minProtGoal);
		}
		if(request.getConstraints().getMaxProt() != 0){
			double maxProtGoal = Math.abs(request.getConstraints().getMaxProt()) * 1.05 + 5;
			assertTrue("Total Prot " + response.getTotalProt() + ", expected less than " + maxProtGoal,
					response.getTotalProt() <= maxProtGoal);
		}
		if(request.getConstraints().getMinFat() > 0){
			double minFatGoal = request.getConstraints().getMinFat() * 0.95 - 5;
			assertTrue("Total Fat " + response.getTotalFat() + ", expected greater than " + minFatGoal,
					response.getTotalFat() >= minFatGoal);
		}
		if(request.getConstraints().getMaxFat() != 0){
			double maxFatGoal = Math.abs(request.getConstraints().getMaxFat()) * 1.05 + 5;
			assertTrue("Total Fat " + response.getTotalFat() + ", expected less than " + maxFatGoal,
					response.getTotalFat() <= maxFatGoal);
		}
		System.out.print("Completed Design Point successfully in " + (endTime - startTime) + " milliseconds - \n"
				+ constraints.toString() + "\n\n");
	}
}
