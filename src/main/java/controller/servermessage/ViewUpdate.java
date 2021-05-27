package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

public class ViewUpdate extends ServerMessage {
	protected static final long serialVersionUID = 7736L;
	public SimpleGame simple_game = null;
	public SimplePlayer simple_player = null;
	public Turn turn = null;

	public ViewUpdate(SimpleGame simple_game) {
		this.simple_game = simple_game;
	}

	public ViewUpdate(SimplePlayer simple_player) {
		this.simple_player = simple_player;
	}

	public ViewUpdate(Turn turn) {
		this.turn = turn;
	}
}
