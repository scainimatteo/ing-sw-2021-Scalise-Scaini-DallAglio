package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public abstract class Message implements Serializable {
	private static final long serialVersionUID = 442L;
	public Player player;

	public void setPlayer(Player player){
		this.player = player;
	}

	public abstract void useMessage(Controller controller);
}
