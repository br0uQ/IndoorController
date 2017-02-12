package de.jschmucker.indoorcontroller.model.ort.detections.wifidetection;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.Ort;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class WifiUmgebung extends Ort {

    private ArrayList<WifiSensor> wifiSensors;

    /**
     * @param name
     * @param sensoren sensoren
     */
    public WifiUmgebung(String name, ArrayList<WifiSensor> sensoren) {
        wifiSensors = sensoren;
        this.name = name;
    }

    public ArrayList<WifiSensor> getWifis() {
        return wifiSensors;
    }

    static String dataToString(WifiUmgebung wifiUmgebung) {
        StringBuilder sb = new StringBuilder();

        sb.append(wifiUmgebung.getName());
        sb.append("\n");

        for (WifiSensor sensor : wifiUmgebung.getWifis()) {
            sb.append(sensor.getSsid());
            sb.append("\n");
            sb.append(sensor.getBssid());
            sb.append("\n");
        }

        return sb.toString();
    }

    static WifiUmgebung stringToData(String data) {
        ArrayList<WifiSensor> sensors = new ArrayList<>();
        String name;

        String[] dataArray = data.split("\\n");
        name = dataArray[0];
        for (int i = 1; i < dataArray.length - 1; i = i + 2) {
            WifiSensor sensor = new WifiSensor(dataArray[i], dataArray[i + 1]);
            sensors.add(sensor);
        }
        return new WifiUmgebung(name, sensors);
    }
}//end WifiUmgebung