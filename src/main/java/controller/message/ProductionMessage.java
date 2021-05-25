package it.polimi.ingsw.controller.message;

import java.util.ArrayList;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.player.Player;

public class ProductionMessage extends TurnMessage {
	public ArrayList<ProductionInterface> productions;

	public ProductionMessage(ArrayList<ProductionInterface> productions){
		this.productions = productions;
	}

	public void useMessage(Controller controller) {
		controller.handleProduction(this.player, this.productions);
	}
}
