package com.tanboonjun.oneandroid.HomeItems.Inner;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanboonjun.oneandroid.R;

import org.greenrobot.eventbus.EventBus;

public class InnerItem extends com.ramotion.garlandview.inner.InnerItem {

    private final View mInnerLayout;

    public final TextView mHeader;

    private InnerData mInnerData;

    public InnerItem(View itemView) {
        super(itemView);

        mInnerLayout = ((ViewGroup)itemView).getChildAt(0);

        mHeader = (TextView) itemView.findViewById(R.id.tv_header);

        mInnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(InnerItem.this);
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

        mHeader.setText(data.title);
    }

}
