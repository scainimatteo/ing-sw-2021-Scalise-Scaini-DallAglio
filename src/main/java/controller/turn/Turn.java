package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.controller.util.LeaderCardController;
import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceHandler;

import it.polimi.ingsw.util.Observable;

public abstract class Turn extends Observable<FaithController> {
	protected Player player;
	protected ChoiceHandler handler;
	protected ExtraSpaceAbility[] extra_space;

	protected FaithController playAction(){
		return null;
	}

	/**
	 * Adds all extra space from the player's LeaderCard deck to the turn's available extra space
	 */
	private void checkExtraSpace(){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		int index = 0;
		for (LeaderCard card: player.getDeck()){
			if (card.isActive() && card.getAbility().checkAbility(test)){
 				extra_space[index] = (ExtraSpaceAbility) card.getAbility();
				index ++;
			}
		}
	}
	
	protected void payResources (Resource[] resources){}

	protected int storeResources (Resource[] resources){
		return 0;
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

	public void checkNotify(FaithController faith_controller){
		if (faith_controller.getGainedFaith() > 0 || faith_controller.getToRedistributeFaith() > 0){
			notify(faith_controller);
		} 
	}
}
