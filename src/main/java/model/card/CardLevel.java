package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;

public class CardLevel {
	private int lv;
	private DevelopmentCardsColor color;

	public CardLevel (int level, DevelopmentCardsColor color){
		this.lv = level;
		this.color = color;
	}
	
	public int getLevel() {
		return lv;
	}

	public DevelopmentCardsColor getColor () {
		return color;
	}
	
	public int compareLevel(CardLevel target){
		return this.lv - target.lv;
	}

	public boolean compareColor(CardLevel target){
		return this.color.equals(target.color);
	}
}
