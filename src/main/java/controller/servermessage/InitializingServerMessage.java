package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.InitializingMessageType;
import it.polimi.ingsw.controller.servermessage.ServerMessage;

import java.io.Serializable;

public class InitializingServerMessage extends ServerMessage implements Serializable {
	protected static final long serialVersionUID = 976756L;
	public String message;
	public InitializingMessageType type;
	public String match_name;

	public InitializingServerMessage(String message, InitializingMessageType type) {
		this.initializing = true;
		this.message = message;
		this.type = type;
	}

	public InitializingServerMessage(String message, InitializingMessageType type, String match_name) {
		this(message, type);
		this.match_name = match_name;
	}
}
