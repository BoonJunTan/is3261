package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";
    Button logoutBtn;
    Button saveBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(myIntent);
            }
        });

        saveBtn = (Button) rootView.findViewById(R.id.save_detail_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE).edit();
                EditText nameEditText = (EditText) getActivity().findViewById(R.id.nameEditText);
                editor.putString("username", String.valueOf(nameEditText.getText()));

                SharedPreferences prefs = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);
                new MyAsyncTask().execute("https://anchantapp.herokuapp.com/contact/update/" + String.valueOf(userId) + "/", nameEditText.getText().toString());
            }
        });

        SharedPreferences prefs = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        String userName = prefs.getString("username", null);
        String location = prefs.getString("userLocation", null);

        EditText nameEditText = (EditText) rootView.findViewById(R.id.nameEditText);
        nameEditText.setText(userName);
        nameEditText.setHint("Username");
        nameEditText.setHintTextColor(getResources().getColor(R.color.white));

        TextView locationTextView = (TextView) rootView.findViewById(R.id.locationTextView);
        locationTextView.setText(location);

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
                httpURLConnection.setRequestMethod("POST");
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream ());
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("username" , str[1]);

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
                    Toast.makeText(getActivity().getApplicationContext(), "Update successfully.", Toast.LENGTH_SHORT).show();
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
