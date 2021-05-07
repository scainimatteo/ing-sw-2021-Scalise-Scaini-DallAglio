package it.polimi.ingsw.model.resources;

import java.lang.IllegalArgumentException;

import java.io.Serializable;

public class Production implements ProductionInterface, Serializable {
	private static final long serialVersionUID = 6L;
	protected Resource[] required_resources;
	protected Resource[] produced_resources;

	public Production (Resource[] required, Resource[] produced){
		this.required_resources = required;
		this.produced_resources = produced;
	}

	public Resource[] getRequiredResources(){
		return this.required_resources;
	}

	public Resource[] getProducedResources(){
		return this.produced_resources;
	}

	public void setRequiredResources(Resource[] cost){
		this.required_resources = cost;
	}

	public void setProducedResources(Resource[] output){
		this.produced_resources = output;
	}

	/**
	 * @return the requested resources if compareArrays returns true, null if compareArrays returns false
	 */
	public Resource[] activateProduction(){
		return produced_resources;
	}

	/**
	 * @param array_in_input is an array of Resources that must to be compared with this.required_resources
	 * @return true if the two arrays have the same elements (even if they are not in the same position)
	 */
	private boolean compareArrays(Resource[] array_in_input){
		if (array_in_input[0].equals(this.required_resources[0]) || array_in_input[1].equals(this.required_resources[0])){
			if (array_in_input[0].equals(this.required_resources[0])){
				return(array_in_input[1].equals(this.required_resources[1]));
			} else {
				return(array_in_input[0].equals(this.required_resources[1]));
			}
		} else {
			return false;
		}
	}
}
