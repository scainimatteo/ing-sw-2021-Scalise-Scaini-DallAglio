package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public class AssignedResourceMessage extends ResourceMessage {
	public AssignedResourceMessage (Storage storage){
		this.storage = storage;
	}

	public void useMessage(Controller controller){
		controller.handleInitStore(this.player, this.storage);
	}
}
