package de.jschmucker.indoorcontroller.model.ort;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.sensor.SensorManagement;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public abstract class Ort {
	protected boolean active;
	private SensorManagement sensorManagement;
	protected String name;

	public Ort(){

	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}
}//end Ort