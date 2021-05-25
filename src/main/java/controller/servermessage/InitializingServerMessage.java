package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import java.io.Serializable;

public class InitializingServerMessage extends ServerMessage implements Serializable {
	protected static final long serialVersionUID = 976756L;
	public String message;

	public InitializingServerMessage(String message) {
		this.initializing = true;
		this.message = message;
	}
}
