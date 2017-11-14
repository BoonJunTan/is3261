package com.tanboonjun.oneandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tanboonjun.oneandroid.Preview.LinearLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import ru.noties.markwon.Markwon;

public class TopicActivity extends Activity {

    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";

    int topicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        String topicTitle = getIntent().getStringExtra("TopicTitle");
        topicID = getIntent().getIntExtra("TopicID", -1);

        ((TextView) findViewById(R.id.topic)).setText(topicTitle);

        SharedPreferences prefs = getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/subtopic/" + String.valueOf(userId) + "/" + String.valueOf(topicID));
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
                    Markwon.setMarkdown((TextView) findViewById(R.id.markdown_title), obj.getJSONObject("success").get("content").toString());

                    if (obj.getJSONObject("success").getBoolean("is_completed")) {
                        Button try_it_out_btn = (Button) findViewById(R.id.try_it_out_button);
                        try_it_out_btn.setEnabled(false);
                        try_it_out_btn.setText("Completed");
                        try_it_out_btn.setBackgroundColor(getResources().getColor(R.color.gray));
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

    public void previewClick(View view) {
        Intent myIntent = new Intent(this, LinearLayoutActivity.class);
        startActivity(myIntent);
    }

    public void tryItOutClick(View view) {
        Intent myIntent = new Intent(this, TryItOutActivity.class);
        myIntent.putExtra("TopicID", topicID);
        startActivity(myIntent);
    }

}
