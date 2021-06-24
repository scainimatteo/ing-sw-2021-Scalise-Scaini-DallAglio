package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.Observer;

public class RemoteView implements Observer {
	private ClientHandler client_handler;

	public RemoteView(ClientHandler client_handler) {
		this.client_handler = client_handler;
	}

	/**
	 * Send a message to the specific Client
	 *
	 * @param servermessage the ServerMessage to send
	 */
	public void update(ServerMessage servermessage) {
		this.client_handler.sendToClient(servermessage);
	}
}
