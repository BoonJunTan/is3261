package com.tanboonjun.oneandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Locale;

import ru.noties.markwon.Markwon;

public class TopicActivity extends Activity {

    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";

    int topicID;
    String activityName;
    boolean speaking = false;
    TextToSpeech textToSpeech;
    String text;

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

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
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

                    activityName = obj.getJSONObject("success").getString("activity_name");

                    if (TextUtils.isEmpty(activityName)) {
                        Button preview_btn = (Button) findViewById(R.id.preview_button);
                        preview_btn.setVisibility(View.GONE);
                    }

                    text = obj.getJSONObject("success").getString("listen_content");
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

    public void listenClick(View view) {
        Button listenBtn = (Button) findViewById(R.id.listen_button);
        if (!speaking) {
            speaking = true;
            listenBtn.setText("Playing... Press to pause");
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            speaking = false;
            listenBtn.setText("Stopped... Press to restart");
            textToSpeech.stop();
        }

    }

    public void previewClick(View view) {
        try {
            Class<?> c = Class.forName(activityName);
            Intent intent = new Intent(this, c);
            startActivity(intent);
        } catch (ClassNotFoundException ignored) {

        }
    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    public void tryItOutClick(View view) {
        Intent myIntent = new Intent(this, TryItOutActivity.class);
        myIntent.putExtra("TopicID", topicID);
        startActivity(myIntent);
    }

}
