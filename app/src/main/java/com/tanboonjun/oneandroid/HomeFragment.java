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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

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
    }

    @Override
    public void onResume() {
        super.onResume();
        generateData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void generateData() {
        String[] outerDataLists = {"Getting Started", "Building Apps with Content Sharing", "Best Practices for User Interface", "Best Practices for User Input"};

        ArrayList<String[]> innerDataLists = new ArrayList<>();
        String[] innerData1 = {"Building Your First App", "Supporting Different Devices", "Building a Dynamic UI with Fragment", "Saving Data", "Interacting with Other Apps", "Working with System Permissions"};
        String[] innerData2 = {"Sharing Simple Data", "Sharing Files", "Sharing Files with NFC"};
        String[] innerData3 = {"Designing for Multiple Screens", "Build a Responsive UI with ConstraintLayout", "Adding the App Bar", "Showing Pop-Up Messages", "Creating Custom View", "Creating Backward-Compatible UIs", "Implementing Accessibility", "Managing the System UI", "Creating Apps with Material Design"};
        String[] innerData4 = {"Using Touch Gestures", "Handling Keyboard Input", "Supporting Game Controllers"};
        innerDataLists.add(innerData1);
        innerDataLists.add(innerData2);
        innerDataLists.add(innerData3);
        innerDataLists.add(innerData4);

        final List<List<InnerData>> outerData = new ArrayList<>();
        for (int i = 0; i < outerDataLists.length; i++) {
            final List<InnerData> innerData = new ArrayList<>();

            // First Add Title
            innerData.add(new InnerData(outerDataLists[i]));

            // Then Add Sub-Title
            for (int j = 0; j < innerDataLists.get(i).length; j++) {
                innerData.add(new InnerData(innerDataLists.get(i)[j]));
            }

            outerData.add(innerData);
        }

        initRecyclerView(outerData);
    }

    private void initRecyclerView(List<List<InnerData>> data) {
        final TailRecyclerView rv = (TailRecyclerView) getView().findViewById(R.id.recycler_view);
        ((TailLayoutManager)rv.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        rv.setAdapter(new OuterAdapter(data));

        rv.setOnFlingListener(null);
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
