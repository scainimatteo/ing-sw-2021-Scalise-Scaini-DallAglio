package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;

import it.polimi.ingsw.model.player.Player;

public class Game {
	private Player[] players;
	private Market market;
	private DevelopmentCardsOnTable development_cards_on_table;

	//TODO: Il gioco potrebbe crearsi le carte con la Factory
	public Game(Player[] players, DevelopmentCard[] all_development_cards) {
		this.players = players;
		this.market = new Market();
		this.development_cards_on_table = new DevelopmentCardsOnTable(all_development_cards);
	}

	public Player[] getPlayers() {
		return this.players;
	}
}
