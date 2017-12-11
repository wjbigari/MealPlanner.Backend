package com.company.DatabaseOps;

import Models.FoodItem;
import Models.MealItem;
import Models.MealPlannerRec;
import org.json.JSONObject;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class InsertMealHistoryOp extends DatabaseOp {
    private String username;
    private MealPlannerRec mealPlan;
    private Calendar date;
    private int mealid;
    public InsertMealHistoryOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
        this.mealPlan = new MealPlannerRec(new JSONObject(jrequest.getString("mealPlan")));
        if(jrequest.has("month")){
            date = Calendar.getInstance();
            date.set(jrequest.getInt("year"),jrequest.getInt("month")-1,jrequest.getInt("day"));
        }else{
            date= Calendar.getInstance();
        }
    }

    @Override
    public JSONObject performOp() throws SQLException {

        try {
            storeMealPlan();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        JSONObject returnObject =new JSONObject();
        returnObject.put("response", "successfully added meal to user's history!");
        return returnObject;
    }

    private void storeMealPlan() throws SQLException, ClassNotFoundException {
        Connection con = null;
        Statement stmt = null;
        Class.forName("com.mysql.jdbc.Driver");
        System.out.println(date.get(Calendar.YEAR)+" : " + date.get(Calendar.MONTH)+ ": "+ date.get(Calendar.DAY_OF_MONTH));
        //Open a connection
        ResultSet rs;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO mealhistory(username, date) VALUES( ?,?);", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,username); // the first value is replaced by uniqueID as Long
            ps.setDate(2, new java.sql.Date(date.getTimeInMillis()));  // the third value is replaced by the timestamp of the calendar
            System.out.println(ps.toString());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            rs.next();
            mealid = rs.getInt(1);
            ArrayList<MealItem> breakfast = mealPlan.getBreakfastItems();
            ArrayList<MealItem> lunch= mealPlan.getLunchItems();
            ArrayList<MealItem> dinner = mealPlan.getDinnerItems();
            insertMealtoDatabase(breakfast,DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root"));
            insertMealtoDatabase(lunch, DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root"));
            insertMealtoDatabase(dinner, DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root"));
            con.close();
        }


    }

    private void insertMealtoDatabase(ArrayList<MealItem> meal, Connection con) throws SQLException {
        for(MealItem mi:meal){
            try(PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO mealhistoryitem(mealid, foodid, mealtime,amount) VALUES(?,?,?,?)")){
                preparedStatement.setInt(1,mealid);
                preparedStatement.setInt(2, mi.getFoodItem().getFoodId());
                preparedStatement.setString(3,mi.getMeal().name());
                preparedStatement.setInt(4,mi.getNumServings());
                preparedStatement.executeUpdate();
            }
        }
    }
}
