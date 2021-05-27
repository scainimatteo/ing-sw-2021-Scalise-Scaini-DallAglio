package it.polimi.ingsw.model.game;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.util.Observable;

public class Game extends Observable {
	private Player[] players;
	private Market market;
	private DevelopmentCardsOnTable development_cards_on_table;
	private Turn turn;

	public Game(Player[] players, DevelopmentCard[] all_development_cards) {
		this.players = players;
		this.market = new Market();
		this.development_cards_on_table = new DevelopmentCardsOnTable(all_development_cards);
		this.turn = new Turn(this.players[0]);
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public Turn getTurn(){
		return this.turn;
	}

	/**
	 * MARKET METHODS
	 */
	public Market getMarket() {
		return this.market;
	}

	public void shiftRow(int index) {
		this.market.shiftRow(index);
	}
	
	public void shiftColumn(int index) {
		this.market.shiftColumn(index);
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
	}
	
	/**
	 * TURN METHODS
	 */
	public int nextPlayer(Player curr) throws IllegalArgumentException {
		for (int i = 0; i < players.length; i++){
			if (players[i] == curr){ 
				return i+1;
			}
		}
		throw new IllegalArgumentException();
	}

	public void shiftPlayers() {
		List<Player> players_list = Arrays.asList(this.players);
		Collections.rotate(players_list, 1);
		this.players = players_list.toArray(new Player[this.players.length]);
	}

	public void endTurn(){
		if (nextPlayer(turn.getPlayer()) == 4){
			if(turn.isFinal()){
				endGame();
			} else {
				shiftPlayers();
				turn.init(players[0]);
			}
		} else {
			turn.init(players[nextPlayer(turn.getPlayer())]);
		}
	}

	public void endGame(){}

	public void handleError(){
	}
}
