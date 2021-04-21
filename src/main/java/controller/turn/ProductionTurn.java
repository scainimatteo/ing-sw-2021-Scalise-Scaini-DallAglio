package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.controller.util.FaithController;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.resources.Production;

public class ProductionTurn extends Turn{
	
	public ProductionTurn(Player player){
		this.player = player;
	}

	private Production chooseProduction(){
		return null;
	}

	private boolean[] checkProductionAbility(LeaderCard[] leader_card){
		boolean[] to_return = {false, false};

		return to_return;
	}

	@Override
	protected FaithController playAction(){
		return null;
	}
}
