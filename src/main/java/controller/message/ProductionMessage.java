package it.polimi.ingsw.controller.message;

import java.util.ArrayList;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class ProductionMessage extends TurnMessage implements Serializable {
	private static final long serialVersionUID = 99789L;
	public ArrayList<ProductionInterface> productions;

	public ProductionMessage(ArrayList<ProductionInterface> productions){
		this.productions = productions;
	}

	public void useMessage(Controller controller) {
		controller.handleProduction(this.player, this.productions);
	}
}
