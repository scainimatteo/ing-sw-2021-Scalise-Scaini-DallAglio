package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.controller.util.FaithController;

public abstract class Turn {
	protected Player player;

	protected FaithController playAction(){
		return null;
	}
	
	public void playTurn(){
		return;
	}
	
	public LeaderAbility checkAbility(LeaderAbility ability){
		return null;
	}
}
