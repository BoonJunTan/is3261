package com.tanboonjun.oneandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class EnrollActivity extends AppCompatActivity {
    TableLayout tableLayout;
    TextView topicTitleTv;
    TextView topicDescriptionTv;
    Button enrollBtn;
    int userId;
    int topicId;
    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        SharedPreferences prefs = getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        topicId = getIntent().getExtras().getInt("topicId");
        boolean isEnrolled = getIntent().getExtras().getBoolean("isEnrolled");
        topicTitleTv = (TextView) findViewById(R.id.topicTitleTv);
        topicDescriptionTv = (TextView) findViewById(R.id.topicDescriptionTv);
        enrollBtn = (Button) findViewById(R.id.enrollBtn);
        if (isEnrolled == false) {
            enrollBtn.setVisibility(View.VISIBLE);
        }
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/topic/info/" + String.valueOf(topicId));
        tableLayout = (TableLayout) findViewById(R.id.subtopic_table);
        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        TextView tv = new TextView(this);
        tv.setText("Courses");
        tv.setTextSize(16);
        tv.setPadding(20, 20, 20, 20);
        tv.setTextColor(Color.LTGRAY);
        tr.addView(tv);
        tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void enrollToTopic(View view) {
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/contact/course/" + String.valueOf(userId) + "/" + String.valueOf(topicId));
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
                JSONObject obj1 = new JSONObject(result);
                if (obj1.has("success")) {
                    try {
                        JSONObject obj = (JSONObject) obj1.get("success");
                        topicTitleTv.setText(obj.getString("title"));
                        topicDescriptionTv.setText(obj.getString("description"));
                        JSONArray array = (JSONArray) obj.getJSONArray("subtopics");
                        for (int i = 0; i < array.length(); i++) {
                            TableRow tr2 = new TableRow(getApplication());
                            tr2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                            TextView tv2 = new TextView(getApplication());
                            tv2.setText(String.valueOf(i + 1) + ". " + array.get(i));
                            tv2.setTextSize(16);
                            tv2.setPadding(20, 20, 20, 20);
                            tv2.setTextColor(Color.WHITE);
                            tr2.addView(tv2);
                            tableLayout.addView(tr2, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT));
                        }
                    } catch(Exception e) {
                        Toast.makeText(EnrollActivity.this, "Successfully enrolled!", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(EnrollActivity.this, MainActivity.class);
                        startActivity(myIntent);
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
