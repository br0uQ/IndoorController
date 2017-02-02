package de.jschmucker.indoorcontroller.model.ort;

import android.location.Location;

/**
 * Created by jschmucker on 01.02.17.
 */

public abstract class LocationDetection {
    protected LocationDetectionFragment fragment;

    public LocationDetectionFragment getFragment() {
        return fragment;
    }

    public abstract String getDetectionName();
    public abstract Ort createLocation();
}
