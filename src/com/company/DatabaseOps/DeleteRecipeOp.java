package com.company.DatabaseOps;

import org.json.JSONObject;

import java.sql.SQLException;
import java.sql.*;

public class DeleteRecipeOp extends DatabaseOp{
    private int recipeId;

    public DeleteRecipeOp(JSONObject jObject){
        super(jObject);
        this.recipeId = jObject.getInt("recipeId");
    }

    @Override
    public JSONObject performOp() throws SQLException {
        performDatabaseOp();
        return null;
    }
    private void performDatabaseOp(){
        Connection con = null;
        Statement stmt = null;
        try{
            //Registering JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //Open a connection
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

            //deleting userrecipe and the corresponding recipeitems
            stmt = con.createStatement();
            String query = " DELETE FROM userrecipe WHERE recipeid = " + this.recipeId + ";";
            stmt.executeUpdate(query);

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
}
