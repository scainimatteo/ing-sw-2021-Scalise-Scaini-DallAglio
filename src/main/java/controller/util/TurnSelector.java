package it.polimi.ingsw.controller.util;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public class TurnSelector implements Serializable {
	private static final long serialVersionUID = 10L;
	private final String message = "Pick a turn:\n 1. BuyCardTurn\n2. MarketTurn\n3. ProductionTurn\n";
	private int chosen;

	public String getMessage() {
		return this.message;
	}

	public int getChosen() {
		return this.chosen;
	}

	/**
	 * Choose a number between 1 and 3 that represents a Turn
	 *
	 * @param chosen_turn the number chosen
	 * @return if the choice was successfull
	 */
	public boolean setChosen(int chosen_turn) {
		if (chosen_turn > 0 && chosen_turn < 4) {
			this.chosen = chosen_turn;
			return true;
		}

		return false;
	}
}
