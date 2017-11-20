package com.company.DatabaseOps;

import Models.Constraints;
import Models.UserProfile;
import org.json.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.sql.*;
import java.util.Base64;
import java.util.Random;

public class InsertUserOp extends DatabaseOp {
    private UserProfile userProfile;
    private Constraints constraints;
    private String passString;
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
            byte[] salt = new byte[16];
            Random random = new Random();
            random.nextBytes(salt);
            MessageDigest md = null;
            //get a message digest
            try{
                md = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException x){
                System.out.println(x.getStackTrace());
            }
            //hash the password
            byte [] hash = md.digest(passString.getBytes());

            //executing insert query for userprofile
            stmt = con.createStatement();
            String query = "INSERT INTO userprofile (username, name, weight, height, age, gender, password) " +
                            "VALUES ('" + this.userProfile.getUsername() + "' , '" + this.userProfile.getName() +"' , " + this.userProfile.getWeight() + " , " +
                            " " + this.userProfile.getHeight() +" , " + this.userProfile.getAge() +" , '" + this.userProfile.getGen() + "' , '" + byteArrayToString(hash)+ "');";

            stmt.executeUpdate(query);
            System.out.println("User info inserted");

            //executing insert query for user constraints
            stmt = con.createStatement();
            query = "INSERT INTO constraints " +
                     "VALUES ('" + this.userProfile.getUsername() + "' , " + this.constraints.getMinCals() +" , " + this.constraints.getMaxCals() + "  , " + this.constraints.getMinCarbs() + " , " + this.constraints.getMaxCarbs() + " , " + this.constraints.getMinProt() + " , " + this.constraints.getMaxProt() +" , " + this.constraints.getMinFat() + " , " + this.constraints.getMaxFat() +");";
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
        this.passString = jobject.getString("password");
    }
    private String byteArrayToString(byte[] in) {
        char out[] = new char[in.length * 2];
        for (int i = 0; i < in.length; i++) {
            out[i * 2] = "0123456789ABCDEF".charAt((in[i] >> 4) & 15);
            out[i * 2 + 1] = "0123456789ABCDEF".charAt(in[i] & 15);
        }
        return new String(out);
    }
}
