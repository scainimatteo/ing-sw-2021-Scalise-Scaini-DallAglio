package it.polimi.ingsw.controller;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

import java.lang.IndexOutOfBoundsException;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.controller.RemoteView;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.server.ClientHandler;

public class Initializer {
	private Player[] players;
	private RemoteView[] remote_views;

	/**
	 * Starting procedure for the match:
	 *   - create the Players and the RemoteViews from the ClientHandlers
	 *   - distribute 4 LeaderCards to each player
	 *   - choose randomly the first player
	 *
	 * @param clients the ClientHandlers of the Players
	 * @return the new Game
	 */
	public Game initializeGame(ArrayList<ClientHandler> clients) throws InstantiationException {
		try {
			createPlayers(clients);
			distributeLeaderCards();
			chooseMatchOrder();
			Game game = createGame();
			addRemoteViews(game);
			return createGame();
		} catch (Exception e) {
			//TODO: too generic?
			e.printStackTrace();
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
		this.remote_views = new RemoteView[clients.size()];

		for (int i = 0; i < clients.size(); i++) {
			this.players[i] = new Player(clients.get(i).getNickname(), all_cells, all_tiles);
			this.remote_views[i] = new RemoteView(this.players[i], clients.get(i));
			this.players[i].addObserver(this.remote_views[i]);
			clients.get(i).setPlayer(this.players[i]);
		}
	}

	/**
	 * Distribute the starting LeaderCards to the Players
	 */
	private void distributeLeaderCards() throws ParseException, IOException {
		Deck<LeaderCard> all_leader_cards = getLeaderCardDeck();

		for (Player p: this.players) {
			// pick 4 LeaderCards from the Deck
			ArrayList<LeaderCard> leader_cards = new ArrayList<LeaderCard>();
			for (int i = 0; i < 4; i++) {
				leader_cards.add(all_leader_cards.draw());
			}
			p.setLeaderCards(leader_cards);
		}
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

	/**
	 * Shuffle the array of player to choose a random order
	 */
	private void chooseMatchOrder() {
		List<Player> players_list = Arrays.asList(this.players);
		Collections.shuffle(players_list);
		this.players = players_list.toArray(new Player[this.players.length]);
	}

	/**
	 * Add the RemoteViews as Observer of Game
	 */
	private void addRemoteViews(Game game) {
		for (RemoteView r: this.remote_views) {
			game.addObserver(r);
		}
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