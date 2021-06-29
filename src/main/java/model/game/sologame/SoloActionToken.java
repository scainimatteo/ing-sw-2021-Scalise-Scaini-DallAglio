package it.polimi.ingsw.model.game.sologame;

import it.polimi.ingsw.controller.SoloGameController;

public interface SoloActionToken {
	/**
	 * Use the right method of the SoloGameController using the Visitor pattern
	 *
	 * @param controller the SoloGameController that uses the SoloActionTokens
	 */
	public void useToken(SoloGameController controller);

	/**
	 * Return a string representing the SoloActionToken
	 *
	 * Used by the CLI
	 */
	public String toString();

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public String getType();
}
