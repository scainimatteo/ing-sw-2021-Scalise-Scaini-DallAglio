package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.observer.ModelObserver;

public class RemoteView implements ModelObserver {
	private ClientHandler client_handler;

	public RemoteView(ClientHandler client_handler) {
		this.client_handler = client_handler;
	}

	/**
	 * Send a message to the specific Client
	 *
	 * @param servermessage the ServerMessage to send
	 */
	public void updateModel(ServerMessage servermessage) {
		this.client_handler.sendToClient(servermessage);
	}
}
