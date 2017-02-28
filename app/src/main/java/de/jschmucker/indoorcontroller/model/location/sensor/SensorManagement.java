package de.jschmucker.indoorcontroller.model.location.sensor;


import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.location.detections.nfcdetection.NfcSensor;
import de.jschmucker.indoorcontroller.model.location.detections.roomdetection.BeaconSensor;
import de.jschmucker.indoorcontroller.model.location.detections.wifidetection.WifiSensor;

/**
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
class SensorManagement {

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