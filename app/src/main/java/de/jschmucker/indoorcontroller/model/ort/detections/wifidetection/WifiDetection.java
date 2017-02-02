package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.ort.LocationDetection;
import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.ort.OrtsManagement;

/**
 * Created by joshua on 01.02.17.
 */

public class WifiDetection extends LocationDetection {
    private Context context;

    public WifiDetection(Context context) {
        this.context = context;
        fragment = new CreateOrtWifiFragment();
        name = context.getString(R.string.wifi_detection_name);
    }

    @Override
    public Ort createLocation() {
        // ToDo tell fragment to create Location
        return null;
    }
}
