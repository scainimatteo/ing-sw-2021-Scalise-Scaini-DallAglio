package it.polimi.ingsw.model.game.sologame;

import it.polimi.ingsw.controller.SoloGameController;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import java.io.Serializable;

public class MoveBlackCrossOneSpace implements SoloActionToken, Serializable {
	private static final long serialVersionUID = 78756L;

	@Override
	public void useToken(SoloGameController controller) {
		controller.moveBlackCrossOneSpace();
	}

	@Override
	public String toString() {
		return "   _   _\n _| |_/ |\n|_   _| |\n  |_| |_|\n";
	}

	@Override
	public String getPath() {
		return "/images/tokens/sologame/move_black_cross_one_space.png";
	}

	@Override
	public String getType() {
		return "MOVEBLACKCROSSONESPACE";
	}
}

/*
   _   _
 _| |_/ |
|_   _| |
  |_| |_|
*/
