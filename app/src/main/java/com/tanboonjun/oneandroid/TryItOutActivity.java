package com.tanboonjun.oneandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TryItOutActivity extends Activity {

    LinearLayout footerBtnLayout;
    ConstraintLayout activity_try_it_out;
    int noOfAnswerCorrectlySelected = 0;
    int topicID;
    int userID;

    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_it_out);
        activity_try_it_out = (ConstraintLayout)findViewById(R.id.activity_try_it_out);
        footerBtnLayout = (LinearLayout) findViewById(R.id.footer_btn_layout);

        topicID = getIntent().getIntExtra("TopicID", -1);

        SharedPreferences prefs = getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        userID = prefs.getInt("userId", -1);

        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/subtopic/" + String.valueOf(userID) + "/" + String.valueOf(topicID));

        footerBtnLayout.setVisibility(LinearLayout.GONE);
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
                final JSONObject obj = new JSONObject(result);
                if (obj.has("success")) {
                    TableLayout questions_list_table = (TableLayout) findViewById(R.id.try_it_out_table_layout);

                    final int noOfQuestions = obj.getJSONObject("success").getJSONArray("questions").length();

                    for (int i = 0; i < noOfQuestions; i++) {
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);

                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

                        View taskRowView = inflater.inflate(R.layout.try_it_out_table_row, viewGroup, false);
                        TableRow task_list_row = (TableRow) taskRowView.findViewById(R.id.try_it_out_table_row);

                        TextView questionTV = (TextView) task_list_row.findViewById(R.id.markdown_question_tv);
                        questionTV.setText(obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getString("title"));

                        questions_list_table.addView(task_list_row);

                        LinearLayout answerLayout = (LinearLayout) taskRowView.findViewById(R.id.answers_layout);

                        final String answer = obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getString("correct_answer");

                        for (int x = 0; x < obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getJSONArray("answers").length(); x++) {
                            final Button currentBtn = new Button(getApplicationContext());
                            final String buttonText = obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getJSONArray("answers").getString(x);
                            currentBtn.setText(buttonText);
                            currentBtn.setTextSize(13);
                            currentBtn.setAllCaps(false);
                            currentBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (buttonText.equals(answer)) {
                                        Toast.makeText(getApplicationContext(), "You have selected the right answer, congrats.", Toast.LENGTH_SHORT).show();
                                        noOfAnswerCorrectlySelected++;
                                        currentBtn.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                                        currentBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        currentBtn.setEnabled(false);
                                        if (noOfAnswerCorrectlySelected == noOfQuestions) {
                                            footerBtnLayout.setVisibility(LinearLayout.VISIBLE);
                                            new FinishTopicTask().execute("https://anchantapp.herokuapp.com/contact/set/" + String.valueOf(userID) + "/" + String.valueOf(topicID));
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "You have selected the wrong answer, please pick another one.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            currentBtn.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));

                            answerLayout.addView(currentBtn);
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class FinishTopicTask extends AsyncTask<String, Void, String> {

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
                final JSONObject obj = new JSONObject(result);
                if (obj.has("success")) {

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

    public void nextSubTopicBtnClick(View view) {

    }

    public void seeMyProgressBtnClick(View view) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        myIntent.putExtra("load_task_fragment", "task_fragment");
        startActivity(myIntent);
    }

}
