package it.polimi.ingsw.model.game;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.controller.servermessage.EndGameMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.util.observer.ModelObservable;

import it.polimi.ingsw.view.simplemodel.SimpleGame;

public class Game extends ModelObservable {
	protected ArrayList<Player> players;
	protected Turn turn;
	protected Market market;
	protected DevelopmentCardsOnTable development_cards_on_table;
	private HashMap<Player, Integer> victory_points;

	public Game(ArrayList<Player> players, DevelopmentCard[] all_development_cards) {
		this.players = players;
		this.market = new Market();
		this.development_cards_on_table = new DevelopmentCardsOnTable(all_development_cards);
		this.victory_points = new HashMap<Player, Integer>();
		this.turn = new Turn(this.players.get(0));
	}

	/**
	 * Persistence only - recreate a Game from the match saved in memory
	 */
	public Game(ArrayList<Player> players, Market market, DevelopmentCardsOnTable development_cards_on_table, Turn turn) {
		this.players = players;
		this.market = market;
		this.development_cards_on_table = development_cards_on_table;
		this.victory_points = new HashMap<Player, Integer>();
		this.turn = turn;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public Turn getTurn(){
		return this.turn;
	}

	/**
	 * Notify an ErrorMessage to the Clients
	 *
	 * @param error_string the error to report
	 * @param player the Player that committed the error
	 */
	public void handleError(String error_string, Player player){
		notifyModel(new ErrorMessage(error_string, player.getNickname()));
	}

	/**
	 * Send a ViewUpdate with the current Game
	 */
	public void notifyGame() {
		notifyModel(new ViewUpdate(this.simplify()));
	}

	/**
	 * Simplify the Game to send it to the Client
	 *
	 * @return the SimpleGame used to represent this Game
	 */
	private SimpleGame simplify() {
		ArrayList<String> order = new ArrayList<String>();
		for (Player p: this.players) {
			order.add(p.getNickname());
		}
		return new SimpleGame(order, this.market.peekMarket(), this.market.getFreeMarble(), this.development_cards_on_table.getTopCards());
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

	/**
	 * Sets the following player as active, initiates a new round if there is no following player
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

	/**
	 * Calculate the total sum of victory points then send the rankings
	 */
	public void endGame(){
		countVictoryPoints();

		// create a new Hashmap using the nicknames instead of the Players
		HashMap<String, Integer> rank = new HashMap<String, Integer>();
		for (Player p: this.victory_points.keySet()) {
			rank.put(p.getNickname(), this.victory_points.get(p));
		}
		notifyModel(new EndGameMessage(rank));
	}

	/**
	 * Adds the total victory points of the players and stores them in the victory_points HashMap
	 */
	private void countVictoryPoints() {
		for (Player p: this.players) {
			this.victory_points.put(p, 0);

			// DEVCARDSLOTS
			Iterator<DevelopmentCard> slots_iterator = p.getDevCardIterator();
			while (slots_iterator.hasNext()) {
				DevelopmentCard card = slots_iterator.next();
				addPoints(p, card.getPoints());
			}

			// FAITHTRACK
			addPoints(p, p.getFaithTrack().getMarkerVictoryPoints());

			// VATICAN REPORTS
			addPoints(p, p.getFaithTrack().getVaticanReportsPoints());

			// LEADERCARDS
			for (LeaderCard c: p.getLeaderCards()) {
				addPoints(p, c.getPoints());
			}

			// RESOURCES
			int number_of_resources = 0;
			HashMap<Resource, Integer> total_resources = p.totalResources();
			for (Resource r: total_resources.keySet()) {
				number_of_resources += total_resources.get(r);
			}
			addPoints(p, Math.floorDiv(number_of_resources, 5));
		}
	}

	/**
	 * Add points to the victory_points HashMap
	 *
	 * @param player the Player that made the points
	 * @param points the points to add
	 */
	private void addPoints(Player player, int points) {
		int points_now = this.victory_points.get(player);
		this.victory_points.put(player, points_now + points);
	}
}
