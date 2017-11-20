package com.company.DatabaseOps;

import Models.Constraints;
import Models.UserProfile;
import com.company.DatabaseOps.DatabaseOp;
import org.json.JSONObject;
import static Models.UserProfile.gender.FEMALE;
import static Models.UserProfile.gender.MALE;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class LoginOp extends DatabaseOp {
    private String username;
    private String password;
    private JSONObject responseObject;

    public LoginOp(JSONObject jrequest) {
        super(jrequest);
        this.username = jrequest.getString("username");
        this.password = jrequest.getString("password");
    }

    @Override
    public JSONObject performOp() throws SQLException {
        this.responseObject = new JSONObject();
        try {
            login();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.responseObject;
    }

    private void login() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = null;
        Statement stmt = null;
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mealplanner","root","root");

        stmt = con.createStatement();
        String query = "SELECT * FROM userprofile WHERE username = '"+ this.username + "';";

        ResultSet rs = stmt.executeQuery(query);
        boolean login = false;
        if(rs.next()){
            String passwordFromDB = rs.getString("password");
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte [] hash = md.digest(this.password.getBytes());
                System.out.println( passwordFromDB + " : " +  byteArrayToString(hash));
                if(passwordFromDB.equals(byteArrayToString(hash))){
                    System.out.println(username + "logged in.");
                    login = true;
                }else{
                    System.out.println("failed to log in");
                }
            }
            catch (NoSuchAlgorithmException x){
                System.out.println(x.getStackTrace());
            }

        }
        if (login){
            stmt = con.createStatement();
            String userProfile = "SELECT * FROM userprofile WHERE username = '" + this.username + "' ; ";
            ResultSet rs1 = stmt.executeQuery(userProfile);
            rs1.next();
            String name = rs1.getString("name");
            int weight = rs1.getInt("weight");
            int height = rs1.getInt("height");
            int age = rs1.getInt("age");
            UserProfile.gender gender = rs1.getString("gender").equals("MALE")? MALE : FEMALE;

            String userConstraints = "SELECT * FROM constraints WHERE username = '" + this.username + "';";
            ResultSet rs2 = stmt.executeQuery(userConstraints);
            rs2.next();
            int mincals = rs2.getInt("mincals");
            int maxcals = rs2.getInt("maxcals");
            int mincarbs = rs2.getInt("mincarbs");
            int maxcarbs = rs2.getInt("maxcarbs");
            int minprot = rs2.getInt("minprot");
            int maxprot = rs2.getInt("maxprot");
            int minfat = rs2.getInt("minfat");
            int maxfat = rs2.getInt("maxfat");

            Constraints constraints = new Constraints(mincals, maxcals, mincarbs, maxcarbs, minprot, maxprot, minfat, maxfat);
            UserProfile userprofile = new UserProfile(this.username, name, age, height, weight, gender);
            userprofile.setConstraints(constraints);
            
            responseObject.put("userProfile", userprofile.toJSON().toString());
        }

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
