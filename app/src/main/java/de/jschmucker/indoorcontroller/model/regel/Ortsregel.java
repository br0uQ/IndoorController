package de.jschmucker.indoorcontroller.model.regel;

import java.util.ArrayList;

import de.jschmucker.indoorcontroller.model.steuerung.Action;
import de.jschmucker.indoorcontroller.model.ort.Eigenschaft;

/**
 * @author joshua
 * @version 1.0
 * @created 06-Dez-2016 14:18:19
 */
public class Ortsregel {

	private Action[] actions;
	private Eigenschaft[] eigenschaften;
	private String name;

	/**
	 * 
	 * @param name
	 * @param eigenschaften
	 * @param actions    actions
	 */
	public Ortsregel(String name, Eigenschaft[] eigenschaften, Action[] actions){
		this.name = name;
		this.eigenschaften = eigenschaften;
		this.actions = actions;
	}
}//end Ortsregel