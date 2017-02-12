package de.jschmucker.indoorcontroller.model.location.detections.wifidetection;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.location.Location;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiEnvironment extends Location {

    private ArrayList<WifiSensor> wifiSensors;

    /**
     * @param name
     * @param sensoren sensoren
     */
    public WifiEnvironment(String name, ArrayList<WifiSensor> sensoren) {
        wifiSensors = sensoren;
        this.name = name;
    }

    public ArrayList<WifiSensor> getWifis() {
        return wifiSensors;
    }

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
}//end WifiEnvironment