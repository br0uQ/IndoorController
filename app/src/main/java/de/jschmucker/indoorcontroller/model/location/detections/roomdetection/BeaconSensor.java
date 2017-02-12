package de.jschmucker.indoorcontroller.model.location.detections.roomdetection;


import de.jschmucker.indoorcontroller.model.location.sensor.LocationSensor;

/**
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class BeaconSensor extends LocationSensor {
	public final static int TOP_LEFT = 0;
	public final static int TOP_RIGHT = 1;
	public final static int BOTTOM_LEFT = 2;
	public final static int BOTTOM_RIGHT = 3;

	public BeaconSensor(){

	}

}//end BeaconSensor