package de.jschmucker.indoorcontroller.model.task;

import de.jschmucker.indoorcontroller.model.ort.Location;
import de.jschmucker.indoorcontroller.model.steuerung.Action;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class Task {

	private Action[] actions;
	private String name;
	private boolean enabled;

	/**
	 * 
	 * @param name
	 * @param actions    actions
	 */
	public Task(String name, Action[] actions){
		this.name = name;
		this.actions = actions;
		enabled = true;
	}

	public String getName() {
		return name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Action[] getActions() {
		return actions;
	}

	public Location[] getLocations() {
		//ToDo Orte Eigenschaten klären
		return null;
	}
}//end Task