package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

public class ViewUpdate extends ServerMessage {
	protected static final long serialVersionUID = 7736L;
	public SimpleGame simple_game;
	public SimplePlayer simple_player;
	public Turn turn;

	public ViewUpdate(SimpleGame simple_game, SimplePlayer simple_player, Turn turn) {
		this.simple_game = simple_game;
		this.simple_player = simple_player;
		this.turn = turn;
	}
}
