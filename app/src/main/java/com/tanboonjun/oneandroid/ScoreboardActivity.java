package com.tanboonjun.oneandroid;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ScoreboardActivity extends AppCompatActivity {


    TextView scoreboardTitleTv;
    TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        scoreboardTitleTv = (TextView) findViewById(R.id.scoreboardTitleTv);
        tableLayout = (TableLayout) findViewById(R.id.userpoint_table);
        String location = getIntent().getExtras().getString("location");
        scoreboardTitleTv.setText(location + " Leaderboard");
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/location/leaderboard", location);
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
                httpURLConnection.setRequestMethod("POST");
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream ());
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("location" , str[1]);
                    wr.writeBytes(obj.toString());
                    wr.flush();
                    wr.close();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
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
                    JSONArray array = (JSONArray) obj.getJSONArray("success");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject user = (JSONObject) array.get(i);
                        TableRow tr = new TableRow(getApplication());
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                        TextView tv = new TextView(getApplication());
                        tv.setText(String.valueOf(i + 1) + ". " + user.getString("username") + " - " + user.getInt("count")) ;
                        tv.setTextSize(16);
                        tv.setPadding(20, 20, 20, 20);
                        tv.setTextColor(Color.WHITE);
                        tr.addView(tv);
                        tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You have entered invalid username or password.", Toast.LENGTH_SHORT).show();
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
