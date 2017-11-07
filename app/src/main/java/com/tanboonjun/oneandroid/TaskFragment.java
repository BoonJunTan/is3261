package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {


    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        TableLayout task_list_table = (TableLayout) view.findViewById(R.id.task_table_layout);

        for (int i = 0; i < 5; i++) {
            View taskRowView = inflater.inflate(R.layout.task_table_row, container, false);
            TableRow task_list_row = (TableRow) taskRowView.findViewById(R.id.task_table_row);

            task_list_table.addView(task_list_row);
        }

        return view;
    }

}
