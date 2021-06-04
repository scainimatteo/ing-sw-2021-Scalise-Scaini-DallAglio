package it.polimi.ingsw.controller.servermessage;

import java.util.HashMap;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import java.io.Serializable;

public class EndGameMessage extends ServerMessage implements Serializable {
	protected static final long serialVersionUID = 334812L;
	public HashMap<String, Integer> rank = new HashMap<String, Integer>();

	public EndGameMessage(HashMap<String, Integer> rank) {
		this.end_game = true;
		this.rank = rank;
	}
}
