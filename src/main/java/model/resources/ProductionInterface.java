package it.polimi.ingsw.model.resources;

public interface ProductionInterface {
	public Resource[] activateProduction();
	public Resource[] getRequiredResources();
	public Resource[] getProducedResources();
	public void setRequiredResources(Resource[] cost);
	public void setProducedResources(Resource[] cost);
}
