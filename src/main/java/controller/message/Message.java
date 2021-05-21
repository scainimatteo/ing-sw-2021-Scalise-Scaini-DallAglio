package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

public interface Message {
	public Player player = null;

	public void useMessage(Controller controller);
}
