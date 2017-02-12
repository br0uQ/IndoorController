package de.jschmucker.indoorcontroller.model.ort.sensor;


import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection.NfcSensor;
import de.jschmucker.indoorcontroller.model.ort.detections.roomdetection.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.detections.wifidetection.WifiSensor;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class SensorManagement {

	private ArrayList<BeaconSensor> beaconSensors;
	private ArrayList<NfcSensor> nFCSensors;
	private ArrayList<WifiSensor> wifiSensors;

	public SensorManagement(){

	}

	public ArrayList<WifiSensor> getWifiSensors(){
		return wifiSensors;
	}

	public ArrayList<BeaconSensor> getBeaconSensors() {
		return beaconSensors;
	}

	public ArrayList<NfcSensor> getNFCSensors() {
		return nFCSensors;
	}
}//end SensorManagement