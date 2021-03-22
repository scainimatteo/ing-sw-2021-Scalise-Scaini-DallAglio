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

	public Resource[] getRequiredResources () {
		return this.required_resources;
	}

	public Resource[] getProducedResources () {
		return this.produced_resources;
	}

	public Resource[] activateProduction (Resource[] cost) {
		if (this.compareArrays(cost)) {
			return produced_resources;
		}else {
			return null;
		}
	}

	/**
	 * TODO: change public to private
	 */
	public boolean compareArrays (Resource[] array_in_input) {
		if (array_in_input[0].equals(this.required_resources[0]) || array_in_input[1].equals(this.required_resources[0])) {
			if (array_in_input[0].equals(this.required_resources[0])) {
				if (array_in_input[1].equals(this.required_resources[1])) {
					return true;
				}else {
					return false;
				}
			}else {
				if (array_in_input[0].equals(this.required_resources[1])) {
					return true;
				}else {
					return false;
				}
			}
		}else {
			return false;
		}
	}
}
