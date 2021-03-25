package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.model.resources.Resource;

import java.io.*;

public class Production {
	private Resource[] required_resources;
	private Resource[] produced_resources;

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

	/**
	 * @param cost requires an array of resources to compare it with required_resources
	 * @return the requested resources if compareArrays returns true, null if compareArrays returns false
	 */
	public Resource[] activateProduction(Resource[] cost){
		if(this.compareArrays(cost)){
			return produced_resources;
		}else{
			return null;
		}
	}

	/**
	 * @param array_in_input is an array of Resources that must to be compared with this.required_resources
	 * @return true if the two arrays have the same elements (even if they are not in the same position)
	 * TODO: change public to private
	 */
	public boolean compareArrays(Resource[] array_in_input){
		if(array_in_input[0].equals(this.required_resources[0]) || array_in_input[1].equals(this.required_resources[0])){
			if(array_in_input[0].equals(this.required_resources[0])){
				return(array_in_input[1].equals(this.required_resources[1]));
			}else{
				return(array_in_input[0].equals(this.required_resources[1]));
			}
		}else{
			return false;
		}
	}
}
