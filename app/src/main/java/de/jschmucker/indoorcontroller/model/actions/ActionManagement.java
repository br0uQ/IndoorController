package de.jschmucker.indoorcontroller.model.actions;


import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.actions.examplecontrol.ExampleActionFragment;

/**
 * @author joshua
 * @version 1.0
 * @created 13-Dez-2016 15:33:51
 */
public class ActionManagement {
    private Action action;
    private ArrayList<Action> actions = new ArrayList<>();
	private ActionFragment[] actionFragments = new ActionFragment[] {
			new ExampleActionFragment()
	};

	public ActionFragment[] getActionFragments() {
        return actionFragments;
    }

	public void finalize() throws Throwable {

	}

    public void addAction(Action action) {
        actions.add(action);
        this.action = action;
    }

    public Action getAction() {
        Action ret = action;
        action = null;
        return ret;
    }

    public boolean isNameAvailable(String name) {
        for (Action a : actions) {
            if (a.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }
}//end Control