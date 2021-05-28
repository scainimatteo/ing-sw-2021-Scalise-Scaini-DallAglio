package it.polimi.ingsw.model.card;

import java.util.NoSuchElementException; 
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class ExtraSpaceAbility extends LeaderAbility {
	private static final long serialVersionUID = 78L;
	private Resource resource_type;
	private ArrayList<Resource> storage;

	public ExtraSpaceAbility(Resource resource_type){
		this.resource_type = resource_type;
		this.storage = new ArrayList<Resource> ();
	}

	public Resource getResourceType() {
		return resource_type;
	}
	
	public int peekResources(){
		return this.storage.size();
	}

	/**
	 * Checks whether the given ArrayList of resources containes only the correct resource type
	 * 
	 * @param res is the ArrayList of resources to be checked
	 */
	private boolean isAllRightType (ArrayList<Resource> res){
		for (Resource x : res){
			if (!x.equals(resource_type)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the given ArrayList can be stored in the extra space
	 *
	 * @param res is the ArrayList to be checked
	 */
	public boolean canBeStoredExtra(ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else {
			return res.size() <= 2 - storage.size() && isAllRightType(res);
		}
	}

	/**
	 * Checks if the given ArrayList is contained in the extra space
	 *
	 * @param res is the ArrayList to be checked
	 */
	public boolean isContainedExtra(ArrayList<Resource> res){
		if (res.isEmpty()){
			return true;
		} else {
		return res.size() <= storage.size() && isAllRightType(res);
		}
	}

	/**
	* @param new_resource is the resource to be added to the space
	* @throws IllegalArgumentException if resource type isn't compatible with required type or the space is full
	*/
	public void storeExtra(ArrayList<Resource> res) throws IllegalArgumentException {
		if (!res.get(0).equals(resource_type) || res.size() > 2 - storage.size()){
			System.out.println(res.get(0) + " " + resource_type);
			throw new IllegalArgumentException();	
		}
		else {
			storage.addAll(res);
		} 
	}

	/**
	* @param quantity is the number of resources to be taken
	* @return array of requested resources
	* @throws NoSuchElementException if requested quantity exceed present quantity
	*/
	public void getFromExtra(ArrayList<Resource> res) throws NoSuchElementException {
		if (res.size() > storage.size() || res.size() == 0){
			throw new NoSuchElementException();
		} else if (res.stream().allMatch(x->x.equals(resource_type))) {
			throw new IllegalArgumentException();
		} else {
			for (Resource x : res){
				storage.remove(x);
			}
		}
	}

	/**
	* Checks if the target ability is ExtraSpaceAbility type through overload of the original method
	*
	* @param target allows the ability to run the overloaded method if the type is correct, else the inherited superclass method is run
	* @return true, as it highlights that target ability is an ExtraSpaceAbility type else the inherited superclass method would be run
	*/ 
	public boolean checkAbility (ExtraSpaceAbility target){
		return true;
	}

	public String printText(){
		String string = "| " + resource_type.getAbbreviation() + " |            |\n|----·            |\n|                 |\n|  ·----· ·----·  |\n";
		if (storage.size() == 0){
			string +="|  |    | |    |  |\n";
		} else if (storage.size() ==1){
			string +="|  | " + resource_type.getAbbreviation() + " |   |   |  |\n";
		} else if (storage.size() == 2){
			string +="|  | " + resource_type.getAbbreviation() + " |   | " + resource_type.getAbbreviation() + " |  |\n";
		}
		return string + "|  ·----· ·----·  |\n|                 |\n";
	}
}
/*
| XX |            |
|----·            |
|                 |
|  ·----· ·----·  |
|  |    | |    |  |
|  ·----· ·----·  |
|                 |
*/
