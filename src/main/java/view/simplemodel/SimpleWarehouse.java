package it.polimi.ingsw.view.simplemodel;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public class SimpleWarehouse {
	private ArrayList<Resource> top_resource;
	private ArrayList<Resource> middle_resources;
	private ArrayList<Resource> bottom_resources;

	public SimpleWarehouse(ArrayList<Resource> top, ArrayList<Resource> middle, ArrayList<Resource> bottom){
		this.top_resource = top;
		this.middle_resources = middle;
		this.bottom_resources = bottom;
	}

	public ArrayList<Resource>  getTopResource(){
		 return top_resource;
	}

	public ArrayList<Resource> getMiddleResources(){
		return middle_resources;
	}

	public ArrayList<Resource> getBottomResources(){
		return bottom_resources;
	}
}
