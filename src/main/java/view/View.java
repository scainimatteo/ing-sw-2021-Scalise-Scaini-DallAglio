package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

public abstract class View {
	protected SimpleGame simple_game;
	protected SimplePlayer[] simple_players;

	protected boolean initializing = true;
	protected boolean nickname_flag = true;
	protected String nickname;

	public abstract void startView(Client client);

	public abstract void updateView();

	public abstract void handleError(ErrorMessage error_message);

	public abstract void handleInitializing(InitializingServerMessage initializing_message);

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
