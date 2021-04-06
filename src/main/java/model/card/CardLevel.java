package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;

public class CardLevel {
	private int lv;
	private DevelopmentCardsColor color;

	public CardLevel (int level, DevelopmentCardsColor color){
		this.lv = level;
		this.color = color;
	}

	/**
	* @return the level of the Card 
	*/
	public int getLevel() {
		return lv;
	}

	
	/**
	* @return the color  of the Card 
	*/
	public DevelopmentCardsColor getColor () {
		return color;
	}
	
	
	/**
	* @param target the card to compare with this 
	* @return the difference in levels
	*/
	public int compareLevel(CardLevel target){
		return this.lv - target.lv;
	}

	/**
	* @param target the card to compare with this 
	* @return the result of the comparison
	*/
	public boolean compareColor(CardLevel target){
		return this.color.equals(target.color);
	}
}
