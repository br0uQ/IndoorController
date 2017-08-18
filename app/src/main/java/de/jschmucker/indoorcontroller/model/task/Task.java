package de.jschmucker.indoorcontroller.model.task;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import de.jschmucker.indoorcontroller.model.location.Location;
import de.jschmucker.indoorcontroller.model.actions.Action;

/**
 * A Task is defined by a list of Locations and whether they should if active (true) or
 * inactive (false) for this task and a list of actions that should be executed if all the locations
 * are so.
 * @author jschmucker
 * @version 1.0
 */
public class Task implements Observer {

	private ArrayList<Action> actions;
	private String name;
	private boolean enabled;
    private Map<Location, Boolean> locations;
    private final Context context;

    /**
     * Create a new Task with the given parameters.
     * @param context
     * @param name The name of the Task
     * @param locations The list of the locations for this task and whether they should be active (true)
     * @param actions The list of actions to be executed by this task if locations are active/inactive
     */
	public Task(Context context, String name, Map<Location, Boolean> locations, ArrayList<Action> actions){
		this.name = name;
		this.actions = actions;
        this.context = context;
		enabled = true;

        Set<Location> ls = locations.keySet();
        for (Location location : ls) {
            location.addObserver(this);
        }
        this.locations = locations;
	}

    /**
     * @return The name of this task
     */
	public String getName() {
		return name;
	}

    /**
     * Enables or disables the Task depending on the parameter enabled.
     * @param enabled
     */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

    /**
     * @return True if the Task is enabled, false if not
     */
	public boolean isEnabled() {
		return enabled;
	}

    /**
     * @return The list of actions to be executed by this Task
     */
	public ArrayList<Action> getActions() {
		return actions;
	}

    /**
     * @return The list of locations and whether they should be active (true) or inactive (false)
     */
	public Map<Location, Boolean> getLocations() {
		return locations;
	}

    @Override
    public void update(Observable o, Object arg) {
        Log.d(getClass().getSimpleName(), "Task \"" + name + "\": update");
		if (enabled) {
			boolean executeAction = true;
			Set<Location> ls = locations.keySet();
			for (Location location : ls) {
				if (locations.get(location) != location.isActive()) {
					executeAction = false;
				}
			}
			if (executeAction) {
				for (Action action : actions) {
					action.execute(context);
				}
			}
		}
    }

    /**
     * Changes the name of the Task to the given parameter.
     * @param name
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * Changes the Locations list to the given parameter.
     * @param locationBooleanMap
     */
    public void setLocations(Map<Location, Boolean> locationBooleanMap) {
        locations = locationBooleanMap;
    }

    /**
     * Changes the Actions list to the given parameter.
     * @param actions
     */
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
}//end Task