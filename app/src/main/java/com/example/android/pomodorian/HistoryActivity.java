package com.example.android.pomodorian;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lukem on 11/8/2017.
 */

public class HistoryActivity extends AppCompatActivity {

    // ListView to be used with the Navigation Drawer
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Session> sessions;
    ListView sessionListView;
    SessionAdapter adapter;
    PomodoroAppDBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Instantiate the Navigation Drawer
        mDrawerList = (ListView) findViewById(R.id.navList);

        // Show the toggle for the Navigation drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Get reference to the drawer layout and current activity
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        // Instantiate the entries in the nav drawer
        addNavDrawerItems();
        setupDrawer();

        dbHelper = new PomodoroAppDBHelper(this, "pomodorian", null, 1);

        sessions = dbHelper.populateListFromDB();

        /*
        for (int i = 0; i < 20; i++) {
            sessions.add(new Session());
        }
        */


        sessionListView = (ListView) findViewById(R.id.list);
        adapter = new SessionAdapter(this, sessions);

        sessionListView.setAdapter(adapter);

        if (adapter.isEmpty()) {
            TextView error = (TextView) findViewById(R.id.database_empty_text_view);
            error.setVisibility(View.VISIBLE);
        } else {
            TextView error = (TextView) findViewById(R.id.database_empty_text_view);
            error.setVisibility(View.GONE);
        }

    }

    // Sync the Nav Drawer icon with the current activity
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void addNavDrawerItems() {
        // ArrayAdapter to be used with the Navigation Drawer
        ArrayAdapter<String> mAdapter;
        final String[] activitiesArray = {getString(R.string.session), getString(R.string.history)};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activitiesArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String activity = activitiesArray[position];
                if (position == 0) {
                    finish();
                    // Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                }
                /*
                if (position == 0 && (!parentList.equals(listNames.get(0)))) {
                    // Open an intent to the first list
                    Intent list1Intent = new Intent(MainActivity.this, classesArray[0]);
                    new Utils().putListExtrasInIntentBundle(list1Intent, listNames);
                    list1Intent.putExtra("parentList", listNames.get(0));
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    startActivity(list1Intent);
                }
                */
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item click
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
