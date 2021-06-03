package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public class ErrorMessage extends ServerMessage {
	protected static final long serialVersionUID = 224224L;
	public String nickname;
	public String error_string;

	public ErrorMessage(String error_string, String nickname) {
		this.error = true;
		this.error_string = error_string;
		this.nickname = nickname;
	}
}
