package com.kasgoku.android.swoopapp;

import org.joda.time.DateTimeUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapViewFragment extends Fragment {

    private GoogleMap map;
    private SupportMapFragment frag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.map_view_fragment, null);

        frag = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.thatMap);
        map = frag.getMap();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.5, -98.35), 4f));
        for (Event e : ((MainActivity) getActivity()).getEvents()) {
            MarkerOptions options = new MarkerOptions().position(e.location).title(e.getPlace()).snippet(e.getDate());
            if (e.startDate.isAfter(DateTimeUtils.currentTimeMillis())) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            }
            else {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }

            map.addMarker(options);
        }

        return fragView;
    }

    public void selectItem(Event event) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(event.location, 19f));
    }

}
