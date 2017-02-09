package de.jschmucker.indoorcontroller.model.regel;

import java.util.Observable;
import java.util.Observer;

import de.jschmucker.indoorcontroller.model.ort.Ort;
import de.jschmucker.indoorcontroller.model.steuerung.Action;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class Ortsregel implements Observer {

	private Action[] actions;
	private String name;
	private boolean enabled;
	private Ort[] orte;

	/**
	 * 
	 * @param name
	 * @param actions    actions
	 */
	public Ortsregel(String name, Ort[] orte, Action[] actions){
		this.name = name;
		this.actions = actions;
		enabled = true;
		this.orte = orte;
/*		for (Ort ort : orte) {
			ort.addObserver(this);
		}*/
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

	@Override
	public void update(Observable o, Object arg) {
		Ort ort = (Ort) o;
        if (ort.isActive()) {
            for (Action action : actions) {
                action.execute();
            }
        }
	}
}//end Ortsregel