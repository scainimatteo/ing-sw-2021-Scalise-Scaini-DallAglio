package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.Observer;

public class RemoteView implements Observer {
	private ClientHandler client_handler;
	private Turn turn;

	public RemoteView(ClientHandler client_handler) {
		this.client_handler = client_handler;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	/**
	 * Send a message to the specific Client
	 *
	 * @param servermessage the ServerMessage to send
	 */
	public void update(ServerMessage servermessage) {
		if (servermessage.error) {
			((ErrorMessage) servermessage).setNickname(this.turn.getNickname());
		}
		this.client_handler.sendToClient(servermessage);
	}
}
