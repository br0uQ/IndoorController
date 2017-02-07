package de.jschmucker.indoorcontroller.model.regel;

import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.steuerung.Action;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class Ortsregel {

	private Action[] actions;
	private String name;
	private boolean enabled;

	/**
	 * 
	 * @param name
	 * @param actions    actions
	 */
	public Ortsregel(String name, Action[] actions){
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

	public Ort[] getLocations() {
		//ToDo Orte Eigenschaten klären
		return null;
	}
}//end Ortsregel