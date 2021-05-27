package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import java.io.Serializable;

public class BuyCardMessage extends TurnMessage implements Serializable {
	private static final long serialVersionUID = 4321L;
	public int slot;

	public BuyCardMessage(int row, int column, int slot){
		this.row = row;
		this.column = column;
		this.slot = slot;
	}

	public void useMessage(Controller controller) {
		controller.handleBuyCard(this.player, this.row, this.column, this.slot);
	}
}
