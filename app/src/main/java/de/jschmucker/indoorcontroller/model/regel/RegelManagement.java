package de.jschmucker.indoorcontroller.model.regel;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.ort.Eigenschaft;
import de.jschmucker.indoorcontroller.model.steuerung.Action;
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
		regeln = new ArrayList<>();
		addRule(new Ortsregel("TestRegel", new Eigenschaft[]{}, new Action[]{}));
	}

	/**
	 * 
	 * @param regel    regel
	 */
	public void addRule(Ortsregel regel){
		regeln.add(regel);
	}

	public ArrayList<Ortsregel> getRegeln() {
		return regeln;
	}
}//end RegelManagement