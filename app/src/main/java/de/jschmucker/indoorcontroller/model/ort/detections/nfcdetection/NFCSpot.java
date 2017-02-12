package de.jschmucker.indoorcontroller.model.ort.detections.nfcdetection;

import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class NFCSpot extends Ort {

	private NFCSensor nFCSensor;

	public NFCSpot(String name, NFCSensor sensor){
		this.name = name;
		nFCSensor = sensor;
	}

	public NFCSensor getNfcSensor() {
		return  nFCSensor;
	}

	static String dataToString(NFCSpot nfcSpot) {
		StringBuilder sb = new StringBuilder();

		sb.append(nfcSpot.getName());
		sb.append("\n");

		// ToDo: save real nfc sensor data
		sb.append(nfcSpot.getNfcSensor().toString());
		sb.append("\n");

		return sb.toString();
	}

	static NFCSpot stringToData(String data) {
		String[] dataArray = data.split("\n");
		String name = dataArray[0];

		// ToDo: load real nfc sensor data
		NFCSensor sensor = new NFCSensor();

		return new NFCSpot(name, sensor);
	}
}//end NFCSpot