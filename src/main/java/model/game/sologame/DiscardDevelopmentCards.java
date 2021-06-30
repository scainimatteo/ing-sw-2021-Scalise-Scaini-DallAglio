package it.polimi.ingsw.model.game.sologame;

import it.polimi.ingsw.controller.SoloGameController;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import java.io.Serializable;

public class DiscardDevelopmentCards implements SoloActionToken, Serializable {
	private static final long serialVersionUID = 999009L;
	DevelopmentCardsColor color;

	public DiscardDevelopmentCards(DevelopmentCardsColor color) {
		this.color = color;
	}

	public DevelopmentCardsColor getColor() {
		return this.color;
	}

	@Override
	public void useToken(SoloGameController controller) {
		controller.discardDevelopmentCards(this.color);
	}

	@Override
	public String toString() {
		return this.color.colorString("    ___\n __|_  )\n|___/ /\n   /___|\n");
	}

	@Override
	public String getPath() {
		switch(this.color) {
			case GREEN:
				return "/images/tokens/sologame/discard_development_cards_green.png";
			case YELLOW:
				return "/images/tokens/sologame/discard_development_cards_yellow.png";
			case PURPLE:
				return "/images/tokens/sologame/discard_development_cards_purple.png";
			case BLUE:
				return "/images/tokens/sologame/discard_development_cards_blue.png";
			default:
				return "/images/tokens/sologame/discard_development_cards_green.png";
		}
	}

	@Override
	public String getType() {
		return "DISCARDDEVELOPMENTCARDS";
	}
}

