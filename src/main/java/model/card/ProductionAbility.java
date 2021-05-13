package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.ProductionInterface;

public class ProductionAbility extends LeaderAbility implements ProductionInterface {
	protected static final long serialVersionUID = 7L;
	protected Production production;
	
	public ProductionAbility (Resource[] required, Resource[] produced){
		this.production = new Production(required, produced);
	}

	public Production getProduction(){
		return this.production;
	}

	public Resource[] getRequiredResources(){
		return this.production.getRequiredResources();
	}

	public Resource[] getProducedResources(){
		return this.production.getProducedResources();
	}

	public void setRequiredResources(Resource[] cost){
		return;
	}
	
	public void setProducedResources(Resource[] new_resource) {
		Resource[] new_product = this.production.getProducedResources();
		new_product[0] = new_resource[0];
		this.production = new Production(this.production.getRequiredResources(), new_product);
	}

	/**
	* Checks if the target ability is ProductionAbility type through overloading of the original method
	*
	* @param target allows the ability to run the overloaded method if the type is correct, else the inherited superclass method is run
	* @return true, as it highlights that target ability is a ProductionAbility type else the inherited superclass method would be run
	*/ 
	public boolean checkAbility (ProductionAbility target){
		return true;
	}

	public Resource[] activateProduction(){
		return this.production.activateProduction();
	}

	@Override
	public String printText(){
		return production.printText();
	}
}
