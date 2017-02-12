package de.jschmucker.indoorcontroller.controller.location;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.ort.Location;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NfcDetectionFragment;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NfcSpot;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.Room;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiDetectionFragment;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiEnvironment;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.RoomDetectionFragment;

public class ChangeLocationActivity extends AppCompatActivity implements IndoorServiceProvider {
    public static final String LOCATION_ID = "LOCATION_ID";
    private Location location;
    private IndoorService indoorService;
    private boolean bound = false;
    private int locationID;

    private TextView name;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_ort);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        locationID = intent.getIntExtra(LOCATION_ID, -1);
        if (locationID == -1) {
            //ToDo: Opened ChangeLocationActivity without specifying the Location ERROR and close
        }

        name = (TextView) findViewById(R.id.textedit_change_ort_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_ort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_ort_menu_save) {
            //ToDo save changes and exit activity
            finish();
            return true;
        }
        if (id == R.id.change_ort_menu_delete) {
            //ToDo delete location and exit activity
            indoorService.getLocationManagement().removeOrt(location);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!bound) {
            // Bind to Service
            Intent intent = new Intent(this, IndoorService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onPause() {
        super.onStop();

        // unbind from service
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            IndoorService.IndoorBinder binder = (IndoorService.IndoorBinder) service;
            indoorService = binder.getService();
            bound = true;

            location = indoorService.getOrt(locationID);
            name.setText(location.getName());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (location instanceof Room) {
                fragment = new RoomDetectionFragment();
                transaction.replace(R.id.change_ort_fragment_container, fragment);
                transaction.commit();

                RoomDetectionFragment raumFragment = (RoomDetectionFragment) fragment;
                Room room = (Room) location;
                raumFragment.setBeacons(room.getBeacons());
            } else if (location instanceof NfcSpot) {
                fragment = new NfcDetectionFragment();
                transaction.replace(R.id.change_ort_fragment_container, fragment);
                transaction.commit();

                NfcDetectionFragment nfcFragment = (NfcDetectionFragment) fragment;
                NfcSpot nfcSpot = (NfcSpot) location;
                nfcFragment.setNfcSensor(nfcSpot.getNfcSensor());
            } else if (location instanceof WifiEnvironment) {
                fragment = new WifiDetectionFragment();
                transaction.replace(R.id.change_ort_fragment_container, fragment);
                transaction.commit();

                WifiDetectionFragment wifiFragment = (WifiDetectionFragment) fragment;
                WifiEnvironment wifiEnvironment = (WifiEnvironment) location;
                wifiFragment.setWifiList(wifiEnvironment.getWifis());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public IndoorService getIndoorService() {
        return indoorService;
    }
}
