package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.message.Message;

public interface Controller {
	public void handleMessage(Message message);
}
