package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.GameController;

import it.polimi.ingsw.model.player.Player;

public interface Message {
	public Player player = null;

	public void useMessage(GameController controller);
}
