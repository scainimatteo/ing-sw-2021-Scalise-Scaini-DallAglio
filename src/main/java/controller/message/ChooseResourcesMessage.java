package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

import java.io.Serializable;

public class ChooseResourcesMessage extends ResourceMessage implements Serializable {
	private static final long serialVersionUID = 85643L;

	public ChooseResourcesMessage(Storage storage){
		this.storage = storage;
	}

	public void useMessage(Controller controller){
		controller.handleChooseResources(this.player, this.storage);
	}
}
