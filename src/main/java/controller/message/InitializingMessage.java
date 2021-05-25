package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.InitialController;
import it.polimi.ingsw.controller.Controller;

import java.io.Serializable;

public class InitializingMessage extends Message implements Serializable {
	protected static final long serialVersionUID = 976756L;
	public String message;

	public InitializingMessage(String message) {
		this.message = message;
	}

	public void useMessage(Controller controller) {
		InitialController initial_controller = (InitialController) controller;
		initial_controller.setReceivedMessage(this.message);
	}
}
