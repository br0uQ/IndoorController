package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.R;
import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * A WifiEnvironment is a location defined by the detected WiFis in its environment.
 * The WifiEnvironment contains a list of wifi access points (WifiSensors).
 * If every access point of that list is detected by the device this Location is set active.
 * If not it is set inactive.
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
class WifiEnvironment extends Location {

    private ArrayList<WifiSensor> wifiSensors;

    /**
     * Create a new WifiEnvironment with the given parameters
     * @param name Name of the WifiEnvironment
     * @param sensors List of wifi access points of this WifiEnvironment
     */
    public WifiEnvironment(String name, ArrayList<WifiSensor> sensors) {
        wifiSensors = sensors;
        this.name = name;
        imageResourceId = R.drawable.ic_wifis_white24dp;
    }

    /**
     * @return The wifi access point list of this WifiEnvironment
     */
    public ArrayList<WifiSensor> getWifis() {
        return wifiSensors;
    }

    /**
     * Parse the given WifiEnvironment into a data String.
     * @param wifiEnvironment
     * @return The parsed String
     */
    static String dataToString(WifiEnvironment wifiEnvironment) {
        StringBuilder sb = new StringBuilder();

        sb.append(wifiEnvironment.getName());
        sb.append("\n");

        for (WifiSensor sensor : wifiEnvironment.getWifis()) {
            sb.append(sensor.getSsid());
            sb.append("\n");
            sb.append(sensor.getBssid());
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Create a WifiEnvironment parsed from the given data String
     * @param data
     * @return Created WifiEnvironment parsed from the given data String
     */
    static WifiEnvironment stringToData(String data) {
        ArrayList<WifiSensor> sensors = new ArrayList<>();
        String name;

        String[] dataArray = data.split("\\n");
        name = dataArray[0];
        for (int i = 1; i < dataArray.length - 1; i = i + 2) {
            WifiSensor sensor = new WifiSensor(dataArray[i], dataArray[i + 1]);
            sensors.add(sensor);
        }
        return new WifiEnvironment(name, sensors);
    }

    public void setWifis(ArrayList<WifiSensor> selectedWifis) {
        wifiSensors = selectedWifis;
    }
}//end WifiEnvironment