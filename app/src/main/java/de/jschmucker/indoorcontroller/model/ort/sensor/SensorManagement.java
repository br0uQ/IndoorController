package de.jschmucker.indoorcontroller.model.ort.sensor;


import java.util.ArrayList;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class SensorManagement {

	private ArrayList<BeaconSensor> beaconSensors;
	private ArrayList<NFCSensor> nFCSensors;
	private ArrayList<WifiSensor> wifiSensors;

	public SensorManagement(){

	}

	public ArrayList<WifiSensor> getWifiSensors(){
		return wifiSensors;
	}

	public ArrayList<BeaconSensor> getBeaconSensors() {
		return beaconSensors;
	}

	public ArrayList<NFCSensor> getNFCSensors() {
		return nFCSensors;
	}
}//end SensorManagement