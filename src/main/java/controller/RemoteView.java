package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.Observer;

public class RemoteView implements Observer {
	private ClientHandler client_handler;

	public RemoteView(ClientHandler client_handler) {
		this.client_handler = client_handler;
	}

	public void update(ServerMessage servermessage) {
		if (servermessage.error) {
			//TODO: wrong, put the nickname of the active player
			((ErrorMessage) servermessage).setNickname(this.client_handler.getNickname());
		}
		this.client_handler.sendToClient(servermessage);
	}
}
