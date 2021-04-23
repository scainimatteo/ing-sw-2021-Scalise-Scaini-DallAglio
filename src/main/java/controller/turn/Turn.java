package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceHandler;

import it.polimi.ingsw.util.Observable;

public abstract class Turn extends Observable<FaithController> {
	protected Player player;
	protected ChoiceHandler handler;

	protected FaithController playAction(){
		return null;
	}
	
	public void playTurn(){
		return;
	}
	
	public LeaderAbility checkAbility(LeaderAbility ability){
		return null;
	}

	protected FaithController discardLeaderCard(boolean[] whichLeaderCard){
		return this.player.discardLeaderCard(whichLeaderCard);
	}
}
