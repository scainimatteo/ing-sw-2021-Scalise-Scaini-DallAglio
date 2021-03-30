package it.polimi.ingsw.model.card;

public abstract class Card {
	protected int victory_points;
	
//	public Card (int points){
//		this.victory_points = points;
//	}

	public int getPoints (){
		return victory_points;
	}
}
