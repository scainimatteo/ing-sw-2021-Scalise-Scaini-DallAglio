package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

public class Warehouse {
	private Resource top_resource;
	private Resource[] middle_resources;
	private Resource[] bottom_resources;

	private boolean isPossibleToInsert(Resource[] new_resources) {
		return false;
	}

	private void rearrangeWarehouse(Resource[] new_resources) {
		return;
	}

	public void tryToInsert(Resource[] new_resources) {
		return;
	}

	public Resource[] getFromWarehouse(Resource resource_type, int quantity) {
		return null;
	}
}
