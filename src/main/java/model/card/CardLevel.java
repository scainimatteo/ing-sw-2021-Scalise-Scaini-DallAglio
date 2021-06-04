package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import java.io.Serializable;

public class CardLevel implements Serializable {
	private static final long serialVersionUID = 8L;
	private int level;
	private DevelopmentCardsColor color;

	public CardLevel (int level, DevelopmentCardsColor color){
		this.level = level;
		this.color = color;
	}

	/**
	* @return the level of the Card 
	*/
	public int getLevel() {
		return this.level;
	}
	
	/**
	* @return the color of the Card 
	*/
	public DevelopmentCardsColor getColor() {
		return this.color;
	}
	
	/**
	* @param target the card to compare with this 
	* @return the difference in levels
	*/
	public int compareLevel(CardLevel target){
		return this.level - target.level;
	}

	/**
	* @param target the card to compare with this 
	* @return the result of the comparison
	*/
	public boolean compareColor(CardLevel target){
		return this.color.equals(target.color);
	}

	/**
	 * @param o the CardLevel to compare
	 * @return true if the CardLevels are equal or if the CardLevel passed is smaller
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CardLevel)) {
			return false;
		}
		CardLevel card_level = (CardLevel) o;

		if (!compareColor(card_level)) {
			return false;
		} else if (compareLevel(card_level) < 0) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Integer.parseInt(this.level + "" + this.color.ordinal());
	}

	//TODO: rename toString?
	public String toText(){
		String lvl = "";
		char ball = '\u25EF';
		for (int i = 0; i < this.level; i++){
			lvl += Character.toString(ball);
		}
		return color.colorString(color.toString() + " " + lvl);
	}
}

