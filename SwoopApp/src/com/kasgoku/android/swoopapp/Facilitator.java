package com.kasgoku.android.swoopapp;

import org.json.JSONObject;

public class Facilitator {

    public String id;
    public String firstName;
    public String lastName;
    public String fullName;

    public Facilitator(JSONObject facJson) {
        this.id = facJson.optString("_id");
        this.firstName = facJson.optString("first_name");
        this.lastName = facJson.optString("last_name");
        this.fullName = facJson.optString("name");
    }
}
