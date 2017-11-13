package com.tanboonjun.oneandroid.HomeItems.Topic;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.tanboonjun.oneandroid.R;

public class TopicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        String topicTitle = getIntent().getStringExtra("TopicTitle");

        ((TextView) findViewById(R.id.topic)).setText(topicTitle.substring(topicTitle.indexOf(":") + 1));

        ((TextView) findViewById(R.id.topic_description)).setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    }
}
