package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.resources.ProductionInterface;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

public class ProductionMessage extends TurnMessage {
	public ProductionInterface production;

	public ProductionMessage(ProductionInterface production){
		this.production = production;
	}

	public void useMessage(Controller controller) {
		controller.handleProduction(this.player, this.production);
	}
}
