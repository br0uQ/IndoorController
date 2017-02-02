package de.jschmucker.indoorcontroller.model.ort.sensor;


/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiSensor extends LocationSensor {
	private String ssid;
	private String bssid;

	public WifiSensor(){

	}

	public String getSsid() {
		return ssid;
	}

	public String getBssid() {
		return bssid;
	}
}//end WifiSensor