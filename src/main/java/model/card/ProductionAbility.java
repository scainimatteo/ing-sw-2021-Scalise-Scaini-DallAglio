package it.polimi.ingsw.model.card;

import java.util.ArrayList;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.ProductionInterface;

public class ProductionAbility extends LeaderAbility implements ProductionInterface {
	protected static final long serialVersionUID = 7L;
	protected Production production;
	
	public ProductionAbility (ArrayList<Resource> required, ArrayList<Resource> produced){
		this.production = new Production(required, produced);
	}

	public Production getProduction(){
		return this.production;
	}

	public ArrayList<Resource> getRequiredResources(){
		return this.production.getRequiredResources();
	}

	public ArrayList<Resource> getProducedResources(){
		return this.production.getProducedResources();
	}

	public void setRequiredResources(ArrayList<Resource> cost){
		return;
	}
	
	public void setProducedResources(ArrayList<Resource> new_resource) {
		ArrayList<Resource> new_product = this.production.getProducedResources();
		new_product.remove(0);
		new_product.add(0, new_resource.get(0));
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

	@Override
	public String toString(){
		String[] production_string = production.productionToString();
		String prod1 = "| " + production_string[0] + "  |\n";
		String prod2 = "| " + production_string[1] + "  |\n";
		String prod3 = "| " + production_string[2] + "  |\n";
		String prod4 = "| " + production_string[3] + "  |\n";
		String prod5 = "| " + production_string[4] + "  |\n";

		return prod1 + prod2 + prod3 + prod4 + prod5 + "|                 |\n|                 |\n"; 
	}
}
