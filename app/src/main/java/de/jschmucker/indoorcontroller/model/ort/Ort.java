package de.jschmucker.indoorcontroller.model.ort;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.sensor.SensorManagement;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public abstract class Ort {

	private Eigenschaft[] eigenschaften;
	private SensorManagement sensorManagement;
	protected String name;

	public Ort(){

	}

	public String getName() {
		return name;
	}

	public Eigenschaft[] getEigenschaften(){
		return eigenschaften;
	}
}//end Ort