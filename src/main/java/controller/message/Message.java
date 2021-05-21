package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

public abstract class Message {
	public Player player;

	public void setPlayer(Player player){
		this.player = player;
	}

	public void useMessage(Controller controller);
}
