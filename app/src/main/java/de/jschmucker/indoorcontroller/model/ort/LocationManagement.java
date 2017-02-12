package de.jschmucker.indoorcontroller.model.ort;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NfcDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.RoomDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiDetection;

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
}//end LocationManagement