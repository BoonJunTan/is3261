package com.tanboonjun.oneandroid;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {


    public CourseFragment() {
        // Required empty public constructor
    }

    private Topic[] topics = {
            new Topic("Topic1", true),
            new Topic("topic2", false),
            new Topic("Topic3", false),
            new Topic("topic4", true),
            new Topic("Topic5", false),
            new Topic("topic6", true),
            new Topic("Topic7", false),
            new Topic("topic8", false)
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        CoursesAdapter coursesAdapter = new CoursesAdapter(getActivity(), topics);
        gridView.setAdapter(coursesAdapter);

        return rootView;
    }

}
