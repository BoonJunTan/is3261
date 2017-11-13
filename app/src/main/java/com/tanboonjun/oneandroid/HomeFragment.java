package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.os.AsyncTask;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
        //generateData();
        new MyAsyncTask().execute("https://anchantapp.herokuapp.com/topic");
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

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... str) {
            URL url = convertToUrl(str[0]);
            HttpURLConnection httpURLConnection = null;
            int responseCode;
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();
                if (responseCode == httpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while ((line = reader.readLine())!=null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                }
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();
            }
            return stringBuilder.toString();
        }

        public void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.has("success")) {

                    final List<List<InnerData>> outerData = new ArrayList<>();

                    Iterator<?> keys = obj.getJSONObject("success").keys();

                    while (keys.hasNext()) {
                        String key = (String)keys.next();
                        final List<InnerData> innerData = new ArrayList<>();

                        // First Add Title
                        innerData.add(new InnerData(key));

                        for (int i = 0; i < obj.getJSONObject("success").getJSONArray(key).length(); i++) {
                            innerData.add(new InnerData(obj.getJSONObject("success").getJSONArray(key).getJSONObject(i).get("title").toString()));
                        }

                        outerData.add(innerData);
                    }

                    initRecyclerView(outerData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // From Internet
    private URL convertToUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(),
                    url.getQuery(), url.getRef());
            url = uri.toURL();
            return url;
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
