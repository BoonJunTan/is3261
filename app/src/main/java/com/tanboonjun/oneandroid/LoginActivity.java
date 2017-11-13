package com.tanboonjun.oneandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameEt = (EditText) findViewById(R.id.usernameEt);
        passwordEt = (EditText) findViewById(R.id.passwordEt);
    }

    public void loginClick(View view) {
        String username = String.valueOf(usernameEt.getText());
        String password = String.valueOf(passwordEt.getText());
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/contact/login", username, password);
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
                    obj.put("username" , str[1]);
                    obj.put("password" , str[2]);

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
                    SharedPreferences.Editor editor = getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE).edit();
                    editor.putString("username", String.valueOf(usernameEt.getText()));
                    editor.putInt("userId", obj.getInt("success"));
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(), "in onPostExecute of Activity3, result = " + obj.has("success"), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // from internet
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
