package it.polimi.ingsw.model.card;

import java.util.NoSuchElementException; 
import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.player.Storage;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class ExtraSpaceAbility extends LeaderAbility implements Storage {
	private static final long serialVersionUID = 78L;
	private Resource resource_type;
	private int index;

	public ExtraSpaceAbility(Resource resource_type){
		this.resource_type = resource_type;
		this.index = 0;
	}
	
	@Override
	public void getResource(Resource res) throws NoSuchElementException {
		getResources(1);	
		return;
	}
	
	@Override
	public void storeResource(Resource res) throws IllegalArgumentException {
		putResource(res);
		return;
	}

	/**
	* @param new_resource is the resource to be added to the space
	* @throws IllegalArgumentException if resource type isn't compatible with required type or the space is full
	*/
	public void putResource(Resource new_resource) throws IllegalArgumentException {
		if (!new_resource.equals(resource_type) || index >= 2){
			throw new IllegalArgumentException();	
		}
		else {
			index++;
		} 
	}

	/**
	* @return the number of resources available
	*/
	public int peekResources() {
		return index;
	}

	/**
	* @return the type of resources available
	*/
	public Resource getResourceType() {
		return resource_type;
	}

	/**
	* @param quantity is the number of resources to be taken
	* @return array of requested resources
	* @throws NoSuchElementException if requested quantity exceed present quantity
	*/
	public Resource[] getResources(int quantity) throws NoSuchElementException {
		if (quantity > index || quantity == 0){
			throw new NoSuchElementException();
		}
		else {
			Resource[] moved_resources = new Resource[1];
			if (quantity == 1){
				moved_resources[0] = resource_type;
			}
			else if (quantity == 2){
				moved_resources = new Resource[2];
				moved_resources[0] = resource_type;
				moved_resources[1] = resource_type;
			}
			index = index - quantity;
			return moved_resources;
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

	@Override
	public String printText(){
		String string = "| " + resource_type.getAbbreviation() + " |            |\n|----·            |\n|                 |\n|  ·----· ·----·  |";
		if (index == 0){
			string +="|  |   |   |   |  |\n";
		} else if (index ==1){
			string +="|  | " + resource_type.getAbbreviation() + " |   |   |  |\n";
		} else if (index == 2){
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
