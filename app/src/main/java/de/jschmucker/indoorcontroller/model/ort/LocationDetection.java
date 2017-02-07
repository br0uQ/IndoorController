package de.jschmucker.indoorcontroller.model.ort;

import android.location.Location;

import java.util.ArrayList;

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
    public abstract Ort createLocation(String name);
    public abstract void saveLocations(ArrayList<Ort> orte);
    public abstract void loadLoactions(ArrayList<Ort> orte);
}
