package it.polimi.ingsw.controller.match;

import java.util.ArrayList;

import it.polimi.ingsw.controller.turn.Turn;

import it.polimi.ingsw.controller.match.Initializer;

import it.polimi.ingsw.controller.util.CommunicationController;
import it.polimi.ingsw.controller.util.ChoiceController;

import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.ClientHandler;

public class Match implements Runnable {
	private CommunicationController comm_controller;
	private ChoiceController choice_controller;
	private Game game;

	public Match(ArrayList<ClientHandler> clients) {
		this.comm_controller = new CommunicationController();
		this.choice_controller = new ChoiceController(this.comm_controller);
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
		//TODO: if not used, move into the for
		Player[] players = this.game.getPlayers();
		boolean last_round = false;

		while (last_round) {
			for (Player p: players) {
				Turn turn = choice_controller.pickTurn(p, this.game.getDevelopmentCardsOnTable(), this.game.getMarket());
				turn.playTurn();

				last_round = checkLastRound();
			}
		}
		//TODO: last round stuff
	}

	public boolean checkLastRound() {
		//TODO: last round stuff
		return false;
	}
}
