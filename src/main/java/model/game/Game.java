package it.polimi.ingsw.model.game;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.util.Observable;

import it.polimi.ingsw.view.simplemodel.SimpleGame;

public class Game extends Observable {
	private Player[] players;
	private Market market;
	private DevelopmentCardsOnTable development_cards_on_table;
	private Turn turn;

	public Game(Player[] players, DevelopmentCard[] all_development_cards) {
		this.players = players;
		this.market = new Market();
		this.development_cards_on_table = new DevelopmentCardsOnTable(all_development_cards);
		this.turn = new Turn(this.players[0], false);
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public Turn getTurn(){
		return this.turn;
	}

	public void handleError(String error_message){
		notify(new ErrorMessage(error_message));
	}

	private void notifyGame() {
		notify(new ViewUpdate(this.simplify()));
	}

	private SimpleGame simplify() {
		return null;
	}

	/**
	 * MARKET METHODS
	 */
	public Market getMarket() {
		return this.market;
	}

	public void shiftRow(int index) {
		this.market.shiftRow(index);
		this.notifyGame();
	}
	
	public void shiftColumn(int index) {
		this.market.shiftColumn(index);
		this.notifyGame();
	}

	public ArrayList<Resource> getRow(int index) {
		return this.market.getRow(index);
	}

	public ArrayList<Resource> getColumn(int index) {
		return this.market.getColumn(index);
	}

	/**
	 * TABLE METHODS
	 */
	public DevelopmentCardsOnTable getDevelopmentCardsOnTable() {
		return this.development_cards_on_table;
	}

	public DevelopmentCard[][] getTopCards() {
		return this.development_cards_on_table.getTopCards();
	}

	public void getFromDeck(DevelopmentCard chosen_card) {
		this.development_cards_on_table.getFromDeck(chosen_card);
		this.notifyGame();
	}

	public void shiftPlayers() {
		List<Player> players_list = Arrays.asList(this.players);
		Collections.rotate(players_list, 1);
		this.players = players_list.toArray(new Player[this.players.length]);
		this.notifyGame();
	}
}
