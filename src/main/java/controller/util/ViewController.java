package it.polimi.ingsw.controller.util;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.controller.util.ViewMessage;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.view.Viewable;

public class ViewController {
	ClientHandler client;
	Player[] players;
	Game game;

	public ViewController(ClientHandler client, Game game) {
		this.client = client;
		this.game = game;
		this.players = game.getPlayers();
	}

	/**
	 * Handle the ViewRequests creating the ViewReplies
	 *
	 * @param message the ViewRequest to handle
	 */
	public void handleViewRequest(ViewMessage message) {
		Viewable reply = null;

		switch(message.getViewType()) {
			case FAITHTRACK:
				reply = handleFaithTrack(message.getNickname());
				break;
			case MARKET:
				reply = handleMarket();
				break;
			case DEVELOPMENTCARDSONTABLE:
				reply = handleDevelopmentCardsOnTable();
				break;
			case STRONGBOX:
				reply = handleStrongBox(message.getNickname());
				break;
			default:
				System.out.println("An error occurred");
				throw new IllegalArgumentException();
		}
		
		sendViewReply(message, reply);
	}

	/**
	 * @param nickname the nickname of the Player
	 * @return the Player with that nickname
	 */
	private Player getPlayerFromNickname(String nickname) {
		Player player = null;
		for (int i = 0; i < this.players.length; i++) {
			if (players[i].getNickname().equals(nickname)) {
				player = players[i];
				break;
			}
		}

		if (player == null) {
			throw new IllegalArgumentException();
		}
		return player;
	}

	/**
	 * Print the FaithTrack of the player identified by the nickname
	 *
	 * @param nickname the Player to get the FaithTrack from
	 * @return the FaithTrack of that Player
	 */
	private FaithTrack handleFaithTrack(String nickname) {
		if (nickname == null) {
			// the client it's asking for his own FaithTrack
			return getPlayerFromNickname(this.client.getNickname()).getFaithTrack();
		} else {
			return getPlayerFromNickname(nickname).getFaithTrack();
		}
	}

	private Market handleMarket() {
		return this.game.getMarket();
	}

	private DevelopmentCardsOnTable handleDevelopmentCardsOnTable() {
		return this.game.getDevelopmentCardsOnTable();
	}

	private StrongBox handleStrongBox(String nickname) {
		if (nickname == null) {
			// the client it's asking for his own StrongBox
			return getPlayerFromNickname(this.client.getNickname()).getPlayerStrongBox();
		} else {
			return getPlayerFromNickname(nickname).getPlayerStrongBox();
		}
	}

	/**
	 * @param request the corresponding ViewRequest
	 * @param reply the Viewable to put in the ViewReply
	 */
	private void sendViewReply(ViewMessage request, Viewable reply) {
		request.setReply(reply);
		Message message = new Message(MessageType.VIEWREPLY, request);
		this.client.addViewReply(message);
	}
}
