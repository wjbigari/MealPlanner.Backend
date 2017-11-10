package com.company.DatabaseOps;

import Models.Constraints;
import Models.UserProfile;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.*;

public class UpdateUserOp extends DatabaseOp{
    private UserProfile userProfile;
    private Constraints constraints;
    private JSONObject returnObject;

    public UpdateUserOp (JSONObject jObject){super(jObject);}

    @Override
    public JSONObject performOp() throws SQLException {
        getItems();
        toDatabase();
        createSuccessString();
        return this.returnObject;

    }

    private void createSuccessString() {
        this.returnObject = new JSONObject();
        //TODO WILL - PUT SOME MESSAGE HERE
        this.returnObject.put("response", "User Info Updated");
    }

    private void toDatabase() {
        //TODO YASH - update the entry that corresponds to the username with this.userProfile and this.constraints
        Connection con = null;
        Statement stmt = null;
        try {
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner", "root", "root");

            //executing update query for userprofile
            String query = "UPDATE userprofile " +
                    "SET name = '" + this.userProfile.getName() + "' , " +
                    "weight = '" + this.userProfile.getWeight() + "' ," +
                    "height = '" + this.userProfile.getHeight() + "' ," +
                    "age = '" + this.userProfile.getAge() + "'  " +
                    "gender = '" + this.userProfile.getGen() +"' " +
                    "WHERE username = '" + this.userProfile.getUsername() + "';";

            stmt.executeUpdate(query);
            System.out.println("User record successfully updated");

            //executing update query for constraints
            query = "UPDATE constraints " +
                    "SET mincals = '" + this.constraints.getMinCals() + "' , maxcals = '" + this.constraints.getMaxCals() + "' , " +
                    "mincarbs = '" + this.constraints.getMinCarbs() + "' , maxcarbs = '" + this.constraints.getMaxCarbs() + "' , " +
                    "minprot = '" + this.constraints.getMinProt() + "' , maxprot = '" + this.constraints.getMaxProt() + "' , " +
                    "minfat = '" + this.constraints.getMinFat() + "' , maxfat = '" + this.constraints.getMaxFat() + "' " +
                    "WHERE username = '" + this.userProfile.getUsername() + "' ;";

            stmt.executeUpdate(query);
            System.out.println("Constraints updated successfully");

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    con.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (con != null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        } //end finally
    }

    private void getItems(){
        this.userProfile = new UserProfile(new JSONObject(this.jobject.getString("userProfile")));
        //this.constraints = new Constraints(new JSONObject(this.jobject.getString("constraints")));
        this.constraints = this.userProfile.getConstraints();
    }

}
