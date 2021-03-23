package it.polimi.ingsw.model.card;

import java.lang.IllegalArgumentException; 
import java.lang.IndexOutOfBoundsException;


import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class ExtraSpaceAbility implements LeaderAbility {
	private Resource resource_type;
	private Resource[] store_resources;

	public void putResource(Resource new_resource) throws IllegalArgumentException, IndexOutOfBoundsException {
		if (!new_resource.getColor().equals(resource_type.getColor())){
			throw new IllegalArgumentException();	
		}
		else {
			if (store_resources[0] == null){
				store_resources [0] = new_resource;
			}	
			else if(store_resources[1] == null){
				store_resources [1] = new_resource;
			}	
			else {throw new IndexOutOfBoundsException();}
		}
	} 

	public Resource[] peekResources() {
		return store_resources;
	}

	public Resource getResourceType() {
		return resource_type;
	}

//restituisce la prima risorsa trovata o non restituisce nulla
	public Resource[] getResource() {
		Resource[] moved_resources = this.peekResources();
		for (Resource x : store_resources) {
			x = null;
		}
		return moved_resources;
	}
}
