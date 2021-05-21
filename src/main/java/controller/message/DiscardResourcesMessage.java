package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

public class DiscardResourcesMessage implements Message {
	public DiscardResourcesMessage(){
	}

	public void useMessage(Controller controller){
		controller.handleDiscardResources(this.player);
	}
}
