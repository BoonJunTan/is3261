package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {


    public TaskFragment() {
        // Required empty public constructor
    }

    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";
    TableLayout task_list_table;
    ViewGroup c;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        c = container;
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        task_list_table = (TableLayout) view.findViewById(R.id.task_table_layout);
        SharedPreferences prefs = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/contact/progress/" + String.valueOf(userId));

        return view;
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
                    JSONArray array = (JSONArray) obj.get("success");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject currentObj = (JSONObject) array.get(i);
                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View taskRowView = inflater.inflate(R.layout.task_table_row, c, false);
                        TableRow task_list_row = (TableRow) taskRowView.findViewById(R.id.task_table_row);
                        TextView titleTv = (TextView) task_list_row.findViewById(R.id.titleTv);
                        TextView subTitleTv = (TextView) task_list_row.findViewById(R.id.subTitleTv);
                        TextView pctTv = (TextView) task_list_row.findViewById(R.id.pctTv);
                        ProgressBar progressBar = (ProgressBar) task_list_row.findViewById(R.id.taskProgressBar);
                        titleTv.setText(currentObj.getString("title"));
                        subTitleTv.setText(currentObj.getString("title"));
                        progressBar.setProgress(currentObj.getInt("completed"));
                        String pct = String.valueOf(currentObj.getDouble("completed")) + " %";
                        pctTv.setText(pct);
                        task_list_table.addView(task_list_row);
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
