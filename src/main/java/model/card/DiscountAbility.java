package it.polimi.ingsw.model.card;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class DiscountAbility extends LeaderAbility {
	private Resource discounted_resource;	

	public DiscountAbility (Resource discount){
		if (discount != Resource.FAITH){
		discounted_resource = discount;
		}
		else {throw new IllegalArgumentException();}
	}

	public Resource getDiscountedResource(){
		return discounted_resource;
	}	
	
	/**
	* Checks if the target ability is DiscountAbility type through overload of the original method
	*
	* @param target allows the ability to run the overloaded method if the type is correct, else the inherited superclass method is run
	* @return true, as it highlights that target ability is a DiscountAbility type else the inherited superclass method would be run
	*/ 
	public boolean checkAbility (DiscountAbility target){
		return true;
	}
}
