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
}

/*
    ___
 __|_  )
|___/ /
   /___|
*/
