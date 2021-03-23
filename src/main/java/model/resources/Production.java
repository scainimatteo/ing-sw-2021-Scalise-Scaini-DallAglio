package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.model.resources.Resource;

public class Production {
	protected Resource[] required_resources;
	protected Resource[] produced_resources;

	public Production (Resource[] required, Resource[] produced){
		this.required_resources = required;
		this.produced_resources = produced;
	}

	public Resource[] getRequiredResources () {
		return required_resources;
	}

	public Resource[] getProducedResorces () {
		return produced_resources;
	}

	public Resource[] activateProduction(Resource[] cost) {
		return null;
	}
}
