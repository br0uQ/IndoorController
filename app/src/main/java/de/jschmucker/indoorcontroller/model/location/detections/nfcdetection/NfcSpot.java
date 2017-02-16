package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import de.jschmucker.indoorcontroller.model.location.Location;

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

		sb.append(nfcSpot.getNfcSensor().getSerialNumber());
		sb.append("\n");

		return sb.toString();
	}

	static NfcSpot stringToData(String data) {
		String[] dataArray = data.split("\n");
		String name = dataArray[0];

		NfcSensor sensor = new NfcSensor(dataArray[1]);

		return new NfcSpot(name, sensor);
	}

	public void setNfcSensor(NfcSensor nfcSensor) {
		nFCSensor = nfcSensor;
	}
}//end NfcSpot