package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import java.io.Serializable;

public class EndTurnMessage extends Message implements Serializable {
	private static final long serialVersionUID = 323444L;

	public EndTurnMessage(){
	}

	public void useMessage(Controller controller) {
		controller.handleEndTurn(this.player);
	}
}
