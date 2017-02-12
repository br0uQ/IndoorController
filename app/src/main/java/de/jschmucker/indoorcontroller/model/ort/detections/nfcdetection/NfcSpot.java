package de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection;

import de.jschmucker.indoorcontroller.model.ort.Location;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class NfcSpot extends Location {

	private NfcSensor nFCSensor;

	public NfcSpot(String name, NfcSensor sensor){
		this.name = name;
		nFCSensor = sensor;
	}

	public NfcSensor getNfcSensor() {
		return  nFCSensor;
	}

	static String dataToString(NfcSpot nfcSpot) {
		StringBuilder sb = new StringBuilder();

		sb.append(nfcSpot.getName());
		sb.append("\n");

		// ToDo: save real nfc sensor data
		sb.append(nfcSpot.getNfcSensor().toString());
		sb.append("\n");

		return sb.toString();
	}

	static NfcSpot stringToData(String data) {
		String[] dataArray = data.split("\n");
		String name = dataArray[0];

		// ToDo: load real nfc sensor data
		NfcSensor sensor = new NfcSensor();

		return new NfcSpot(name, sensor);
	}
}//end NfcSpot