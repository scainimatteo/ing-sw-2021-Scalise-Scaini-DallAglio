package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;

// import it.polimi.ingsw.controller.faithtrack.FaithController;

public abstract class Turn {
	protected Player player;

	public Turn(Player player){
		this.player = player;
	}

	// private FaithController playAction(){
	// }
	
	public void PlayTurn(){
	}
}
