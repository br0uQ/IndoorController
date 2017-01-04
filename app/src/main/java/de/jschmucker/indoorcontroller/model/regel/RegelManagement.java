package de.jschmucker.indoorcontroller.model.regel;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.steuerung.Steuerung;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:20
 */
public class RegelManagement {

	private ArrayList<Ortsregel> regeln;
	private Steuerung steuerung;

	public RegelManagement(){

	}

	/**
	 * 
	 * @param regel    regel
	 */
	public void addRule(Ortsregel regel){
		regeln.add(regel);
	}
}//end RegelManagement