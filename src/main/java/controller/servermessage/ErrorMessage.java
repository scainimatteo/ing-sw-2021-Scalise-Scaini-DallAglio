package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.model.player.Player;

public class ErrorMessage extends ServerMessage {
	public Player player;
	public String error_string;

	public ErrorMessage(String error_string) {
		this.error = true;
		this.error_string = error_string;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
