package de.jschmucker.indoorcontroller.model.ort.sensor;


/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiSensor extends LocationSensor {
	private String ssid;

	public WifiSensor(){

	}

	public String getSsid() {
		return ssid;
	}
}//end WifiSensor