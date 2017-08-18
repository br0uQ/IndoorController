package de.jschmucker.indoorcontroller.model.location;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.model.location.detections.nfcdetection.NfcDetection;
import de.jschmucker.indoorcontroller.model.location.detections.roomdetection.RoomDetection;
import de.jschmucker.indoorcontroller.model.location.detections.wifidetection.WifiDetection;

/**
 * The LocationManagement manages the Locations including the LocationDetections.
 * @author jschmucker
 * @version 1.0
 */
public class LocationManagement implements Observer {
    private final String TAG = getClass().getSimpleName();

    private final ArrayList<Location> locations;
	private final LocationDetection[] detections;

    /**
     * Create a new LocationManagement.
     * To add a new LocationDetection add it to the detections array.
     * @param context
     */
	public LocationManagement(Context context){
        locations = new ArrayList<>();

        /**
         * To create add a new LocationDetection add it to this detections array.
         */
		detections = new LocationDetection[] {
				new RoomDetection(context),
				new WifiDetection(context),
				new NfcDetection(context)
		};
	}

    /**
     * @return An array with all LocationDetections
     */
	public LocationDetection[] getLocationDetections() {
		return detections;
	}

	/**
	 * Add a new Location.
	 * @param location The Location to be added
	 */
	public void addLocation(Location location){
		locations.add(location);
        location.addObserver(this);
	}

    /**
     * @return A ArrayList with all Locations
     */
	public ArrayList<Location> getLocations(){
		return locations;
	}

    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "update");
    }

    /**
     * Loads all Locations that are saved with saveLocations().
     * To do that it calls the loadLocations() method in every LocationDetection.
     */
    public void loadLocations() {
        for (LocationDetection detection : detections) {
            detection.loadLocations(locations);
        }
    }

    /**
     * Starts the detection in every LocationDetection
     */
    public void startDetection() {
        for (LocationDetection detection : detections) {
            detection.startDetection(locations);
        }
    }

    /**
     * Saves all Locations.
     * To do that it calls the saveLocations() method in every LocationDetection.
     */
    public void saveLocations() {
        for (LocationDetection detection : detections) {
            detection.saveLocations(locations);
            detection.stopDetection();
        }
    }

    /**
     * Stops the detection in every LocationDetection
     */
    public void stopDetection() {
        for (LocationDetection detection : detections) {
            detection.stopDetection();
        }
    }

    /**
     * Removes the given location from the locations list.
     * @param location
     */
    public void removeLocation(Location location) {
        locations.remove(location);
    }

    /**
     * Checks whether the given Location name is available.
     * @param name Name to check for
     * @return False if another Location has that name, true if not
     */
    public boolean isNameAvailable(String name) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param location
     * @return The LocationDetection that belongs to the given Location
     */
    public LocationDetection getLocationDetection(Location location) {
        for (LocationDetection detection : detections) {
            if (detection.isDetectionOfLocation(location)) {
                return detection;
            }
        }
        return null;
    }

    /**
     * @param name
     * @return The Location with the given name
     */
    public Location getLocation(String name) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }

    /**
     * Will be called by the SettingsActivity. This method calls the reloadSettings() method in
     * every LocationDetection to reload the settings that where changed by the user.
     */
    public void reloadSettings() {
        for (LocationDetection locationDetection : detections) {
            locationDetection.reloadSettings();
        }
    }
}//end LocationManagement