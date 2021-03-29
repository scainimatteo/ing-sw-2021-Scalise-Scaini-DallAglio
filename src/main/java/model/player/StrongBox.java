package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.model.resources.Resource;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;

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

	/**
	 * @param new_resources is the array of resources get from the market
	 * @exception IllegalArgumentException is thrown if a resource is not present
	 */
	public void insertResources(Resource[] new_resources) {
		for (Resource resource : new_resources){
			if(this.storage.containsKey(resource)){
				this.storage.put(resource, storage.get(resource) + 1);
			}else{
				throw new IllegalArgumentException();
			}
		}
	}

	public HashMap<Resource, Integer> getStrongBox(){
		return this.storage;
	}

	/**
	 * @param resource_type is the resource to be removed
	 * @param quantity is the number of resources requested
	 * @return the array of resources requested
	 * @exception IllegalArgumentException is thrown if the resource_type is not contained
	 * @exception IndexOutOfBoundsException is thrown if the quantity is greater than the resources stored
	 */
	public Resource[] removeResources(Resource resource_type, int quantity) {
		Integer numof_resource = this.storage.get(resource_type);

		if(!(this.storage.containsKey(resource_type))){
			throw new IllegalArgumentException();
		}

		if(quantity <= numof_resource){
			ArrayList<Resource> temp_array = new ArrayList<Resource>();

			for (int i = 0; i < quantity; i ++){
				temp_array.add(resource_type);
			}

			Resource[] to_return = temp_array.toArray(new Resource[quantity]);

			this.storage.put(resource_type, (numof_resource - quantity));

			return to_return;
		}else{
			throw new IndexOutOfBoundsException();
		}
	}
}
