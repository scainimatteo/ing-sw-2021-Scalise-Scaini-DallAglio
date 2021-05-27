package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.Observer;

import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

public class RemoteView implements Observer {
	private Player player;
	private ClientHandler client_handler;

	public RemoteView(Player player, ClientHandler client_handler) {
		this.player = player;
		this.client_handler = client_handler;
	}

	public void update(ServerMessage servermessage) {
		if (servermessage.error) {
			((ErrorMessage) servermessage).setNickname(player.getNickname());
		}
		this.client_handler.sendToClient(servermessage);
	}
}
