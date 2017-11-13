package com.tanboonjun.oneandroid.HomeItems.Details;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tanboonjun.oneandroid.R;

class DetailsItem extends RecyclerView.ViewHolder {

    private final View mDetailLayout;

    public final TextView mHeader;

    DetailsItem(View itemView) {
        super(itemView);

        mHeader = (TextView) itemView.findViewById(R.id.tv_title);

        mDetailLayout = ((ViewGroup)itemView).getChildAt(0);

        mDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mDetailLayout.getContext(), mHeader.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setContent(DetailsData data) {
        ((TextView)itemView.findViewById(R.id.tv_title)).setText(data.title);
    }



}
