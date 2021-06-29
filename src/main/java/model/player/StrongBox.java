package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.model.resources.Resource;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;

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
	 * Persistence only - recreate a StrongBox from the match saved in memory
	 */
	public StrongBox(HashMap<Resource, Integer> storage){
		this.storage = storage;
	}

	public int get(Resource res){
		return storage.get(res);
	}
	
	public HashMap<Resource, Integer> getStorage(){
		return this.storage;
	}

	/**
	 * @param new_resources is the list of resources to insert
	 * @exception IllegalArgumentException is thrown if a resource is not present
	 */
	public void insertResources(ArrayList<Resource> new_resources) {
		for (Resource resource : new_resources){
			if (this.storage.containsKey(resource)){
				this.storage.put(resource, storage.get(resource) + 1);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	/**
	 * @param to_remove is the list containing the resource to be removed
	 */
	public void removeResources (ArrayList<Resource> to_remove) {
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource res : check){
			this.storage.put(res, this.storage.get(res) -  (int) to_remove.stream().filter(x->x.equals(res)).count());
		}
		return;
	}

	/**
	 * @param to_check are the resources that need to be checked
	 * @return true if the resources are contained
	 */
	public boolean areContainedInStrongbox(ArrayList<Resource> cost){
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource res : check){
			if(storage.get(res) < (int) cost.stream().filter(x->x.equals(res)).count()){
				return false;
			}
		}
		return true;
	}

	/**
	 * sets all resource values to zero
	 */
	public void clear(){
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource res : check){
			storage.put(res, 0);	
		}
	}

	/**
	 * @return true if every resource value is zero
	 */
	public boolean isEmpty(){
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource res : check){
			if(storage.get(res) != 0){
				return false;
			}
		}
		return true;
	}
}

