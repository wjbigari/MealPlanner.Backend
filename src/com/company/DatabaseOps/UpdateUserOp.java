package com.company.DatabaseOps;

import Models.Constraints;
import Models.UserProfile;
import org.json.JSONObject;

import java.sql.SQLException;

public class UpdateUserOp extends DatabaseOp{
    private UserProfile userProfile;
    private Constraints constraints;
    private JSONObject returnObject;

    public UpdateUserOp (JSONObject jObject){super(jObject);}

    @Override
    public JSONObject performOp() throws SQLException {
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
    }

    private void getItems(){
        this.userProfile = new UserProfile(new JSONObject(this.jobject.getString("userProfile")));
        this.constraints = new Constraints(new JSONObject(this.jobject.getString("constraints")));
    }

}
