package de.jschmucker.indoorcontroller.model.ort;

import android.content.Context;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NFCSpot;
import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NfcDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.Raum;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.RoomDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiDetection;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiUmgebung;
import de.jschmucker.indoorcontroller.model.ort.sensor.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.NFCSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.SensorManagement;
import de.jschmucker.indoorcontroller.model.ort.sensor.WifiSensor;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class OrtsManagement {

	private Context context;
	private ArrayList<Ort> orte;
	private SensorManagement sensorManagement;
	private LocationDetection[] detections;

	public OrtsManagement(Context context){
		this.context = context;
		detections = new LocationDetection[] {
				new RoomDetection(context),
				new WifiDetection(context),
				new NfcDetection(context)
		};
		orte = new ArrayList<>();
		addOrt(new Raum("TestRaum", new BeaconSensor[] {new BeaconSensor(), new BeaconSensor(),
                new BeaconSensor(), new BeaconSensor()}));
		addOrt(new NFCSpot("TestNFCSpot", new NFCSensor()));
		addOrt(new WifiUmgebung("TestWifiUmgebung", new WifiSensor[] {new WifiSensor()}));
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
	}

	public ArrayList<Ort> getOrte(){
		return orte;
	}

	public SensorManagement getSensorManagement(){
		return sensorManagement;
	}
}//end OrtsManagement