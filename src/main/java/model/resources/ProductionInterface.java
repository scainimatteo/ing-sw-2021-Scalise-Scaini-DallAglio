package it.polimi.ingsw.model.resources;

import java.util.ArrayList;

public interface ProductionInterface {
	public ArrayList<Resource> getRequiredResources();
	public ArrayList<Resource> getProducedResources();
	public void setRequiredResources(ArrayList<Resource> cost);
	public void setProducedResources(ArrayList<Resource> cost);
}
