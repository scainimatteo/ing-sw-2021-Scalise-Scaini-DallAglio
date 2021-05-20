package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.GameController;

public class MarketMessage implements TurnMessage{
	public boolean row_or_column = false;

	public void useMessage(GameController controller){
		return;
	}
}
