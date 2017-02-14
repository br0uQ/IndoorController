package de.jschmucker.indoorcontroller.model.actions;


import android.content.Context;

/**
 * @author jschmucker
 * @version 1.0
 * @created 13-Dez-2016 15:33:50
 */
public abstract class Action {
	protected String name;

	public abstract void execute(Context context);

	public String getName() {
		return name;
	}
}