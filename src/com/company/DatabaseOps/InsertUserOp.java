package com.company.DatabaseOps;

import Models.Constraints;
import Models.UserProfile;
import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.*;

public class InsertUserOp extends DatabaseOp {
    private UserProfile userProfile;
    private Constraints constraints;
    JSONObject returnObject;
    public InsertUserOp(JSONObject jObject){
        super(jObject);
    }
    @Override
    public JSONObject performOp() throws SQLException {
        getItems();
        toDatabase();
        createSuccessString();
        return this.returnObject;

    }

    private void createSuccessString() {
        this.returnObject = new JSONObject();
        //TODO WILL - PUT SOMEMESSAGE HERE
        this.returnObject.put("response", "New User SuccessfullyCreated");
    }

    private void toDatabase() {
        //TODO YASH - this.userProfile and this.constraints should be put in a new entry on database
        Connection con = null;
        Statement stmt = null;
        try{
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

            //executing insert query for userprofile
            stmt = con.createStatement();
            String query = "INSERT INTO userprofile " +
                            "VALUES ('" + this.userProfile.getUsername() + "' , '"+ this.userProfile.getName() + "' , '" + this.userProfile.getWeight() + "' , " +
                            " '" + this.userProfile.getHeight() +"' , '" + this.userProfile.getAge() +"');";

            stmt.executeUpdate(query);
            System.out.println("User info inserted");

            //executing insert query for user constraints
            stmt = con.createStatement();
            query = "INSERT INTO constraints " +
                     "VALUES ('" + this.userProfile.getUsername() + "' , '" + this.constraints.getMinCals() +"' , '" + this.constraints.getMaxCals() + "'  , '" + this.constraints.getMinCarbs() + "' , '" + this.constraints.getMaxCarbs() + "' , '" + this.constraints.getMinProt() + "' , '" + this.constraints.getMaxProt() +"' , '" + this.constraints.getMinFat() +"' , '" + this.constraints.getMaxFat() +"');";
            stmt.executeUpdate(query);
            System.out.println("User constraints inserted");

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally {
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
