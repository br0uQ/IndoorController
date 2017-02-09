package de.jschmucker.indoorcontroller.model.ort;

import java.util.Observable;

import de.jschmucker.indoorcontroller.model.ort.sensor.SensorManagement;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public abstract class Ort  extends Observable {
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

	public void setActive(boolean active) {
		if (this.active != active) {
			this.active = active;
			setChanged();
			notifyObservers();
		}
	}
}//end Ort