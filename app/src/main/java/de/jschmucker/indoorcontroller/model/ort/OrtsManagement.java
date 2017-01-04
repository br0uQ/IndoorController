package de.jschmucker.indoorcontroller.model.ort;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.sensor.BeaconSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.LocationSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.NFCSensor;
import de.jschmucker.indoorcontroller.model.ort.sensor.SensorManagement;
import de.jschmucker.indoorcontroller.model.ort.sensor.WifiSensor;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class OrtsManagement {

	private ArrayList<Ort> orte;
	private SensorManagement sensorManagement;
	private String[] ortstypen = new String[] {
			"Raum",
			"NFCSpot",
			"WifiUmgebung"
	};

	public OrtsManagement(){
		orte = new ArrayList<>();
	}

	public String[] getOrtstypen() {
		return ortstypen;
	}

	/**
	 * 
	 * @param ort    ort
	 */
	public void addOrt(Ort ort){
		orte.add(ort);
	}

	public ArrayList<Ort> getOrte(){
		return orte;
	}

	public SensorManagement getSensorManagement(){
		return sensorManagement;
	}
}//end OrtsManagement