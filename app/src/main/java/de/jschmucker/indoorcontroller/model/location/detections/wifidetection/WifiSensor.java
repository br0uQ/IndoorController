package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;


import de.jschmucker.indoorcontroller.model.location.sensor.LocationSensor;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiSensor extends LocationSensor {
	private String ssid;
	private String bssid;

	public WifiSensor(String ssid, String bssid) {
		this.ssid = ssid;
		this.bssid = bssid;
	}

	public String getBssid() {
		return bssid;
	}

	public String getSsid() {
		return ssid;
	}
}//end WifiSensor