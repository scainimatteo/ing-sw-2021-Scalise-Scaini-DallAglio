package it.polimi.ingsw.controller.util;

import it.polimi.ingsw.controller.util.MessageType;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 100L;
	private MessageType message_type;
	private String message_string;
	private Object message;
	private boolean parsed = false;

	public Message(MessageType message_type, Object message) {
		this.message_type = message_type;
		this.message = message;
	}

	public MessageType getMessageType() {
		return this.message_type;
	}

	public Object getMessage() {
		return this.message;
	}

	public boolean isParsed() {
		return this.parsed;
	}

	public void setParsed() {
		this.parsed = true;
	}
}
