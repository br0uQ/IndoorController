package de.jschmucker.indoorcontroller.model.ort;

import android.location.Location;

/**
 * Created by jschmucker on 01.02.17.
 */

public abstract class LocationDetection {
    protected LocationDetectionFragment fragment;
    protected String name;

    public LocationDetectionFragment getFragment() {
        return fragment;
    }

    public String getDetectionName() {
        return name;
    }
    public abstract Ort createLocation();
}
