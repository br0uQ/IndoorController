package de.jschmucker.indoorcontroller.model.location;

import java.util.Observable;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public abstract class Location extends Observable {
	protected boolean active;
	protected String name;

	public Location(){

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
}//end Location