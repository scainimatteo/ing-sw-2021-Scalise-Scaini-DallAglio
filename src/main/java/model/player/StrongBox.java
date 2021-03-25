package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.model.resources.Resource;

public class StrongBox {
	private HashMap<Resource, Integer> storage;

	public StrongBox(){
		this.storage = new HashMap<Resource, Integer>();
		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		this.storage.put(coin_resource, 0);
		this.storage.put(stone_resource, 0);
		this.storage.put(shield_resource, 0);
		this.storage.put(servant_resource, 0);
	}

	public void insertResources(Resource[] new_resources) {
		for (Resource resource : new_resources){
			storage.containsKey(resource);
			if(storage.containsKey(resource)){
				this.storage.put(resource, storage.get(resource) + 1);
			}else{
				//TODO: lancia eccezione
				break;
			}
		}
	}

	public HashMap<Resource, Integer> getStrongBox(){
		return this.storage;
	}

	/**
	 * TBT
	 */
	public Resource[] removeResources(Resource resource_type, int quantity) {
		Integer numof_resource = this.storage.get(resource_type);

		if(quantity <= numof_resource){
			ArrayList<Resource> temp_array = new ArrayList<Resource>();

			for (int i = 0; i < quantity; i ++){
				temp_array.add(resource_type);
			}

			Resource[] to_return = temp_array.toArray(new Resource[quantity]);

			this.storage.put(resource_type, (numof_resource - quantity));

			return to_return;
		}else{
			// TODO: lancia l'eccezione
			return null;
		}
	}
}
