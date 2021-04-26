package it.polimi.ingsw.controller.match;

import java.util.ArrayList;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.controller.util.CommunicationController;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.server.ClientHandler;

public class Initializer {
	private CommunicationController comm_controller;
	private Player[] players;

	public Initializer(CommunicationController comm_controller) {
		this.comm_controller = comm_controller;
	}

	/**
	 * Starting procedure for the match:
	 *   - creating the Players from the ClientHandlers
	 *   - distribute 4 LeaderCards to each player, make them choose 2
	 *   - choose randomly the first player and assigning in order the starting resources
	 *
	 * @param clients the ClientHandlers of the Players
	 * @return the new Game
	 */
	public Game initializeGame(ArrayList<ClientHandler> clients) throws InstantiationException {
		try {
			createPlayers(clients);
			distributeLeaderCards();
			chooseMatchOrder();
			distributeRandomResources();
			return createGame();
		} catch (Exception e) {
			//TODO: too generic?
			throw new InstantiationException();
		}
	}

	/**
	 * Create an array of Players using the ClientHandlers
	 *
	 * @param clients the ClientHandlers of the Players
	 */
	private void createPlayers(ArrayList<ClientHandler> clients) throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		Cell[] all_cells = factory.getAllCells();
		Tile[] all_tiles = factory.getAllTiles();

		this.players = new Player[clients.size()];

		int i = 0;
		for (ClientHandler ch: clients) {
			Player player = new Player(ch.getNickname(), all_cells, all_tiles);
			this.comm_controller.insertPlayer(player, ch);
			this.players[i] = player;
			i++;
		}
	}

	/**
	 * Distribute the starting LeaderCards to each Player
	 */
	private void distributeLeaderCards() throws ParseException, IOException {
		Deck<LeaderCard> all_leader_cards = getLeaderCardDeck();
		//TODO: send the 4 cards to the players, get their response using the ChoiceController
	}

	/**
	 * @return a Deck containing all the LeaderCards in random order
	 */
	private Deck<LeaderCard> getLeaderCardDeck() throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		LeaderCard[] all_leader_cards = factory.getAllLeaderCards();

		Deck<LeaderCard> deck = new Deck<LeaderCard>(all_leader_cards.length);
		for (LeaderCard card: all_leader_cards) {
			deck.add(card);
		}
	 	deck.shuffle();
		return deck;
	}

	private void chooseMatchOrder() {
		return;
	}

	private void distributeRandomResources() {
		return;
	}

	/**
	 * @return the new Game
	 */
	private Game createGame() throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		DevelopmentCard[] all_development_cards = factory.getAllDevelopmentCards();
		return new Game(this.players, all_development_cards);
	}
}
