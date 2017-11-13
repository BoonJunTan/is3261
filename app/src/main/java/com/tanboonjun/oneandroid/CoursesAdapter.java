package com.tanboonjun.oneandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ronald on 13/11/17.
 */

public class CoursesAdapter extends BaseAdapter {
    private final Context mContext;
    private final Topic[] topics;
    ImageView picture;
    TextView titleEt;
    TextView subtitleEt;
    TextView enrolledEt;

    public CoursesAdapter(Context context, Topic[] topics) {
        this.mContext = context;
        this.topics = topics;
    }

    @Override
    public int getCount() {
        return topics.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Topic topic = topics[i];

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.courses_item, null);
            view.setTag(R.id.picture, view.findViewById(R.id.picture));
            view.setTag(R.id.titleEt, view.findViewById(R.id.titleEt));
            view.setTag(R.id.subtitleEt, view.findViewById(R.id.subtitleEt));
            view.setTag(R.id.enrolledEt, view.findViewById(R.id.enrolledEt));
        }

        picture = (ImageView) view.getTag(R.id.picture);
        titleEt = (TextView) view.getTag(R.id.titleEt);
        subtitleEt = (TextView) view.getTag(R.id.subtitleEt);
        enrolledEt = (TextView) view.getTag(R.id.enrolledEt);
        if (topic.getIsEnrolled() == true) {
            picture.setImageResource(R.drawable.green_template);
            enrolledEt.setVisibility(view.VISIBLE);
        } else {
            picture.setImageResource(R.drawable.blank_template);
        }

        titleEt.setText(topic.getTitle());
        subtitleEt.setText("subtitle");
        enrolledEt.setText("Enrolled");

        return view;
    }
}
