package de.jschmucker.indoorcontroller.model.location.detections.nfcdetection;


import de.jschmucker.indoorcontroller.model.location.sensor.LocationSensor;

/**
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class NfcSensor extends LocationSensor {
	private String serialNumber;

	public NfcSensor(String serialNumber){
        this.serialNumber = serialNumber;
	}

    public String getSerialNumber() {
        return serialNumber;
    }
}//end NfcSensor