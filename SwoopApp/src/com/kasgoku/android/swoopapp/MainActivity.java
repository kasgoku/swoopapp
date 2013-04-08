package com.kasgoku.android.swoopapp;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import com.viewpagerindicator.TitlePageIndicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

    private ArrayList<Event> eventList;
    private ViewPager pager;

    private ListViewFragment listFrag;
    private MapViewFragment mapFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load the event list
        loadData();

        // Set the pager with an adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SwoopAdapter(getSupportFragmentManager()));

        // Bind the title indicator to the adapter
        TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.titles);
        titleIndicator.setViewPager(pager);
    }

    private void loadData() {
        StringWriter writer = new StringWriter();
        eventList = new ArrayList<Event>();
        try {
            IOUtils.copy(getAssets().open("events.json"), writer, "UTF-8");
            String theString = writer.toString();
            JSONArray array = new JSONArray(theString);
            for (int i = 0; i < array.length(); i++) {
                eventList.add(new Event(this, array.getJSONObject(i)));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class SwoopAdapter extends FragmentPagerAdapter {

        public SwoopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            Fragment swoopFrag = null;
            switch (position) {
                case 0:
                    listFrag = new ListViewFragment();
                    swoopFrag = listFrag;
                    break;
                case 1:
                    mapFrag = new MapViewFragment();
                    swoopFrag = mapFrag;
                    break;
            }

            return swoopFrag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Events";
                case 1:
                    return "Map";
            }
            return super.getPageTitle(position);
        }

    }

    public ArrayList<Event> getEvents() {
        return eventList;
    }

    public void showLocation(Event event) {
        pager.setCurrentItem(1);

        mapFrag.selectItem(event);
    }
}
