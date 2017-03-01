package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * NfcSpot is a location that can be set active by detecting the NFC tag that NfcSpot belongs to
 * @author jschmucker
 * @version 1.0
 */
class NfcSpot extends Location {

	private NfcSensor nFCSensor;

    /**
     * Create a new NfcSpot with the location name and the given NfcSensor
     * @param name Name of the NfcSpot
     * @param sensor NfcSensor that belongs to the NFC tag this NfcSpot shall respond to
     */
	public NfcSpot(String name, NfcSensor sensor){
		this.name = name;
		nFCSensor = sensor;
		active = false;
		imageResourceId = R.drawable.ic_nfc_spot_white24dp;
	}

    /**
     * @return The NfcSensor of this NfcSpot
     */
	public NfcSensor getNfcSensor() {
		return  nFCSensor;
	}

    /**
     * Converts the data of a NfcSpot into a String
     * @param nfcSpot NfcSpot that shall be converted into a String
     * @return Parsed String of the NfcSpot
     */
	static String dataToString(NfcSpot nfcSpot) {
		StringBuilder sb = new StringBuilder();

		sb.append(nfcSpot.getName());
		sb.append("\n");

		sb.append(nfcSpot.getNfcSensor().getSerialNumber());
		sb.append("\n");

		return sb.toString();
	}

    /**
     * Creates a NfcSpot with the data given in form of a String
     * @param data String that contains all the information of the NfcSpot
     * @return The created NfcSpot with the information of the data String
     */
	static NfcSpot stringToData(String data) {
		String[] dataArray = data.split("\n");
		String name = dataArray[0];

		NfcSensor sensor = new NfcSensor(dataArray[1]);

		return new NfcSpot(name, sensor);
	}

    /**
     * Set the NfcSensor of this NfcSpot
     * @param nfcSensor The new NfcSensor to be set
     */
	public void setNfcSensor(NfcSensor nfcSensor) {
		nFCSensor = nfcSensor;
	}
}//end NfcSpot