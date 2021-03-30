package it.polimi.ingsw.model.game.sologame;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

public class DiscardDevelopmentCards implements SoloActionToken {
	DevelopmentCardsColor color;

	public DiscardDevelopmentCards(DevelopmentCardsColor color) {
		this.color = color;
	}

	public DevelopmentCardsColor getColor() {
		return this.color;
	}
}
