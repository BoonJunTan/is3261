package com.tanboonjun.oneandroid.HomeItems.Details;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tanboonjun.oneandroid.R;
import com.tanboonjun.oneandroid.databinding.ActivityDetailsBinding;

import java.util.List;

class DetailsAdapter extends RecyclerView.Adapter<DetailsItem> {

    private final List<DetailsData> mData;

    DetailsAdapter(final List<DetailsData> data) {
        super();
        mData = data;
    }

    @Override
    public DetailsItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final ActivityDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_details, parent, false);
        return new DetailsItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(DetailsItem holder, int position) {
        holder.setContent(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
