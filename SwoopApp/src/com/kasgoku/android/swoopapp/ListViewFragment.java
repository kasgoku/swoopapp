package com.kasgoku.android.swoopapp;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewFragment extends Fragment {

    ListView list;
    EventFragment eventCard;
    private int activatedView = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_fragment, null);

        eventCard = (EventFragment) getFragmentManager().findFragmentById(R.id.eventCard);
        list = (ListView) view.findViewById(R.id.event_list);
        list.setAdapter(new EventAdapter(getActivity(), android.R.layout.simple_list_item_1, ((MainActivity) getActivity()).getEvents()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                eventCard.showEvent(((MainActivity) getActivity()).getEvents().get(position));
                activatedView = position;

                list.invalidateViews();
            }

        });

        return view;
    }

    private class EventAdapter extends ArrayAdapter<Event> {

        public EventAdapter(Context context, int textViewResourceId, List<Event> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.event_list_item, null);
            }

            TextView viewText = (TextView) convertView.findViewById(R.id.city_view);
            viewText.setText(getItem(position).getPlace());
            if (position == activatedView) {
                convertView.setActivated(true);
            }
            else {
                convertView.setActivated(false);
            }

            return convertView;
        }
    }

}
