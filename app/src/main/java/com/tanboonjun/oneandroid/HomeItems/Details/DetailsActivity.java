package com.tanboonjun.oneandroid.HomeItems.Details;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tanboonjun.oneandroid.HomeItems.MoreInfo.MoreInfoActivity;
import com.tanboonjun.oneandroid.R;

import java.util.ArrayList;

public class DetailsActivity extends Activity {

    private static final String BUNDLE_SUBTITLE = "BUNDLE_SUBTITLE";

    private final ArrayList<DetailsData> mListData = new ArrayList<>();

    public static void start(final Activity activity,
                             final String subtitle,
                             final View card) {
        Intent starter = new Intent(activity, DetailsActivity.class);

        starter.putExtra(BUNDLE_SUBTITLE, subtitle);

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
        setContentView(R.layout.activity_details);

        ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra(BUNDLE_SUBTITLE));

        generateData();
    }

    public void generateData() {
        String[] innerData = {"Constraints overview", "Add ConstraintLayout to your project", "Add a constraint", "Adjust the constraint bias", "Adjust the view size", "Adjust the view margins", "Control linear groups with a chain", "Automatically create constraints"};

        for (int i = 0; i < innerData.length; i++) {
            mListData.add(new DetailsData(Integer.toString(i + 1) + ": " + innerData[i]));
        }

        // Need add onClick on each innerData

        ((RecyclerView)findViewById(R.id.recycler_view_subSubTitle)).setAdapter(new DetailsAdapter(mListData));

    }

    public void onCloseClick(View v) {
        super.onBackPressed();
    }

    public void onMoreInfoClick(View v) {
        MoreInfoActivity.start(this, getIntent().getStringExtra(BUNDLE_SUBTITLE), "Test Description", findViewById(R.id.card));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    public void onTopicClick(View v) {
        Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
    }
}
