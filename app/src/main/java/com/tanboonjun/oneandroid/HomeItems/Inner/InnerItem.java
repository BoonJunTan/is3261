package com.tanboonjun.oneandroid.HomeItems.Inner;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanboonjun.oneandroid.TopicActivity;
import com.tanboonjun.oneandroid.R;

public class InnerItem extends com.ramotion.garlandview.inner.InnerItem {

    private final View mInnerLayout;

    public final TextView mHeader;

    private InnerData mInnerData;

    public InnerItem(View itemView) {
        super(itemView);

        mInnerLayout = ((ViewGroup)itemView).getChildAt(0);

        mHeader = (TextView) itemView.findViewById(R.id.header_text_1);

        mInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mInnerLayout.getContext(), TopicActivity.class);
                myIntent.putExtra("TopicTitle", mHeader.getText());
                myIntent.putExtra("TopicID", getItemData().subTitleID);
                mInnerLayout.getContext().startActivity(myIntent);
            }
        });

    }

    @Override
    protected View getInnerLayout() {
        return mInnerLayout;
    }

    public InnerData getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        mInnerData = null;
    }

    void setContent(InnerData data) {
        mInnerData = data;

        mHeader.setText(data.subTitle);
    }

}
