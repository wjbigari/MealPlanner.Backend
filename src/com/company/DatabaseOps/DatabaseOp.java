package com.company.DatabaseOps;

import org.json.JSONObject;

import java.sql.SQLException;

abstract public class DatabaseOp {
    protected JSONObject jobject;

    public DatabaseOp(JSONObject jobject){
        this.jobject = jobject;
    }

    public abstract JSONObject performOp() throws SQLException;


}
