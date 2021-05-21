package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public class InitializingServerMessage extends ServerMessage {
	public String message;

	public InitializingServerMessage(String message) {
		this.initializing = true;
		this.message = message;
	}
}
