package com.tanboonjun.oneandroid.HomeItems.MoreInfo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tanboonjun.oneandroid.R;

class MoreInfoItem extends RecyclerView.ViewHolder {

    MoreInfoItem(View itemView) {
        super(itemView);
    }

    void setContent(MoreInfoData data) {
        ((TextView)itemView.findViewById(R.id.tv_title)).setText(data.title);
        ((TextView)itemView.findViewById(R.id.tv_description)).setText(data.description);
    }

}
