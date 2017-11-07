package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderboardFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                List<LatLng> locations = new ArrayList<LatLng>();
                locations.add(new LatLng(1.369115, 103.845436));
                locations.add(new LatLng(1.3504, 103.8488));
                locations.add(new LatLng(1.3349, 103.8492));
                locations.add(new LatLng(1.371778, 103.893059));
                for(int i = 0 ; i < locations.size(); i++) {
                    LatLng location = locations.get(i);
                    if (i == 0) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title("THE CHAMP")
                                .snippet("Marker Description")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.starone))
                        );
                    } else if (i == 1) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title("THE FIRST LOSER")
                                .snippet("Marker Description")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.startwo))
                        );
                    } else if (i == 2) {
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title("THE POINTLESS")
                                .snippet("Marker Description")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.starthree))
                        );
                    } else {
                        googleMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title("THE COMMONERS")
                                .snippet("Marker Description")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.nonstar))
                        );
                    }

                }
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(1.290270,103.851959)).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });



        return rootView;
    }

}
