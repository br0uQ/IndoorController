package de.jschmucker.indoorcontroller.model.location;

import android.content.Intent;

import java.util.ArrayList;

/**
 * A LocationDetection can create its own locations.
 * Created by jschmucker on 01.02.17.
 */
public abstract class LocationDetection {
    protected LocationDetectionFragment fragment;
    protected String name;

    /**
     * @return A fragment that contains the needed GUI elements to create a new location or change an existing one
     */
    public LocationDetectionFragment getFragment() {
        return fragment;
    }

    /**
     * @return The name of the Detection type
     */
    public String getDetectionName() {
        return name;
    }

    /**
     * Creates a new Location and returns it
     * @param name Name of the new Location to create
     * @return: Location created with the settings in the fragment
     */
    public abstract Location createLocation(String name);

    /**
     * Saves all Locations in the ArrayList that belong to this detection type
     * @param locations Locations to be saved
     */
    public abstract void saveLocations(ArrayList<Location> locations);

    /**
     * Loads all locations that are saved and adds them to the ArrayList
     * @param locations where the loaded locations should be stored at
     */
    public abstract void loadLocations(ArrayList<Location> locations);

    /**
     * Starts the detection for the given locations.
     * @param locations
     */
    public abstract void startDetection(ArrayList<Location> locations);

    /**
     * Stops the detection
     */
    public abstract void stopDetection();

    /**
     * Check whether the given Location belongs to this detection type
     * @param location
     * @return True if location is type of the detection
     */
    public abstract boolean isDetectionOfLocation(Location location);

    /**
     * Set the needed values of the given location in the fragment.
     * This is used when the fragment is used to change a Locations parameters.
     * @param location
     */
    public abstract void setLocationValues(Location location);

    /**
     * Save the new parameters from the fragment in the given Location.
     * This is used when the fragment is used to change a Locations parameters.
     * @param location
     */
    public abstract void saveLocationValues(Location location);

    /**
     * This method is called when the onNewIntent() method of the Activity, that the
     * LocationDetectionFragment is attached to, is called.
     * A LocationDetection can override this method to use the onNewIntent() method of the Activity.
     * @param intent The intent parameter from the onNewIntent() call of the Activity
     */
    public void handleNewIntent(Intent intent) {
        /* does nothing
        Detection that wants to use onNewIntent() of the Activity
        can override this function
         */
    }

    /**
     * @return The Preferences resource id for this LocationDetection
     */
    public abstract int getPreferenceResource();

    /**
     * This is called by the SettingsActivity after preferences where changed.
     */
    public abstract void reloadSettings();
}
