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
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class LocationManagement implements Observer {
    private final String TAG = getClass().getSimpleName();

	private Context context;
	private ArrayList<Location> locations;
	private LocationDetection[] detections;

	public LocationManagement(Context context){
		this.context = context;
        locations = new ArrayList<>();

		detections = new LocationDetection[] {
				new RoomDetection(context),
				new WifiDetection(context),
				new NfcDetection(context)
		};
	}

	public LocationDetection[] getLocationDetections() {
		return detections;
	}

	/**
	 * 
	 * @param location    location
	 */
	public void addOrt(Location location){
		locations.add(location);
        location.addObserver(this);
	}

	public ArrayList<Location> getLocations(){
		return locations;
	}

    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "update");
    }

    public void loadLocations() {
        for (LocationDetection detection : detections) {
            detection.loadLoactions(locations);
        }
    }

    public void startDetection() {
        for (LocationDetection detection : detections) {
            detection.startDetection(locations);
        }
    }

    public void saveLocations() {
        for (LocationDetection detection : detections) {
            detection.saveLocations(locations);
            detection.stopDetection();
        }
    }

    public void stopDetection() {

    }

    public void removeOrt(Location location) {
        locations.remove(location);
    }

    public boolean isNameAvailable(String name) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public LocationDetection getLocationDetection(Location location) {
        for (LocationDetection detection : detections) {
            if (detection.isDetectionOfLocation(location)) {
                return detection;
            }
        }
        return null;
    }

    public Location getLocation(String name) {
        for (Location location : locations) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        return null;
    }
}//end LocationManagement