package com.company.DatabaseOps;

import Models.FoodItem;
import Models.MealItem;
import Models.MealPlannerRec;
import org.json.JSONObject;

import java.sql.*;
import java.util.Calendar;
import static Models.MealItem.Meal.BREAKFAST;
import static Models.MealItem.Meal.DINNER;
import static Models.MealItem.Meal.LUNCH;

public class GetMealHistoryOp extends DatabaseOp {
    private String username;
    private int month;
    private int year;
    private JSONObject responseObject;
    private JSONObject mealPlanList;
    public GetMealHistoryOp(JSONObject jrequest) {
        super(jrequest);
        username = jrequest.getString("username");
        if(jrequest.has("month")){
            month = jrequest.getInt("month");
            year = jrequest.getInt("year");
        }else{
            Calendar cal = Calendar.getInstance();
            month = cal.get(Calendar.MONTH) + 1;
            year = cal.get(Calendar.YEAR);
        }
    }

    @Override
    public JSONObject performOp() throws SQLException {
        responseObject = new JSONObject();
        try {
            getMealPlannerRecs();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return responseObject;
    }

    private void getMealPlannerRecs() throws SQLException, ClassNotFoundException {
        mealPlanList = new JSONObject();

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

        ResultSet rs;
        MealItem tempMealItem;
        FoodItem tempItem;
        MealPlannerRec mealPlannerTemp;
        try (PreparedStatement ps = con.prepareStatement("SELECT mealid, DAY(date) FROM mealhistory WHERE username = ? AND MONTH(date) = ? AND YEAR(date) =?;")) {
            ps.setString(1, username);
            ps.setInt(2, month);
            ps.setInt(3, year);
            System.out.println(ps.toString());
            rs = ps.executeQuery();

            while (rs.next()) {
                ResultSet mealItemSet;
                mealPlannerTemp = new MealPlannerRec();
                Connection mealItemCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
                try (PreparedStatement ps0 = mealItemCon.prepareStatement("SELECT * FROM mealhistoryitem WHERE mealid = ?;")) {
                    ps0.setInt(1, rs.getInt("mealid"));
                    mealItemSet = ps0.executeQuery();

                    while (mealItemSet.next()) {
                        ResultSet ingredientSet;
                        Connection foodItemCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
                        try (PreparedStatement ps1 = foodItemCon.prepareStatement("SELECT * FROM ingredients WHERE foodid=?;")) {
                            ps1.setInt(1, mealItemSet.getInt("foodid"));
                            ingredientSet = ps1.executeQuery();

                            ingredientSet.next();
                            int foodid = ingredientSet.getInt("foodid");
                            String itemname = ingredientSet.getString("contents");
                            double cals = ingredientSet.getDouble("cals");
                            double fats = ingredientSet.getDouble("fats");
                            double prots = ingredientSet.getDouble("prots");
                            double carbs = ingredientSet.getDouble("carbs");
                            int minservamt = (int) Double.parseDouble(ingredientSet.getString("minservamt"));
                            String servingname = ingredientSet.getString("servingName");
                            tempItem = new FoodItem(itemname, foodid, minservamt, servingname, (int) cals, carbs, prots, fats);

                            MealItem.Meal meal;
                            switch (mealItemSet.getString("mealtime")) {
                                case "DINNER":
                                    meal = DINNER;
                                    break;
                                case "LUNCH":
                                    meal = LUNCH;
                                    break;
                                default:
                                    meal = BREAKFAST;
                                    break;
                            }
                            tempMealItem = new MealItem(tempItem, true, (int) mealItemSet.getFloat("amount"), meal);
                            mealPlannerTemp.addItemToRec(tempMealItem);
                        }
                    }
                    mealPlanList.put("" + rs.getInt(2), mealPlannerTemp.toJson().toString());
                }
            }
            responseObject.put("mealList", mealPlanList.toString());
        }
    }
}
