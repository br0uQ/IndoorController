package de.jschmucker.indoorcontroller.model.ort;

import android.content.Context;
import android.content.Intent;
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
public class OrtsManagement implements Observer {
    private final String TAG = getClass().getSimpleName();

	private Context context;
	private ArrayList<Ort> orte;
	private LocationDetection[] detections;

	public OrtsManagement(Context context){
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
	 * @param ort    ort
	 */
	public void addOrt(Ort ort){
		orte.add(ort);
        ort.addObserver(this);
	}

	public ArrayList<Ort> getOrte(){
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

        for (Ort ort : orte) {
            //ort.addObserver(this);
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

    public void removeOrt(Ort ort) {
        orte.remove(ort);
    }
}//end OrtsManagement