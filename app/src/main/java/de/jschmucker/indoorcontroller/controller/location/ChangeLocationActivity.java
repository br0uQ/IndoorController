package de.jschmucker.indoorcontroller.controller.location;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.IndoorService;
import de.jschmucker.indoorcontroller.model.IndoorServiceProvider;
import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.location.LocationDetection;

public class ChangeLocationActivity extends AppCompatActivity
        implements IndoorServiceBound, Observer {
    public static final String LOCATION_ID = "LOCATION_ID";
    private Location location;
    private int locationID;
    private LocationDetection detection;

    private TextView name;
    private Fragment fragment;

    private IndoorServiceProvider indoorServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        locationID = intent.getIntExtra(LOCATION_ID, -1);
        if (locationID == -1) {
            //ToDo: Opened ChangeLocationActivity without specifying the Location ERROR and close
        }

        name = (TextView) findViewById(R.id.textedit_change_ort_name);

        indoorServiceProvider = new IndoorServiceProvider();
        indoorServiceProvider.addObserver(this);
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
            String locationName = name.getText().toString();
            if (indoorServiceProvider.getIndoorService().isLocationNameAvailable(locationName)) {
                detection.saveLocationValues(location);
                location.setName(locationName);

                finish();
            }
            return true;
        }
        if (id == R.id.change_ort_menu_delete) {
            indoorServiceProvider.getIndoorService().getLocationManagement().removeOrt(location);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        indoorServiceProvider.connectToService(this);
    }

    @Override
    protected void onPause() {
        super.onStop();

        // unbind from service
        indoorServiceProvider.disconnectFromService(this);
    }

    @Override
    public IndoorService getIndoorService() {
        return indoorServiceProvider.getIndoorService();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (((int) arg) == IndoorServiceProvider.CONNECTED) {
            IndoorService indoorService = indoorServiceProvider.getIndoorService();
            location = indoorService.getOrt(locationID);
            detection = indoorService.getLocationDetection(location);

            name.setText(location.getName());

            fragment = detection.getFragment();
            detection.setLocationValues(location);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.change_ort_fragment_container, fragment);
            transaction.commit();

        } else if (((int) arg) == IndoorServiceProvider.NOT_CONNECTED) {

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (detection != null) {
            detection.handleNewIntent(intent);
        }
    }
}
