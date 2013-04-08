package com.kasgoku.android.swoopapp;

import org.joda.time.DateTimeUtils;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MapViewFragment extends Fragment {

    private GoogleMap map;
    private SupportMapFragment frag;
    private int tourPosition = 0;
    private TextView thatTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aciton_start_tour) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    tourPosition = ((MainActivity) getActivity()).getEvents().size();
                    thatTextView.setVisibility(View.VISIBLE);
                    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(0, 0)).zoom(18f).tilt(70).build()));
                    animateToNext();
                }

                private void animateToNext() {
                    Event nextEvent = ((MainActivity) getActivity()).getEvents().get(--tourPosition);
                    thatTextView.setText(nextEvent.getPlace() + " at " + nextEvent.getDate());

                    map.animateCamera(CameraUpdateFactory.newLatLng(nextEvent.location), new FirstCallBack());
                }

                private void goAround() {
                    CameraUpdate update =
                            CameraUpdateFactory.newCameraPosition(CameraPosition.builder(map.getCameraPosition())
                                    .bearing((map.getCameraPosition().bearing + 180) % 360).build());
                    map.animateCamera(update, 5000, new SecondCallBack());
                }

                class FirstCallBack implements GoogleMap.CancelableCallback {

                    @Override
                    public void onCancel() {
                        tourPosition = ((MainActivity) getActivity()).getEvents().size();
                    }

                    @Override
                    public void onFinish() {
                        try {
                            Thread.sleep(3000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        goAround();
                    }

                }

                class SecondCallBack implements GoogleMap.CancelableCallback {

                    @Override
                    public void onCancel() {
                        tourPosition = ((MainActivity) getActivity()).getEvents().size();
                    }

                    @Override
                    public void onFinish() {
                        try {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        animateToNext();
                    }

                }

            });

            return true;
        }
        else if (item.getItemId() == R.id.aciton_end_tour) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragView = inflater.inflate(R.layout.map_view_fragment, null);

        thatTextView = (TextView) fragView.findViewById(R.id.thatTextView);
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
