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
	private ActionFragment[] actionFragments = new ActionFragment[] {
			new ExampleActionFragment()
	};

	public ActionFragment[] getActionFragments() {
        return actionFragments;
    }

	public void finalize() throws Throwable {

	}

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        Action ret = action;
        action = null;
        return ret;
    }
}//end Control