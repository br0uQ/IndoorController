package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;

/**
 * WifiSensor that belongs to one wifi access point
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiSensor {
	private final String ssid;
	private final String bssid;

    /**
     * Create a new wifi sensor with the given SSID and BSSID
     * @param ssid
     * @param bssid
     */
	public WifiSensor(String ssid, String bssid) {
		this.ssid = ssid;
		this.bssid = bssid;
	}

    /**
     * @return The BSSID of this WifiSensor
     */
	public String getBssid() {
		return bssid;
	}

    /**
     * @return The SSID of this WifiSensor
     */
	public String getSsid() {
		return ssid;
	}
}//end WifiSensor