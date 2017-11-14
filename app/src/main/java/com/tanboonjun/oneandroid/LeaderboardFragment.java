package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


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

        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/location");
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(1.290270,103.851959)).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return rootView;
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str) {
            URL url = convertToUrl(str[0]);
            HttpURLConnection httpURLConnection = null;
            int responseCode;
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine())!=null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            return stringBuilder.toString();
        }

        public void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.has("success")) {
                    JSONArray array = obj.getJSONArray("success");
                    for(int i = 0 ; i < array.length(); i++) {
                        JSONObject currentObj = (JSONObject) array.get(i);
                        LatLng location = new LatLng(currentObj.getDouble("longitude"), currentObj.getDouble("latitude"));
                        String name = currentObj.getString("name");
                        if (i == 0) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title("Champion")
                                    .snippet(name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.medalgold))
                            );
                        } else if (i == 1) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title("First Runner-up")
                                    .snippet(name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.medalsilver))
                            );
                        } else if (i == 2) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title("Second Runner-up")
                                    .snippet(name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.medalbronze))
                            );
                        } else {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title("Commoners")
                                    .snippet(name)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.nonstar))
                            );
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // From Internet
    private URL convertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
