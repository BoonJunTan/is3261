package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;
import com.tanboonjun.oneandroid.HomeItems.Inner.InnerData;
import com.tanboonjun.oneandroid.HomeItems.Inner.InnerItem;
import com.tanboonjun.oneandroid.HomeItems.Outer.OuterAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    // For View
    private final static int OUTER_COUNT = 10;
    private final static int INNER_COUNT = 20;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        generateData();
    }

    public void generateData() {
        final List<List<InnerData>> outerData = new ArrayList<>();
        for (int i = 0; i < OUTER_COUNT; i++) {
            final List<InnerData> innerData = new ArrayList<>();
            for (int j = 0; j < INNER_COUNT; j++) {
                innerData.add(createInnerData());
            }
            outerData.add(innerData);
        }

        initRecyclerView(outerData);
    }

    private InnerData createInnerData() {
        return new InnerData("Test Header");
    }

    private void initRecyclerView(List<List<InnerData>> data) {
        final TailRecyclerView rv = (TailRecyclerView) getView().findViewById(R.id.recycler_view);
        ((TailLayoutManager)rv.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        rv.setAdapter(new OuterAdapter(data));

        new TailSnapHelper().attachToRecyclerView(rv);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnInnerItemClick(InnerItem item) {
        final InnerData itemData = item.getItemData();
        if (itemData == null) {
            return;
        }
    }

}
