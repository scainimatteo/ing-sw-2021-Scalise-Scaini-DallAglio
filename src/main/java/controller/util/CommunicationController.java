package it.polimi.ingsw.controller.util;

import java.util.HashMap;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.ClientHandler;

public class CommunicationController {
	// since the controller only knows about players, it uses
	// this class to communicate with the client that "hides"
	// behind the player
	private HashMap<Player, ClientHandler> players;

	public CommunicationController(){
		this.players = new HashMap<Player, ClientHandler>();
	}

	/**
	 * @param player the player to insert in the map
	 * @param client the clienthandler corrisponding to the player
	 */
	public void insertPlayer(Player player, ClientHandler client) {
		this.players.put(player, client);
	}

	/**
	 * Send a message to a player
	 *
	 * @param player the player to send the message to
	 * @param message the message to send
	 */
	public void sendToPlayer(Player player, Object message) {
		this.players.get(player).asyncSendToClient(message);
	}

	/**
	 * Receive a message from a player
	 *
	 * @param player the player to receive the message from
	 * @return the message received from the player
	 */
	public Object receiveFromPlayer(Player player) {
		try {
			return this.players.get(player).asyncReceiveFromClient();
		} catch (InterruptedException e) {
			System.out.println("An error occurred while trying to communicate with the client");
		}
		return null;
	}
}
