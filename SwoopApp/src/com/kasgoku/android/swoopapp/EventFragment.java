package com.kasgoku.android.swoopapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventFragment extends Fragment {

    private TextView where;
    private TextView when;
    private TextView website;
    private TextView tweets;
    private LinearLayout peopleList;

    private LinearLayout twitterLayout;
    private LinearLayout whereLayout;
    private LinearLayout whenLayout;
    private LinearLayout websiteLayout;
    private LinearLayout peopleLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View eventCard = inflater.inflate(R.layout.event_card_layout, null);

        where = (TextView) eventCard.findViewById(R.id.event_where);
        when = (TextView) eventCard.findViewById(R.id.event_when);
        website = (TextView) eventCard.findViewById(R.id.event_website);
        tweets = (TextView) eventCard.findViewById(R.id.event_tweets);
        twitterLayout = (LinearLayout) eventCard.findViewById(R.id.twitter_layout);
        peopleList = (LinearLayout) eventCard.findViewById(R.id.people_list);

        whereLayout = (LinearLayout) eventCard.findViewById(R.id.where_layout);
        whenLayout = (LinearLayout) eventCard.findViewById(R.id.when_layout);
        websiteLayout = (LinearLayout) eventCard.findViewById(R.id.website_layout);
        peopleLayout = (LinearLayout) eventCard.findViewById(R.id.people_layout);

        return eventCard;
    }

    public void showEvent(final Event event) {
        twitterLayout.setVisibility(View.VISIBLE);
        whereLayout.setVisibility(View.VISIBLE);
        whenLayout.setVisibility(View.VISIBLE);
        websiteLayout.setVisibility(View.VISIBLE);
        peopleLayout.setVisibility(View.VISIBLE);

        whereLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showLocation(event);
            }
        });
        whenLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", event.startDate.getMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("title", "Startup Weekend at " + event.getPlace());
                startActivity(intent);
            }
        });

        where.setText(event.getPlace());
        when.setText(event.getDate());

        if (event.website.equalsIgnoreCase("null")) {
            website.setText("This event does not have a website.");
        }
        else {
            website.setText(event.website);
        }

        if (event.twitterHashtag.equalsIgnoreCase("null")) {
            tweets.setText("No Twitter Hashtag found.");
        }
        else {
            tweets.setText(event.twitterHashtag);
            twitterLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String url;
                    try {
                        url = "https://mobile.twitter.com/search?q=" + URLEncoder.encode(event.twitterHashtag, "utf-8") + "&s=tren";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    // Add Tweets
                    // TODO: Gets hashtag tweets.
                }
            });
        }

        peopleList.removeAllViews();
        if (event.facilitators.size() > 0) {
            for (Facilitator f : event.facilitators) {
                TextView someView = new TextView(getActivity());
                someView.setTextColor(Color.WHITE);
                someView.setTextSize(20);
                someView.setText(f.fullName);
                peopleList.addView(someView);
            }
        }

        Linkify.addLinks(website, Linkify.ALL);
    }

}
