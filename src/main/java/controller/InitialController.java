package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.message.Message;

public class InitialController implements Controller {
	public void handleMessage(Message message) {
		message.useMessage(this);
	}

	//TODO: a method to get the input
}
