package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.view.gui.App;
import it.polimi.ingsw.view.View;

public class GUI extends View {
	@Override
	public void startView(Client client) {
		App.main(null);
	}

	@Override
	public void handleError(ErrorMessage error_message) {
		return;
	}

	@Override
	public void handleInitializing(InitializingServerMessage initializing_message) {
		return;
	}
}
