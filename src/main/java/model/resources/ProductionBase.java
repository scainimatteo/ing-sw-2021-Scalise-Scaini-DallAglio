package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class ProductionBase extends Production {

	public ProductionBase (Resource[] required, Resource[] produced) {
		super(required, produced);
	}

	// public void setRequirements(Resource[] new_requirements) {
	// 	super.required_resource = new_requirements;
	// }

	// public void setProducts(Resource new_product) {
	// 	super.produced_resource = new_product;
	// }
	
	public Resource activateProduction (Resource produced) {
		return produced;
	}
}
