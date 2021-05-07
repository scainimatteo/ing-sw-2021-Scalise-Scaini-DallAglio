package it.polimi.ingsw.controller.match;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator; 
import java.util.HashMap;

import java.lang.Math;

import it.polimi.ingsw.controller.turn.Turn;

import it.polimi.ingsw.controller.match.Initializer;

import it.polimi.ingsw.controller.util.CommunicationController;
import it.polimi.ingsw.controller.util.ResourceController;
import it.polimi.ingsw.controller.util.ChoiceController;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.NotExecutableException;

public class Match implements Runnable {
	private CommunicationController comm_controller;
	private ChoiceController choice_controller;
	private HashMap<Player, Integer> victory_points;
	private Game game;

	public Match(ArrayList<ClientHandler> clients) {
		this.comm_controller = new CommunicationController();
		this.choice_controller = new ChoiceController(this.comm_controller);
		this.victory_points = new HashMap<Player, Integer>();

		try {
			this.game = new Initializer(this.comm_controller, this.choice_controller).initializeGame(clients);
		} catch (InstantiationException e) {
			System.out.println("Game could not start");
		}
	}

	/**
	 * Start the match
	 */
	public void run() {
		Player[] players = this.game.getPlayers();
		boolean last_round = false;
		boolean turn_flag = false;

		while (!last_round) {
			turn_flag = false;

			for (Player p: players) {
				while (!turn_flag) {
					Turn turn = this.choice_controller.pickTurn(p, this.game.getDevelopmentCardsOnTable(), this.game.getMarket());

					try{
						turn.playTurn();
						turn_flag = true;
					} catch (NotExecutableException e){
						this.choice_controller.sendMessage(p, "This turn cannot be performed right now, please choose another one");
					}
				}

				last_round = checkLastRound(players);
			}
			this.game.shiftPlayers();
		}

		countVictoryPoints(players);
		rankPlayers();
		closeGame(players);
	}

	/**
	 * @param players the array of Players
	 * @return true if it's the last round
	 */
	public boolean checkLastRound(Player[] players) {
		int last_position = players[0].getFaithTrack().getLastPosition();

		for (Player p: players) {
			// if a Player is on the last Cell
			if (p.getMarkerPosition() == last_position) {
				return true;
			}

			// if a Player bought 7 DevelopmentCards
			Iterator<DevelopmentCard> slots_iterator = p.getDevCardIterator();
			int i = 0;
			while (slots_iterator.hasNext()) {
				i++;
				slots_iterator.next();
			}

			if (i == 7) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the total victory points of the players and stores them in the victory_points HashMap
	 * @param players the array of Players
	 */
	private void countVictoryPoints(Player[] players) {
		for (Player p: players) {
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
			for (LeaderCard c: p.getDeck()) {
				addPoints(p, c.getPoints());
			}

			// RESOURCES
			ResourceController res_controller = new ResourceController(p, null);
			int number_of_resources = res_controller.howManyResources();
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

	/**
	 * Send the Players the rankings
	 */
	private void rankPlayers() {
		// create a new Hashmap using the nicknames instead of the Players
		HashMap<String, Integer> rank = new HashMap<String, Integer>();
		for (Player p: this.victory_points.keySet()) {
			rank.put(p.getNickname(), this.victory_points.get(p));
		}

		for (Player p: this.victory_points.keySet()) {
			this.choice_controller.sendMessage(p, new Message(MessageType.RANKING, rank));
		}
	}

	/**
	 * @param players the array of Players
	 */
	private void closeGame(Player[] players) {
		for (Player p: players) {
			this.comm_controller.disconnectPlayer(p, "Thanks for playing with us!");
		}
	}
}
