package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {


    public CourseFragment() {
        // Required empty public constructor
    }

    List<Topic> result1 = new ArrayList<>();
    GridView gridView;
    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);
        SharedPreferences prefs = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent myIntent = new Intent(getActivity(), EnrollActivity.class);
                int thisId = result1.get(position).getId();
                myIntent.putExtra("topicId", thisId);
                myIntent.putExtra("isEnrolled", result1.get(position).getIsEnrolled());
                startActivity(myIntent);
            }
        });
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/topic/all/" + String.valueOf(userId));
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
                    Topic[] topics = new Topic[array.length()];
                    for (int i = 0 ; i < array.length() ; i++) {
                        JSONObject data = (JSONObject)array.get(i);
                        Topic abc = new Topic(data.getString("title"), data.getBoolean("is_enrolled"), data.getInt("id"));
                        result1.add(abc);
                    }

                    CoursesAdapter coursesAdapter = new CoursesAdapter(getActivity(), result1);
                    gridView.setAdapter(coursesAdapter);
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
