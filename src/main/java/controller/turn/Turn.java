package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.controller.util.LeaderCardController;
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
		LeaderCardController leader_card_controller = new LeaderCardController();
		FaithController returned_faith_controller = null;
		boolean[] returned_booleans;

		returned_booleans = leader_card_controller.playLeaderCard(this.player);
		returned_faith_controller = this.player.discardLeaderCard(returned_booleans);
		checkNotify(returned_faith_controller);
	
		returned_faith_controller = playAction();
		checkNotify(returned_faith_controller);

		returned_booleans = leader_card_controller.playLeaderCard(this.player);
		returned_faith_controller = this.player.discardLeaderCard(returned_booleans);
		checkNotify(returned_faith_controller);
	}
	
	public LeaderAbility checkAbility(LeaderAbility ability){
		return null;
	}

	public void checkNotify(FaithController faith_controller){
		if (faith_controller.getGainedFaith() > 0 || faith_controller.getToRedistributeFaith() > 0){
			notify(faith_controller);
		} 
	}
}
