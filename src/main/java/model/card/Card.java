package it.polimi.ingsw.model.card;

public abstract class Card {
	protected int victory_points;
	protected int id;
	
	public int getPoints (){
		return victory_points;
	}

	public int getId() {
		return this.id;
	}
}
