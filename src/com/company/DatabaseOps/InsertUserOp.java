package com.company.DatabaseOps;

import Models.Constraints;
import Models.UserProfile;
import org.json.JSONObject;

import java.sql.SQLException;

public class InsertUserOp extends DatabaseOp {
    private UserProfile userProfile;
    private Constraints constraints;
    JSONObject returnObject;
    public InsertUserOp(JSONObject jObject){
        super(jObject);
    }
    @Override
    public JSONObject performOp() throws SQLException {
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
    }

    private void getItems(){
        this.userProfile = new UserProfile(new JSONObject(this.jobject.getString("userProfile")));
        this.constraints = new Constraints(new JSONObject(this.jobject.getString("constraints")));
    }
}
