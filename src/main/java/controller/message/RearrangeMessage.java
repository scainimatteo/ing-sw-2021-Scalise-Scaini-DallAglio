package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class RearrangeMessage extends Message implements Serializable {
	private static final long serialVersionUID = 89700L;
	public int swap1 = 0;
	public int swap2 = 0;

	public RearrangeMessage(int swap1, int swap2){
		this.swap1 = swap1;
		this.swap2 = swap2;
	}

	public void useMessage(Controller controller) {
		controller.handleRearrange(this.player, this.swap1, this.swap2);
	}
}
