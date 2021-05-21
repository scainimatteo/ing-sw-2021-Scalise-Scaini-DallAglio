package it.polimi.ingsw.model.player;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.player.Storage;

import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;

public class StrongBox implements Storage {
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

	public int get(Resource res){
		return storage.get(res);
	}
	
	@Override
	public void getResource(Resource res) throws NoSuchElementException {
		removeResources(res, 1);
		return;
	}

	@Override
	public void storeResource (Resource res) throws IllegalArgumentException {
		insertResources(new Resource[] {res});
	}

	/**
	 * @param new_resources is the array of resources get from the market
	 * @exception IllegalArgumentException is thrown if a resource is not present
	 */
	public void insertResources(Resource[] new_resources) {
		for (Resource resource : new_resources){
			if (this.storage.containsKey(resource)){
				this.storage.put(resource, storage.get(resource) + 1);
			} else {
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
	 * @exception NoSuchElementException is thrown if the quantity is greater than the resources stored
	 */
	public Resource[] removeResources(Resource resource_type, int quantity) throws NoSuchElementException {
		Integer numof_resource = this.storage.get(resource_type);

		if ( !(this.storage.containsKey(resource_type)) ){
			throw new NoSuchElementException();
		}

		if (quantity <= numof_resource){
			ArrayList<Resource> temp_array = new ArrayList<Resource>();

			for (int i = 0; i < quantity; i ++){
				temp_array.add(resource_type);
			}

			Resource[] to_return = temp_array.toArray(new Resource[quantity]);

			this.storage.put(resource_type, (numof_resource - quantity));

			return to_return;
		} else {
			throw new NoSuchElementException();
		}
	}

	/**
	 * @param to_check are the resources that need to be checked
	 * @return true if the resources are contained
	 */
	public boolean areContainedInStrongbox(Resource[] to_check){
		int coin = 0;
		int stone = 0;
		int shield = 0;
		int servant = 0;

		for (int i = 0; i < to_check.length; i ++){
			if (this.storage.containsKey(to_check[i])){
				if (to_check[i].equals(Resource.COIN)){
					coin += 1;
				} else if (to_check[i].equals(Resource.STONE)){
					stone += 1;
				} else if (to_check[i].equals(Resource.SHIELD)){
					shield += 1;
				} else if (to_check[i].equals(Resource.SERVANT)){
					servant += 1;
				}
			} else {
				if (to_check[i] == null){
				} else {
					return false;
				}
			}

			if (coin > this.storage.get(Resource.COIN) || stone > this.storage.get(Resource.STONE) || shield > this.storage.get(Resource.SHIELD) || servant > this.storage.get(Resource.SERVANT)){
				return false;
			}

			to_check[i] = null;
		}

		return true;
	}

	public String printText() {
		String string = "·--------------·\n|              |\n";
		for (Resource r: storage.keySet()) {
			if (storage.get(r) < 10) {
				string += "|    ";
			} else {
				string += "|   ";
			}
			string += String.valueOf(storage.get(r)) + " x " + r.getAbbreviation() + "    |\n";
		}
		return string + "|              |\n|--------------|\n";
	}

	public String printText(int index){
		return null;
	}
}



//    ·--------------·
//    |              |
//    |    1 x sp    |
//    |   10 x sp    |
//    |   [] x sp    |
//    |   [] x sp    |
//    |              |
//    |--------------|
