package de.jschmucker.indoorcontroller.model.ort;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.sensor.BeaconSensor;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class Raum extends Ort {

	private BeaconSensor[] beaconSensors;

	public Raum(String name, BeaconSensor[] sensors){
		this.name = name;
		/* beaconSensors = new BeaconSensor[4];
		for (int i = 0; i < sensors.length; i++) {
			beaconSensors[i] = sensors[i];
		} */
		beaconSensors = sensors;
	}


	public BeaconSensor[] getBeacons() {
		return beaconSensors;
	}
}//end Raum