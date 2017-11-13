package com.tanboonjun.oneandroid;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

    // For Bottom Nav Bar
    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;
    public static final String MY_SHAREDPREFERENCE = "MySharedPreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences(MY_SHAREDPREFERENCE, MODE_PRIVATE);
        String username = prefs.getString("username", null);
        int userId = prefs.getInt("userId", -1);
        // check if specific key has value
        if ( username != null) {
            Toast.makeText(this, username + ": " + String.valueOf(userId), Toast.LENGTH_SHORT).show();
        } else {
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
        }

        mBottomNav = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        mBottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        return true;
                    }
                });

        if (getIntent().getStringExtra("load_task_fragment") != null) {
            selectFragment(mBottomNav.getMenu().getItem(2));
            mBottomNav.getMenu().getItem(2).setChecked(true);
        } else {
            selectFragment(mBottomNav.getMenu().getItem(0));
            mBottomNav.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);
        if (mSelectedItem != homeItem.getItemId()) {
            selectFragment(homeItem);
        } else {
            super.onBackPressed();
        }
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.action_home:
                frag = new HomeFragment();
                break;
            case R.id.action_courses:
                frag = new CourseFragment();
                break;
            case R.id.action_task:
                frag = new TaskFragment();
                break;
            case R.id.action_challenge:
                frag = new LeaderboardFragment();
                break;
            case R.id.action_profile:
                frag = new ProfileFragment();
                break;
        }

        // Update selected item
        mSelectedItem = item.getItemId();

        // Uncheck the other items.
        for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }

        if (frag != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, frag);
            ft.commit();
        }
    }

}
