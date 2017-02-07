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
}//end NFCSpot