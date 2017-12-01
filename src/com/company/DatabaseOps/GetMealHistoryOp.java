package com.company.DatabaseOps;

import org.json.JSONObject;

import java.sql.SQLException;

public class GetMealHistoryOp extends DatabaseOp {
    public GetMealHistoryOp(JSONObject jrequest) {
        super(jrequest);
    }

    @Override
    public JSONObject performOp() throws SQLException {
        return null;
    }
}
