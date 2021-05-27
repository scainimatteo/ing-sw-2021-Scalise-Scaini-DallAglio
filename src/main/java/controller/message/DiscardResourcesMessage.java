package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class DiscardResourcesMessage extends Message implements Serializable {
	private static final long serialVersionUID = 93443L;

	public DiscardResourcesMessage(){
	}

	public void useMessage(Controller controller){
		controller.handleDiscardResources(this.player);
	}
}
