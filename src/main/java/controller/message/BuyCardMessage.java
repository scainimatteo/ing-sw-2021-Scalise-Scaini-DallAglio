package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.GameController;

public class BuyCardMessage implements TurnMessage {
	public int slot;

	public void useMessage(GameController controller) {
		return;
	}
}
