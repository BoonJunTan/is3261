package com.tanboonjun.oneandroid.HomeItems.MoreInfo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.tanboonjun.oneandroid.R;

public class MoreInfoActivity extends Activity {

    private static final String BUNDLE_SUBTITLE = "BUNDLE_SUBTITLE";
    private static final String BUNDLE_DESCRIPTION = "BUNDLE_DESCRIPTION";

    public static void start(Activity activity,
                             String subtitle,
                             String description,
                             View card) {
        Intent starter = new Intent(activity, MoreInfoActivity.class);

        starter.putExtra(BUNDLE_SUBTITLE, subtitle);
        starter.putExtra(BUNDLE_DESCRIPTION, description);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final Pair<View, String> p1 = Pair.create(card, activity.getString(R.string.transition_card));

            final ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, p1);
            activity.startActivity(starter, options.toBundle());
        } else {
            activity.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra(BUNDLE_SUBTITLE));
        ((TextView) findViewById(R.id.tv_description)).setText(getIntent().getStringExtra(BUNDLE_DESCRIPTION));
    }

    public void onCloseClick(View v) {
        onBackPressed();
    }

}
