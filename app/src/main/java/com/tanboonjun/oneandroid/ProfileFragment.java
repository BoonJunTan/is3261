package com.tanboonjun.oneandroid;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";
    Button logoutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutBtn = (Button) rootView.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        Intent myIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(myIntent);
    }
}
