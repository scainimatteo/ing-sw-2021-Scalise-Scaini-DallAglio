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

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.sologame.SoloGame;
import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.track.SoloFaithTrack;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.server.ClientHandler;

public class Initializer {
	protected ArrayList<Player> players;
	private RemoteView[] remote_views;

	/**
	 * Initialize a Game:
	 *   - create the Players and the RemoteViews from the ClientHandlers
	 *   - distribute 4 LeaderCards to each Player
	 *   - choose randomly the first Player
	 *   - move the third and fourth Player forward on their FaithTracks
	 *   - add the RemoteViews as Observers
	 *   - send the Model to the Clients using the ModelObservers
	 *
	 * @param clients the ClientHandlers of the Players
	 * @return the new Game
	 */
	public Game initializeGame(ArrayList<ClientHandler> clients) throws InstantiationException {
		try {
			createPlayers(clients);
			distributeLeaderCards();
			chooseMatchOrder();
			movePlayerForward();
			Game game = createGame();
			addRemoteViews(game);
			game.notifyGame();
			for (Player p: this.players) {
				p.notifyPlayer();
			}
			game.getTurn().notifyTurn();
			return game;
		} catch (Exception e) {
			throw new InstantiationException();
		}
	}

	/**
	 * Initialize a SoloGame:
	 *   - create the Player and the RemoteView from the ClientHandler
	 *   - distribute 4 LeaderCards to the Player
	 *   - add the RemoteView as Observer
	 *   - send the Model to the Client using the ModelObserver
	 *
	 * @param clients the ClientHandler of the Player
	 * @return the new SoloGame
	 */
	public SoloGame initializeSoloGame(ArrayList<ClientHandler> clients) throws InstantiationException {
		try {
			createSoloPlayer(clients);
			distributeLeaderCards();
			SoloGame game = createSoloGame();
			addRemoteViews(game);
			game.notifyGame();
			this.players.get(0).notifyPlayer();
			game.getTurn().notifyTurn();
			return game;
		} catch (Exception e) {
			throw new InstantiationException();
		}
	}

	/**
	 * Persistence
	 * Recreate a Game:
	 *   - create the RemoteViews from the ClientHandlers
	 *   - add the RemoteViews as ModelObservers
	 *   - send the Model to the Clients using the ModelObservers
	 *
	 * @param clients the ClientHandlers of the Players
	 * @param game the Game parsed from the json file
	 */
	public void initializePersistenceGame(ArrayList<ClientHandler> clients, Game game) {
		this.players = game.getPlayers();
		this.remote_views = new RemoteView[players.size()];

		for (int i = 0; i < clients.size(); i++) {
			this.remote_views[i] = new RemoteView(clients.get(i));
			clients.get(i).setPlayer(this.players.get(i));
		}

		for (int i = 0; i < this.players.size(); i++) {
			for (int j = 0; j < this.remote_views.length; j++) {
				this.players.get(i).addModelObserver(this.remote_views[j]);
			}
		}

		addRemoteViews(game);
		game.notifyGame();
		for (Player p: this.players) {
			p.notifyPlayer();
		}
		game.getTurn().notifyTurn();
	}

	/**
	 * Persistence
	 * Recreate a SoloGame:
	 *   - create the RemoteView from the ClientHandler
	 *   - add the RemoteView as ModelObserver
	 *   - send the Model to the Client using the ModelObserver
	 *
	 * @param clients the ClientHandlers of the Players
	 * @param game the SoloGame parsed from the json file
	 */
	public void initializePersistenceSoloGame(ArrayList<ClientHandler> clients, SoloGame game) {
		this.players = game.getPlayers();
		this.remote_views = new RemoteView[1];

		this.remote_views[0] = new RemoteView(clients.get(0));
		clients.get(0).setPlayer(this.players.get(0));

		this.players.get(0).addModelObserver(this.remote_views[0]);

		addRemoteViews(game);
		game.notifyGame();
		this.players.get(0).notifyPlayer();
		game.getTurn().notifyTurn();
	}

	/**
	 * Create an array of Players using the ClientHandlers
	 *
	 * @param clients the ClientHandlers of the Players
	 */
	protected void createPlayers(ArrayList<ClientHandler> clients) throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		Cell[] all_cells = factory.getAllCells();

		this.players = new ArrayList<Player>();
		this.remote_views = new RemoteView[clients.size()];

		for (int i = 0; i < clients.size(); i++) {
			// every time create a different array of cloned Tiles
			Tile[] all_tiles = factory.getAllTiles();
			players.add(new Player(clients.get(i).getNickname(), new FaithTrack(all_cells, all_tiles)));
			this.remote_views[i] = new RemoteView(clients.get(i));
			clients.get(i).setPlayer(this.players.get(i));
		}

		for (int i = 0; i < this.players.size(); i++) {
			for (int j = 0; j < this.remote_views.length; j++) {
				this.players.get(i).addModelObserver(this.remote_views[j]);
			}
		}
	}

	/**
	 * Create a SoloPlayer using the ClientHandler
	 *
	 * @param clients the ClientHandler of the Player
	 */
	protected void createSoloPlayer(ArrayList<ClientHandler> clients) throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		Cell[] all_cells = factory.getAllCells();
		Tile[] all_tiles = factory.getAllTiles();

		this.players = new ArrayList<Player>();
		this.remote_views = new RemoteView[1];

		players.add(new SoloPlayer(clients.get(0).getNickname(), new SoloFaithTrack(all_cells, all_tiles)));
		this.remote_views[0] = new RemoteView(clients.get(0));
		clients.get(0).setPlayer(this.players.get(0));

		this.players.get(0).addModelObserver(this.remote_views[0]);
	}

	/**
	 * Distribute the starting LeaderCards to the Players
	 */
	protected void distributeLeaderCards() throws ParseException, IOException {
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
	protected void chooseMatchOrder() {
		Collections.shuffle(players);
	}

	/**
	 * Move the third and the fourth player one space forward
	 */
	protected void movePlayerForward() {
		try {
			this.players.get(2).moveForward(1);
			this.players.get(3).moveForward(1);
		} catch (IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Add the RemoteViews as ModelObserver of Game
	 */
	private void addRemoteViews(Game game) {
		for (RemoteView r: this.remote_views) {
			game.addModelObserver(r);
			game.getTurn().addModelObserver(r);
		}
	}

	/**
	 * @return the new Game
	 */
	protected Game createGame() throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		DevelopmentCard[] all_development_cards = factory.getAllDevelopmentCards();
		return new Game(this.players, all_development_cards);
	}

	/**
	 * @return the new SoloGame
	 */
	protected SoloGame createSoloGame() throws ParseException, IOException {
		Factory factory = Factory.getIstance();
		DevelopmentCard[] all_development_cards = factory.getAllDevelopmentCards();
		SoloActionToken[] all_solo_action_tokens = factory.getAllSoloActionTokens();
		return new SoloGame(this.players, all_development_cards, all_solo_action_tokens);
	}
}
