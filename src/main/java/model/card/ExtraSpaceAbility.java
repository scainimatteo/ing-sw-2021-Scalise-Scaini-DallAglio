package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class ExtraSpaceAbility implements LeaderAbility {
	private Resource resource_type;
	private Resource[] store_resources;

	public void putResource(Resource new_resource) {
		return;
	}

	public Resource[] peekResources() {
		return null;
	}

	public Resource getResource() {
		return null;
	}
}
