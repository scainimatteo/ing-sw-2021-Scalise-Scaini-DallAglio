package it.polimi.ingsw.controller.util;

import java.io.Serializable;

public class Choice implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean response;
	private String message;

	public Choice(String message) {
		this.message = message;
	}

	public boolean getResponse() {
		return this.response;
	}

	public String getMessage() {
		return this.message;
	}

	public void setResponse(boolean response) {
		this.response = response;
	}
}
