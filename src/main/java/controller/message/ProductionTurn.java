package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.resources.ProductionInterface;

import it.polimi.ingsw.controller.GameController;

public class ProductionTurn implements TurnMessage {
	public ProductionInterface production;

	public void useMessage(GameController controller) {
		return;
	}
}
