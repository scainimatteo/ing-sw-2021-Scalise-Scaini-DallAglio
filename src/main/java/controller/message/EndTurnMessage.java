package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

public class EndTurnMessage extends Message {
	public EndTurnMessage(){
	}

	public void useMessage(Controller controller) {
		controller.handleEndTurn(this.player);
	}
}