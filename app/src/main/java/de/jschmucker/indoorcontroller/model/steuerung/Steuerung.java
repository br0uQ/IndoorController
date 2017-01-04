package de.jschmucker.indoorcontroller.model.steuerung;


import java.util.ArrayList;

/**
 * @author joshua
 * @version 1.0
 * @created 13-Dez-2016 15:33:51
 */
public class Steuerung {

	public ArrayList<Action> actions;



	public void finalize() throws Throwable {

	}
	public Steuerung(){
		actions = new ArrayList<>();
	}

	public ArrayList<Action> getActions(){
		return actions;
	}
}//end Steuerung