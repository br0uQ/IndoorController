package de.jschmucker.indoorcontroller.model.steuerung;


import android.content.Context;

/**
 * @author jschmucker
 * @version 1.0
 * @created 13-Dez-2016 15:33:50
 */
public interface Action {

	public void execute(Context context);
	public String toString();
}