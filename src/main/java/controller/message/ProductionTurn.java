package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.resources.ProductionInterface;

import it.polimi.ingsw.controller.Controller;

public class ProductionTurn implements TurnMessage {
	public ProductionInterface production;

	public void useMessage(Controller controller) {
		return;
	}
}
