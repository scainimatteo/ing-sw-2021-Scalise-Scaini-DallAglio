package it.polimi.ingsw.model.game.sologame;

import it.polimi.ingsw.controller.SoloGameController;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import java.io.Serializable;

public class MoveBlackCrossTwoSpaces implements SoloActionToken, Serializable {
	private static final long serialVersionUID = 7713L;

	@Override
	public void useToken(SoloGameController controller) {
		controller.moveBlackCrossTwoSpaces();
	}

	@Override
	public String toString() {
		return "   _  ___\n _| ||_  )\n|_   _/ /\n  |_|/___|\n";
	}
}

/*
   _  ___
 _| ||_  )
|_   _/ /
  |_|/___|
*/
