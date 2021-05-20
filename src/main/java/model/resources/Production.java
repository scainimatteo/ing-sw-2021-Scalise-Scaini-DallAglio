package it.polimi.ingsw.model.resources;

import java.lang.IllegalArgumentException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class Production implements ProductionInterface, Serializable {
	private static final long serialVersionUID = 6L;
	protected ArrayList<Resource> required_resources;
	protected ArrayList<Resource> produced_resources;

	public Production (ArrayList<Resource> required, ArrayList<Resource> produced){
		this.required_resources = required;
		this.produced_resources = produced;
	}

	public ArrayList<Resource> getRequiredResources(){
		return this.required_resources;
	}

	public ArrayList<Resource> getProducedResources(){
		return this.produced_resources;
	}

	public void setRequiredResources(ArrayList<Resource> cost){
		this.required_resources = cost;
	}

	public void setProducedResources(ArrayList<Resource> output){
		this.produced_resources = output;
	}

	/**
	 * @return the requested resources if compareArrays returns true, null if compareArrays returns false
	 */
	public ArrayList<Resource> activateProduction(){
		return produced_resources;
	}

	
/* TODO: move in simpleModel
	public HashMap<Resource, Integer> numOfRequired(){
		HashMap<Resource, Integer> to_return = new HashMap<Resource, Integer>();

		for (Resource res : required_resources){
			if (to_return.containsKey(res)){
				to_return.put(res, to_return.get(res) + 1);
			} else {
				to_return.put(res, 1);
			}
		}

		return to_return;
	}

	public HashMap<Resource, Integer> numOfProduced(){
		HashMap<Resource, Integer> to_return = new HashMap<Resource, Integer>();

		for (Resource res : produced_resources){
			if (to_return.containsKey(res)){
				to_return.put(res, to_return.get(res) + 1);
			} else {
				to_return.put(res, 1);
			}
		}

		return to_return;
	}

	public String[] productionToText(){
		String[] to_return = new String[5];

		HashMap<Resource, Integer> required = this.numOfRequired();
		Set<Resource> req_set = required.keySet();
		ArrayList<Resource> req_array = req_set.toArray(new Resource[req_set.size()]);

		HashMap<Resource, Integer> produced = this.numOfProduced();
		Set<Resource> prod_set = produced.keySet();
		ArrayList<Resource> prod_array = prod_set.toArray(new Resource[prod_set.size()]);

		int req_size = required.size();
		int prod_size = produced.size();

		for (int i = 0; i < 5; i ++){
			if (req_size == 1){
				if (i == 3){
					to_return[i] = " " + String.valueOf(required.get(req_array[0])) + " " + req_array[0].getAbbreviation();
				} else {
					to_return[i] = "     ";
				}
			} else if (req_size == 2){
				if (i == 2){
					to_return[i] = " " + String.valueOf(required.get(req_array[0])) + " " + req_array[0].getAbbreviation();
				} else if (i == 4){
					to_return[i] = " " + String.valueOf(required.get(req_array[1])) + " " + req_array[1].getAbbreviation();
				} else {
					to_return[i] = "     ";
				}
			} else if (req_size == 3){
				if (i == 1){
					to_return[i] = " " + String.valueOf(required.get(req_array[0])) + " " + req_array[0].getAbbreviation();
				} else if (i == 3){
					to_return[i] = " " + String.valueOf(required.get(req_array[1])) + " " + req_array[1].getAbbreviation();
				} else if (i == 5){
					to_return[i] = " " + String.valueOf(required.get(req_array[2])) + " " + req_array[2].getAbbreviation();
				} else {
					to_return[i] = "     ";
				}
			}

			to_return[i] += " ";

			if (i == 3){
				char arrow = '\u203A';
				to_return[i] += Character.toString(arrow);
			} else {
				to_return[i] += " ";
			}

			to_return[i] += " ";

			if (prod_size == 1){
				if (i == 3){
					to_return[i] += " " + String.valueOf(produced.get(prod_array[0])) + " " + prod_array[0].getAbbreviation();
				} else {
					to_return[i] += "     ";
				}
			} else if (prod_size == 2){
				if (i == 2){
					to_return[i] += " " + String.valueOf(produced.get(prod_array[0])) + " " + prod_array[0].getAbbreviation();
				} else if (i == 4){
					to_return[i] += " " + String.valueOf(produced.get(prod_array[1])) + " " + prod_array[1].getAbbreviation();
				} else {
					to_return[i] += "     ";
				}
			}else if (prod_size == 3){
				if (i == 1){
					to_return[i] += " " + String.valueOf(produced.get(prod_array[0])) + " " + prod_array[0].getAbbreviation();
				} else if (i == 3){
					to_return[i] += " " + String.valueOf(produced.get(prod_array[1])) + " " + prod_array[1].getAbbreviation();
				} else if (i == 5){
					to_return[i] += " " + String.valueOf(produced.get(prod_array[2])) + " " + prod_array[2].getAbbreviation();
				} else {
					to_return[i] += "     ";
				}
			}

			to_return[i] += " ";
		}

		return to_return;
	}
	*/
}
