package de.jschmucker.indoorcontroller.model.actions;


import android.content.Context;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.actions.examplecontrol.ExampleActionFragment;

/**
 * The ActionManagement is responsible for creating, saving, loading, storing and offering all the actions.
 * It contains a list with all the possible ActionFragments.
 * @author jschmucker
 * @version 1.0
 * @created 13-Dez-2016 15:33:51
 */
public class ActionManagement {
    private Action action;
    private final ArrayList<Action> actions = new ArrayList<>();

    /**
     * To add a new action category add the action categories ActionFragment to this array.
     */
	private final ActionFragment[] actionFragments = new ActionFragment[] {
			new ExampleActionFragment()
	};

    /**
     * @return An array with all possible ActionFragments
     */
	public ActionFragment[] getActionFragments() {
        return actionFragments;
    }

    /**
     * Use this function to store a new action in the ActionManagement.
     * @param action Action to be stored
     */
    public void addAction(Action action) {
        actions.add(action);
        this.action = action;
    }

    /**
     * This function returns the last stored action when being called the first time after storing an action.
     * Every further time it returns null.
     * @return The last stored action.
     */
    public Action getAction() {
        Action ret = action;
        action = null;
        return ret;
    }

    /**
     * This function checks if an action already has the given name
     * @param name Name that has to be checked to be possible for a new action
     * @return True if no other action has that name and false if there is an action with the given name
     */
    public boolean isNameAvailable(String name) {
        for (Action a : actions) {
            if (a.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return All actions stored in the ActionManagement
     */
    public ArrayList<Action> getActions() {
        return actions;
    }

    /**
     * Loads all actions that have been saved by calling saveActions().
     * @param context
     */
    public void loadActions(Context context) {
        for (ActionFragment af : actionFragments) {
            af.loadActions(context, actions);
        }
    }

    /**
     * Saves all actions.
     * @param context
     */
    public void saveActions(Context context) {
        for (ActionFragment af : actionFragments) {
            af.saveActions(context, actions);
        }
    }

    /**
     * Get an action by its name
     * @param name Name of the action
     * @return Action with the given name. Returns null if there is no action with that name.
     */
    public Action getAction(String name) {
        for (Action action : actions) {
            if (action.getName().equals(name)) {
                return action;
            }
        }
        return null;
    }
}//end Control