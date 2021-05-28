package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

import java.io.Serializable;

public class StoreMessage extends ResourceMessage implements Serializable {
	private static final long serialVersionUID = 99772L;

	public StoreMessage(Storage storage){
		this.storage = storage;
	}

	public void useMessage(Controller controller){
		controller.handleStore(this.player, this.storage);
	}
}
