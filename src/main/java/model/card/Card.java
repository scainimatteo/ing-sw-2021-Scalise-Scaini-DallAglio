package it.polimi.ingsw.model.card;

import java.io.Serializable;

public abstract class Card implements Serializable {
	private static final long serialVersionUID = 88734L;
	protected int victory_points;
	protected int id;
	
	public int getPoints(){
		return victory_points;
	}

	public int getId() {
		return this.id;
	}
}
