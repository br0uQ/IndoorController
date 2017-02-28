package de.jschmucker.indoorcontroller.model.location;

import java.util.Observable;

import de.jschmucker.indoorcontroller.R;

/**
 * @author jschmucker
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public abstract class Location extends Observable {
	protected boolean active;
	protected String name;
	protected int imageResourceId = R.drawable.ic_my_location_black_24dp;

	protected Location(){

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

	public void setName(String locationName) {
		name = locationName;
	}

	public int getImageResourceId() {
		return imageResourceId;
	}
}//end Location