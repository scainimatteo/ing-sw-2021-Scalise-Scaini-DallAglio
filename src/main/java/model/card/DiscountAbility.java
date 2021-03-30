package it.polimi.ingsw.model.card;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class DiscountAbility implements LeaderAbility {
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
}
