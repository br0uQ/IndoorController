package de.jschmucker.indoorcontroller.model.ort;

import de.jschmucker.indoorcontroller.model.ort.sensor.WifiSensor;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiUmgebung extends Ort {

	private WifiSensor[] wifiSensors;

	/**
	 * 
	 * @param name
	 * @param sensoren    sensoren
	 */
	public WifiUmgebung(String name, WifiSensor[] sensoren){
		wifiSensors = sensoren;
		this.name = name;
	}

	public WifiSensor[] getWifis() {
		return wifiSensors;
	}
}//end WifiUmgebung