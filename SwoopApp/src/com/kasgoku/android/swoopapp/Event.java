package com.kasgoku.android.swoopapp;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class Event {

    public int id;
    public String city;
    public String state;
    public String country;
    public String industry;
    public DateTime startDate;
    public String website;
    public LatLng location;
    public String twitterHashtag;
    public ArrayList<Facilitator> facilitators;
    public static DateTimeFormatter parser = ISODateTimeFormat.dateTime();
    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM dd, yyyy");
    private static Geocoder coder;

    public Event(Context context, JSONObject eventJson) {
        if (null == coder) {
            coder = new Geocoder(context);
        }
        this.id = eventJson.optInt("id");
        this.city = eventJson.optString("city");
        this.state = eventJson.optString("state");
        this.country = eventJson.optString("country");
        this.industry = eventJson.optString("vertical");

        String date = eventJson.optString("start_date");
        this.startDate = parser.parseDateTime(date);

        this.website = eventJson.optString("website");

        JSONObject loc = eventJson.optJSONObject("location");
        if (loc.length() == 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Address addr = coder.getFromLocationName(getPlace(), 1).get(0);
                        location = new LatLng(addr.getLatitude(), addr.getLongitude());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else {
            this.location = new LatLng(loc.optDouble("lat"), loc.optDouble("lng"));
        }

        this.twitterHashtag = eventJson.optString("twitter_hashtag");

        JSONArray f = eventJson.optJSONArray("facilitators");
        facilitators = new ArrayList<Facilitator>();
        for (int i = 0; i < f.length(); i++) {
            facilitators.add(new Facilitator(f.optJSONObject(i)));
        }

    }

    public String getPlace() {
        if (state.equalsIgnoreCase("null") || state.equalsIgnoreCase("")) {
            return city + ", " + country;
        }
        else {
            return city + ", " + state + ", " + country;
        }
    }

    public String getDate() {
        return fmt.print(startDate);
    }
}
