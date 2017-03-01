package de.jschmucker.indoorcontroller.model.actions;


import android.content.Context;

/**
 * An action is something that can be executed from a task when the location list in that task is true.
 * @see de.jschmucker.indoorcontroller.model.task.Task
 * @author jschmucker
 */
public abstract class Action {
    /**
     * The name of the action.
     */
	protected String name;

    /**
     * Execute the action.
     * @param context
     */
	public abstract void execute(Context context);

    /**
     * @return Name of the action
     */
	public String getName() {
		return name;
	}
}