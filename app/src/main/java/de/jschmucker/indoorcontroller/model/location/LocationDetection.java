package de.jschmucker.indoorcontroller.model.location;

import android.content.Intent;

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

    /**
     * Creates a new location and returns it
     * @param name: Name of the new location to create
     * @return: location created with the settings in the fragment
     */
    public abstract Location createLocation(String name);

    /**
     * Saves all locations in the ArrayList that belong to this detection
     * @param orte: Locations to be saved
     */
    public abstract void saveLocations(ArrayList<Location> orte);

    /**
     * Loads all locations that are saved and adds them to the ArrayList
     * @param orte: where the loaded locations should be stored at
     */
    public abstract void loadLoactions(ArrayList<Location> orte);

    public abstract void startDetection(ArrayList<Location> locations);
    public abstract void stopDetection();

    public abstract boolean isDetectionOfLocation(Location location);

    public abstract int getLocationImage();

    public abstract void setLocationValues(Location location);

    public abstract void saveLocationValues(Location location);

    public void handleNewIntent(Intent intent) {
        /* does nothing
        Detection that wants to use onNewIntent() of the Activity
        can override this function
         */
    }
}
