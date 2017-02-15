package de.jschmucker.indoorcontroller.model.location;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.R;
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
	private ArrayList<Location> orte;
	private LocationDetection[] detections;

	public LocationManagement(Context context){
		this.context = context;
        orte = new ArrayList<>();

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
		orte.add(location);
        location.addObserver(this);
	}

	public ArrayList<Location> getOrte(){
		return orte;
	}

    @Override
    public void update(Observable o, Object arg) {
        Log.d(TAG, "update");
    }

    public void loadLocations() {
        for (LocationDetection detection : detections) {
            detection.loadLoactions(orte);
        }
    }

    public void startDetection() {
        for (LocationDetection detection : detections) {
            detection.startDetection(orte);
        }
    }

    public void saveLocations() {
        for (LocationDetection detection : detections) {
            detection.saveLocations(orte);
            detection.stopDetection();
        }
    }

    public void stopDetection() {

    }

    public void removeOrt(Location location) {
        orte.remove(location);
    }

    public int getLocationImage(Location location) {
        for (LocationDetection detection : detections) {
            if (detection.isDetectionOfLocation(location)) {
                return detection.getLocationImage();
            }
        }
        return R.drawable.ic_my_location_black_24dp;
    }

    public boolean isNameAvailable(String name) {
        for (Location location : orte) {
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
}//end LocationManagement