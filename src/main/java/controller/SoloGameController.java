package it.polimi.ingsw.controller;

import java.util.ArrayList;

import it.polimi.ingsw.controller.GameController;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.sologame.SoloGame;

import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.ClientHandler;

public class SoloGameController extends GameController {

	public SoloGameController(ArrayList<ClientHandler> clients) {
		super(clients);
	}

	/**
	 * Initialize the SoloGame using the Initializer
	 *
	 * @throws InstantiationException when the Initializer fails
	 */
	@Override
	public void initializeGame() throws InstantiationException {
		try {
			super.game = new Initializer().initializeSoloGame(super.clients);
		} catch (InstantiationException e) {
			System.out.println("SoloGame could not start");
			throw new InstantiationException();
		}
	}

	@Override
	protected void checkLastTurn(Player player) {
		return;
	}

	@Override
	public void handleEndTurn(Player player) {
		SoloActionToken last_token = ((SoloGame) super.game).getTopToken();
	}
}
