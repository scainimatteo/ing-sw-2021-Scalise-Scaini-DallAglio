package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class ProductionAbility extends Production implements LeaderAbility {
		
	public ProductionAbility (Resource[] required, Resource[] produced){
		super(required, produced);
	}

	public void setProducts(Resource new_product) {
		this.produced_resources[0] = new_product;
	}
}
