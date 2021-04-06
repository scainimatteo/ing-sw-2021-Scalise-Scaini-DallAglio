package it.polimi.ingsw.model.card;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class WhiteMarblesAbility implements LeaderAbility {
	private Resource resource_type;
	
	public WhiteMarblesAbility (Resource bonus){
		if (bonus != Resource.FAITH){
			resource_type = bonus;
		}
		else {throw new IllegalArgumentException();}
	}
	
	public Resource getResourceType(){
		return resource_type;
	}

	public void setResourceType(Resource bonus) throws IllegalArgumentException{
		if (bonus != Resource.FAITH){
			resource_type = bonus;
		} else {
		throw new IllegalArgumentException();
		}
	}
}
