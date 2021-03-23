package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class DiscountAbility implements LeaderAbility {
	private Resource discounted_resource;	

	public DiscountAbility (Resource discount){
		if (discount != Resource.FAITH){
		discounted_resource = discount;
		}
		//TODO: decidere cosa fare in caso di argomento invalido
	}

	public Resource getDiscountedResource(){
		return discounted_resource;
	}	
}
