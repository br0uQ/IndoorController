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
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class Task implements Observer {

	private ArrayList<Action> actions;
	private String name;
	private boolean enabled;
    private Map<Location, Boolean> locations;
    private Context context;

	/**
	 * 
	 * @param name
	 * @param actions    actions
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

	public String getName() {
		return name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public Location[] getLocations() {
		//ToDo Orte Eigenschaten klären
		return null;
	}

    @Override
    public void update(Observable o, Object arg) {
        Log.d(getClass().getSimpleName(), "Task \"" + name + "\": update");
        Location source = (Location) o;
        if (locations.get(source)) {
            for (Action action : actions) {
                action.execute(context);
            }
        }
    }
}//end Task