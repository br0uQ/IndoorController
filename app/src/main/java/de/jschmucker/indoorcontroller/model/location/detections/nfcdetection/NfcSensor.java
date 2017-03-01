package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;

/**
 * A NfcSensor is a class that contains the information of a NFC tag so tags can be compared
 * @author jschmucker
 * @version 1.0
 */
public class NfcSensor {
	private final String serialNumber;

    /**
     * Create a new NfcSensor with the given serial number
     * @param serialNumber Serial number of the NFC tag
     */
	public NfcSensor(String serialNumber){
        this.serialNumber = serialNumber;
	}

    /**
     * @return Serial number of the NFC tag this sensor belongs to
     */
    public String getSerialNumber() {
        return serialNumber;
    }
}//end NfcSensor