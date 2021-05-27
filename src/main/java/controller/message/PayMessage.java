package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

import java.io.Serializable;

public class PayMessage extends ResourceMessage implements Serializable {
	private static final long serialVersionUID = 323225L;
	Storage storage;

	public PayMessage(Storage storage) {
		this.storage = storage;
	}

	public void useMessage(Controller controller) {
		controller.handlePay(this.player, this.storage);
	}
}
