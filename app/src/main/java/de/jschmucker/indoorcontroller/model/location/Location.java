package de.jschmucker.indoorcontroller.model.location;

import java.util.Observable;

import de.jschmucker.indoorcontroller.R;

/**
 * A Location is used from the detections.
 * Each Detection uses its own Locations that they are detecting
 * A location has a name, an icon and is active or inactive.
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

    /**
     * @return The name of the location
     */
	public String getName() {
		return name;
	}

    /**
     * @return True if the location is active, false if not
     */
	public boolean isActive() {
		return active;
	}

    /**
     * Set the location active if parameter active is true or inactive if parameter active is false
     * @param active
     */
    public void setActive(boolean active) {
        if (this.active != active) {
            this.active = active;
			setChanged();
            notifyObservers();
        }
    }

    /**
     * Sets the name of the location.
     * @param locationName
     */
	public void setName(String locationName) {
		name = locationName;
	}

    /**
     * Returns a default icon. Each location can override this method to return its own icon.
     * @return The resource id of the locations icon
     */
	public int getImageResourceId() {
		return imageResourceId;
	}
}//end Location