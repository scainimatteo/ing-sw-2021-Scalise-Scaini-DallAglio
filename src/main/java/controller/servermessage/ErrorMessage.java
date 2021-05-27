package it.polimi.ingsw.controller.servermessage;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public class ErrorMessage extends ServerMessage {
	protected static final long serialVersionUID = 224224L;
	public String nickname;
	public String error_string;

	public ErrorMessage(String error_string) {
		this.error = true;
		this.error_string = error_string;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
