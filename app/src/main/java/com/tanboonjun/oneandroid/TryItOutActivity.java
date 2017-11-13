package com.tanboonjun.oneandroid;

import android.app.Activity;
import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TryItOutActivity extends Activity {

    ConstraintLayout activity_try_it_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_it_out);
        activity_try_it_out = (ConstraintLayout)findViewById(R.id.activity_try_it_out);

        int topicID = getIntent().getIntExtra("TopicID", -1);

        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/subtopic/1/" + String.valueOf(topicID));
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
                    TableLayout questions_list_table = (TableLayout) findViewById(R.id.try_it_out_table_layout);

                    for (int i = 0; i < obj.getJSONObject("success").getJSONArray("questions").length(); i++) {
                        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);

                        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

                        View taskRowView = inflater.inflate(R.layout.try_it_out_table_row, viewGroup, false);
                        TableRow task_list_row = (TableRow) taskRowView.findViewById(R.id.try_it_out_table_row);

                        TextView questionTV = (TextView) task_list_row.findViewById(R.id.markdown_question_tv);
                        questionTV.setText(obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getString("title"));

                        questions_list_table.addView(task_list_row);

                        LinearLayout answerLayout = (LinearLayout) taskRowView.findViewById(R.id.answers_layout);

                        for (int x = 0; x < obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getJSONArray("answers").length(); x++) {
                            Button currentBtn = new Button(getApplicationContext());
                            currentBtn.setText(obj.getJSONObject("success").getJSONArray("questions").getJSONObject(i).getJSONArray("answers").getString(x));
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
