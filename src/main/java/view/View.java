package it.polimi.ingsw.view;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;

public interface View {
	public void startView(Client client);

	public void updateView();

	public void handleError(ErrorMessage error_message);

	public void handleInitializing(InitializingServerMessage initializing_message);
}
