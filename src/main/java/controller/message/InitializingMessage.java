package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.InitialController;
import it.polimi.ingsw.controller.Controller;

public class InitializingMessage extends Message {
	public String message;

	public InitializingMessage(String message) {
		this.message = message;
	}

	public void useMessage(Controller controller) {
		InitialController initial_controller = (InitialController) controller;
		initial_controller.setReceivedMessage(this.message);
	}
}
