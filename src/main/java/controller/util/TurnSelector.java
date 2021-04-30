package it.polimi.ingsw.controller.util;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class TurnSelector implements Serializable {
	private static final long serialVersionUID = 10L;
	private final String message = "Pick a turn";
	private int chosen;

	public String getMessage() {
		return this.message;
	}

	public int getChosen() {
		return this.chosen;
	}

	public boolean setChosen(int chosen_turn) {
		if (chosen_turn > 0 && chosen_turn < 4) {
			this.chosen = chosen_turn;
			return true;
		}

		return false;
	}
}
