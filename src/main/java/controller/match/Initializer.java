package it.polimi.ingsw.controller.match;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

import java.lang.IndexOutOfBoundsException;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.controller.util.CommunicationController;
import it.polimi.ingsw.controller.util.ChoiceController;
import it.polimi.ingsw.controller.util.FaithController;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.Observable;

public class Initializer extends Observable<FaithController> {
	private CommunicationController comm_controller;
	private ChoiceController choice_controller;
	private Player[] players;

	public Initializer(CommunicationController comm_controller, ChoiceController choice_controller) {
		this.comm_controller = comm_controller;
		this.choice_controller = choice_controller;
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
			asyncDistributeLeaderCards();
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

	//TODO: Test a lot
	/**
	 * Distribute the starting LeaderCards to each Player asynchronously
	 */
	private void asyncDistributeLeaderCards() throws ParseException, IOException, InterruptedException {
		Deck<LeaderCard> all_leader_cards = getLeaderCardDeck();

		// start a thread for each player
		Thread[] threads = new Thread[this.players.length];
		int i = 0;
		for (Player p: this.players) {
			// pick 4 LeaderCards from the Deck
			LeaderCard[] leader_cards = new LeaderCard[4];
			for (int j = 0; j < 4; j++) {
				leader_cards[j] = all_leader_cards.draw();
			}

			threads[i] = new Thread(() -> distributeLeaderCards(p, leader_cards));
			threads[i].start();
			i++;
		}

		// join all the threads
		for (Thread t: threads) {
			t.join();
		}
	}

	/**
	 * Distribute the starting LeaderCards to a Player
	 *
	 * @param p the Player to distribute the LeaderCards to
	 * @param leader_cards an array containg the 4 random LeaderCards
	 */
	private void distributeLeaderCards(Player p, LeaderCard[] leader_cards) {
		Object[] chosen = this.choice_controller.pickBetween(p, "Choose two leader cards", leader_cards, 2);
		LeaderCard[] chosen_leader_cards = new LeaderCard[2];
		for (int i = 0; i < chosen.length; i++) {
			chosen_leader_cards[i] = (LeaderCard) chosen[i];
		}
		p.setLeaderCards(chosen_leader_cards);
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
		List<Player> players_list = Arrays.asList(this.players);
		Collections.shuffle(players_list);
		this.players = players_list.toArray(new Player[this.players.length]);
	}

	private void distributeRandomResources() {
		try {
			this.players[1].tryToInsert(getRandomResources(1));

			addObservers(this.players[2]);
			this.players[2].tryToInsert(getRandomResources(2));
			notify(new FaithController(this.players[2], 1, 0));

			addObservers(this.players[3]);
			this.players[3].tryToInsert(getRandomResources(2));
			notify(new FaithController(this.players[3], 1, 0));
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private Resource[] getRandomResources(int num) {
		Random random = new Random();
		Resource[] all_resources = Resource.values();

		Resource[] resources = new Resource[num];
		for (int i = 0; i < num; i++) {
			int randint = random.nextInt(4);
			resources[i] = all_resources[randint];
		}
		return resources;
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
