package it.polimi.ingsw.view;

import java.util.ArrayList;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

public abstract class View {
	protected SimpleGame simple_game;
	protected ArrayList<SimplePlayer> simple_players;
	protected Turn turn;

	protected boolean initializing = true;
	protected boolean nickname_flag = true;
	protected String nickname;

	public abstract void startView(Client client);

	/**
	 * Print the error message
	 *
	 * @param error_message the ErrorMessage received from the Server
	 */
	public abstract void handleError(ErrorMessage error_message);

	/**
	 * Print the message during the initalization phase
	 *
	 * @param initializing_message the InitializingServerMessage received from the Server
	 */
	public abstract void handleInitializing(InitializingServerMessage initializing_message);

	/**
	 * Update the simple model after a ViewUpdate
	 */
	public void updateView(ViewUpdate view_update) {
		if (view_update.simple_game != null) {
			this.simple_game = view_update.simple_game;
		} else if (view_update.simple_player != null) {
			updateSimplePlayer(view_update.simple_player);
		} else if (view_update.turn != null) {
			this.turn = view_update.turn;
		}
	}

	/**
	 * Change the specific SimplePlayer in the simple_players array
	 *
	 * @param simple_player the SimplePlayer to update
	 */
	private void updateSimplePlayer(SimplePlayer simple_player) {
		String nickname = simple_player.getNickname();
		if (this.simple_players.isEmpty()) {
			this.simple_players.add(simple_player);
			return;
		}

		for (int i = 0; i < this.simple_players.size(); i++) {
			if (this.simple_players.get(i).getNickname().equals(nickname)) {
				this.simple_players.set(i, simple_player);
				break;
			}
		}
	}

	/**
	 * Get the SimplePlayer of the Client
	 *
	 * @return the SimplePlayer of the Client
	 */
	public SimplePlayer getMyPlayer() {
		if (this.simple_players == null) {
			return null;
		}

		for (SimplePlayer s: this.simple_players) {
			if (s.getNickname().equals(this.nickname)) {
				return s;
			}
		}
		// unreachable
		return null;
	}
}
