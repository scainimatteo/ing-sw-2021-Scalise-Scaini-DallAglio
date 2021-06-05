package it.polimi.ingsw.model.card;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class WhiteMarblesAbility extends LeaderAbility {
	private Resource resource_type;
	private static final long serialVersionUID = 97L;
	
	public WhiteMarblesAbility (Resource bonus){
		if (bonus != Resource.FAITH){
			resource_type = bonus;
		}
		else {throw new IllegalArgumentException();}
	}
	
	public Resource getResourceType(){
		return resource_type;
	}

	/**
	* Checks if the target ability is WhiteMarblesAbility type through overloading of the original method
	*
	* @param target allows the ability to run the overloaded method if the type is correct, else the inherited superclass method is run
	* @return true, as it highlights that target ability is a WhiteMarblesAbility type else the inherited superclass method would be run
	*/ 
	public boolean checkAbility (WhiteMarblesAbility target){
		return true;
	}

	@Override
	public String toString(){
		return "|                 |\n|                 |\n|                 |\n" + "|     " + resource_type.printNullMarble() + " ==> " + resource_type.printMarble() + "     |\n|                 |\n|                 |\n|                 |\n";
	}
}
