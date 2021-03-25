package it.polimi.ingsw.model.resources;

public enum Resource {
	COIN ("YELLOW"),
	STONE ("GREY"),
	SERVANT ("PURPLE"),
	SHIELD ("BLUE"),
	FAITH ("RED");

	private String color;

	private Resource(String color){
		this.color = color;
	}

	public String getColor(){
		return this.color;
	}
}
