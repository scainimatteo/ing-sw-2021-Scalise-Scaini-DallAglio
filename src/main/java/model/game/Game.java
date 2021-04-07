package it.polimi.ingsw.model.game;

import java.util.Collections;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;

import it.polimi.ingsw.model.player.Player;

public class Game {
	private Player[] players;
	private Market market;
	private DevelopmentCardsOnTable development_cards_on_table;

	public Game(Player[] players, DevelopmentCard[] all_development_cards) {
		this.players = players;
		this.market = new Market();
		this.development_cards_on_table = new DevelopmentCardsOnTable(all_development_cards);
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public Market getMarket() {
		return this.market;
	}

	public DevelopmentCardsOnTable getDevelopmentCardsOnTable() {
		return this.development_cards_on_table;
	}

	public void shiftPlayers() {
		List<Player> players_list = Arrays.asList(this.players);
		Collections.rotate(players_list, 1);
		this.players = players_list.toArray(new Player[this.players.length]);
	}
}
