package de.jschmucker.indoorcontroller.model.task;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.steuerung.Action;
import de.jschmucker.indoorcontroller.model.steuerung.Control;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class TaskManagement {

	private ArrayList<Task> regeln;
	private Control control;

	public TaskManagement(){
		regeln = new ArrayList<>();
		addRule(new Task("TestRegel", new Action[]{}));
	}

	/**
	 * 
	 * @param regel    regel
	 */
	public void addRule(Task regel){
		regeln.add(regel);
	}

	public ArrayList<Task> getRegeln() {
		return regeln;
	}
}//end TaskManagement