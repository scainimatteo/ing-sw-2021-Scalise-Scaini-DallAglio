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
	private ArrayList<Player> players;
	private Market market;
	private DevelopmentCardsOnTable development_cards_on_table;
	private Turn turn;

	public Game(ArrayList<Player> players, DevelopmentCard[] all_development_cards) {
		this.players = players;
		this.market = new Market();
		this.development_cards_on_table = new DevelopmentCardsOnTable(all_development_cards);
		this.turn = new Turn(this.players.get(0));
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public Turn getTurn(){
		return this.turn;
	}

	public void handleError(String error_message){
		notify(new ErrorMessage(error_message));
	}

	public void notifyGame() {
		notify(new ViewUpdate(this.simplify()));
	}

	private SimpleGame simplify() {
		ArrayList<String> order = new ArrayList<String>();
		for (Player p: this.players) {
			order.add(p.getNickname());
		}
		return new SimpleGame(order, market.peekMarket(), market.getFreeMarble(), development_cards_on_table.getTopCards());
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
	
	/**
	 * TURN METHODS
	 */
	public void endTurn(){
		if (this.players.indexOf(turn.getPlayer()) + 1 == this.players.size()){
			if(turn.isFinal()){
				endGame();
			} else {
				Collections.rotate(this.players, 1);
				turn.clearTurn(this.players.get(0));
			}
		} else {
			turn.clearTurn(this.players.get(this.players.indexOf(turn.getPlayer()) + 1));
		}
	}

	// TODO
	public void endGame(){}
}
