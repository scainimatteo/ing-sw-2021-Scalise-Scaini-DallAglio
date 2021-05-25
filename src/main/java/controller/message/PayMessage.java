package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public class PayMessage extends ResourceMessage {
	Storage storage;

	public PayMessage(Storage storage) {
		this.storage = storage;
	}

	public void useMessage(Controller controller) {
		controller.handlePay(this.player, this.storage);
	}
}
