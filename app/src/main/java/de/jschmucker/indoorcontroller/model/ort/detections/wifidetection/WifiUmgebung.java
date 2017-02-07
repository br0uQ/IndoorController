package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiUmgebung extends Ort {

	private ArrayList<WifiSensor> wifiSensors;

	/**
	 * 
	 * @param name
	 * @param sensoren    sensoren
	 */
	public WifiUmgebung(String name, ArrayList<WifiSensor> sensoren){
		wifiSensors = sensoren;
		this.name = name;
	}

	public ArrayList<WifiSensor> getWifis() {
		return wifiSensors;
	}
}//end WifiUmgebung