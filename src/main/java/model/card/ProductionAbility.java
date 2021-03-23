package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class ProductionAbility extends Production implements LeaderAbility {
	public void setProducts(Resource new_product) {
		this.produced_resources[0] = new_product;
		return;
	}
//Costruttore per evitare ProductionAbility non esistenti. Orribile. e pure illegale. 
	public ProductionAbility (Resource[] required, Resource[] produced){
		this.produced_resources = new Resource[2];
		this.produced_resources[0] = produced[0];
		this.produced_resources[1] = Resource.FAITH;
		this.required_resources = new Resource[1];
		this.required_resources[0] = required[0];
	}
}
