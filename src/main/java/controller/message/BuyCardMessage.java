package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

public class BuyCardMessage implements TurnMessage {
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
