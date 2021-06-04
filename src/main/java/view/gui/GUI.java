package it.polimi.ingsw.view.gui;

import java.util.ArrayList;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.EndGameMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.gui.App;
import it.polimi.ingsw.view.View;

public class GUI extends View {
	@Override
	public void startView(Client client) {
		App.setModel(this);
		App.main(null);
	}

	@Override
	public void handleError(ErrorMessage error_message) {
		return;
	}

	@Override
	public void handleInitializing(InitializingServerMessage initializing_message) {
		return;
	}

	@Override
	public void handleEndGame(EndGameMessage end_game_message) {
		return;
	}

	public SimpleGame getSimpleGame() {
		return this.simple_game;
	}

	public ArrayList<SimplePlayer> getSimplePlayers() {
		return this.simple_players;
	}

	public Turn getTurn() {
		return this.turn;
	}
}
