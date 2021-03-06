package de.jschmucker.indoorcontroller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.jschmucker.indoorcontroller.controller.InfoActivity;
import de.jschmucker.indoorcontroller.controller.SettingsActivity;
import de.jschmucker.indoorcontroller.controller.action.MainActionFragment;
import de.jschmucker.indoorcontroller.controller.location.MainLocationFragment;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.controller.task.MainTasksFragment;

/**
 * The MainActivity of this app.
 * Contains three fragments:
 * The ActionFragment
 * The LocationFragment
 * The TaskFragment
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAGMENT_KEY = "FRAGMENT_KEY";

    private Fragment fragment = new MainTasksFragment();
    private IndoorService indoorService;
    private boolean bound = false;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Saves the information which fragment is the actual attached fragment to the SharedPreferences.
     */
    private void saveFragmentSettings() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (fragment instanceof MainTasksFragment) {
            editor.putString(FRAGMENT_KEY, getString(R.string.rules));
        } else if (fragment instanceof MainLocationFragment) {
            editor.putString(FRAGMENT_KEY, getString(R.string.locations));
        } else if (fragment instanceof MainActionFragment) {
            editor.putString(FRAGMENT_KEY, getString(R.string.actions));
        }
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String fragmentString = preferences.getString(FRAGMENT_KEY, "");
        if (!fragmentString.matches("")) {
            if (fragmentString.matches(getString(R.string.locations))) {
                fragment = new MainLocationFragment();
                setTitle(R.string.locations);
            } else if (fragmentString.matches(getString(R.string.rules))) {
                fragment = new MainTasksFragment();
                setTitle(R.string.rules);
            } else if (fragmentString.matches(getString(R.string.actions))) {
                fragment = new MainActionFragment();
                setTitle(getString(R.string.actions));
            }
        } else {
            fragment = new MainTasksFragment();
        }

        // Bind to Service
        Intent intent = new Intent(this, IndoorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unbind from service
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Maybe add menu, i.e. for settings
        //getMenuInflater().inflate(R.menu.rule, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rules) {
            fragment = new MainTasksFragment();
            saveFragmentSettings();
            toolbar.setTitle(R.string.rules);
            setFragment();
        } else if (id == R.id.nav_sensors) {
            fragment = new MainLocationFragment();
            saveFragmentSettings();
            toolbar.setTitle(R.string.locations);
            setFragment();
        } else if (id == R.id.nav_actions) {
            fragment = new MainActionFragment();
            saveFragmentSettings();
            toolbar.setTitle(getString(R.string.actions));
            setFragment();
        } else if (id == R.id.nav_about) {
            openAbout();
        } else if (id == R.id.nav_settings) {
            openSettings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Replaces the actual attached fragment with the variable fragment.
     */
    private void setFragment() {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Starts the SettingsActivity.
     */
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the InfoActivity.
     */
    private void openAbout() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    /**
     * @return The IndoorService.
     */
    public IndoorService getIndoorService() {
        return indoorService;
    }

    /**
     * ServiceConnection for the IndoorService.
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IndoorService.IndoorBinder binder = (IndoorService.IndoorBinder) service;
            indoorService = binder.getService();
            bound = true;

            setFragment();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };
}
