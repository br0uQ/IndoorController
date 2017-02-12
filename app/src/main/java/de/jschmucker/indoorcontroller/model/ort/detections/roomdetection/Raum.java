package de.jschmucker.indoorcontroller.model.ort.detections.roomdetection;

import de.jschmucker.indoorcontroller.model.ort.Ort;

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

	static String dataToString(Raum raum) {
		StringBuilder sb = new StringBuilder();

		sb.append(raum.getName());
		sb.append("\n");

		for (BeaconSensor sensor : raum.getBeacons()) {
			// ToDo: Save real sensor data
			sb.append("BeaconSensor");
			sb.append("\n");
		}

		return sb.toString();
	}

	static Raum stringToData(String data) {
		String[] dataArray = data.split("\n");
		String name = dataArray[0];
		BeaconSensor[] sensors = new BeaconSensor[dataArray.length];

		for (int i = 1; i < dataArray.length; i++) {
			// ToDo: load real sensor data
			sensors[i - 1] = new BeaconSensor();
		}

		return new Raum(name, sensors);
	}
}//end Raum