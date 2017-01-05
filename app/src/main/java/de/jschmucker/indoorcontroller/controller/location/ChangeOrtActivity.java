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
import de.jschmucker.indoorcontroller.model.ort.NFCSpot;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.Raum;
import de.jschmucker.indoorcontroller.model.ort.WifiUmgebung;
import de.jschmucker.indoorcontroller.model.ort.sensor.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.NFCSensor;

public class ChangeOrtActivity extends AppCompatActivity implements IndoorServiceProvider {
    public static final String LOCATION_ID = "LOCATION_ID";
    private Ort ort;
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
            //ToDo: Opened ChangeOrtActivity without specifying the Location ERROR and close
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
            return true;
        }
        if (id == R.id.change_ort_menu_delete) {
            //ToDo delete location and exit activity
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

            ort = indoorService.getOrt(locationID);
            name.setText(ort.getName());
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (ort instanceof Raum) {
                fragment = new CreateOrtRaumFragment();
                transaction.replace(R.id.change_ort_fragment_container, fragment);
                transaction.commit();

                CreateOrtRaumFragment raumFragment = (CreateOrtRaumFragment) fragment;
                Raum raum = (Raum) ort;
                raumFragment.setBeacons(raum.getBeacons());
            } else if (ort instanceof NFCSpot) {
                fragment = new CreateOrtNfcFragment();
                transaction.replace(R.id.change_ort_fragment_container, fragment);
                transaction.commit();

                CreateOrtNfcFragment nfcFragment = (CreateOrtNfcFragment) fragment;
                NFCSpot nfcSpot = (NFCSpot) ort;
                nfcFragment.setNfcSensor(nfcSpot.getNfcSensor());
            } else if (ort instanceof WifiUmgebung) {
                fragment = new CreateOrtWifiFragment();
                transaction.replace(R.id.change_ort_fragment_container, fragment);
                transaction.commit();

                CreateOrtWifiFragment wifiFragment = (CreateOrtWifiFragment) fragment;
                WifiUmgebung wifiUmgebung = (WifiUmgebung) ort;
                wifiFragment.setWifiList(wifiUmgebung.getWifis());
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
