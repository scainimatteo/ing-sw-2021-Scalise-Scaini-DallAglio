package it.polimi.ingsw.view.simplemodel;

import java.util.ArrayList;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import it.polimi.ingsw.model.resources.Resource;

import java.io.Serializable;

public class SimpleSoloGame extends SimpleGame implements Serializable {
	private static final long serialVersionUID = 6646460L;
	private SoloActionToken last_token;
	private int active_tokens_size;

	public SimpleSoloGame(ArrayList<String> order, Resource[][] market, Resource free_marble, DevelopmentCard[][] development_cards_on_table, SoloActionToken last_token, int active_tokens_size) {
		super(order, market, free_marble, development_cards_on_table);
		this.last_token = last_token;
		this.active_tokens_size = active_tokens_size;
	}

	public SoloActionToken getLastToken() {
		return this.last_token;
	}

	public int getActiveTokensSize() {
		return this.active_tokens_size;
	}
}
