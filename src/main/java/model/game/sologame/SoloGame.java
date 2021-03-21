package it.polimi.ingsw.model.game.sologame;

import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.Game;

public class SoloGame extends Game {
	private SoloActionToken[] solo_action_tokens;
	private ArrayDeque<SoloActionToken> active_tokens;

	public SoloGame() {
		super(1);
		this.solo_action_tokens = new SoloActionToken[7];
		this.solo_action_tokens[0] = SoloActionToken.MOVE_BLACK_CROSS_TWO_SPACES;
		this.solo_action_tokens[1] = SoloActionToken.MOVE_BLACK_CROSS_TWO_SPACES;
		this.solo_action_tokens[2] = SoloActionToken.MOVE_BLACK_CROSS_ONE_SPACE;
		this.solo_action_tokens[3] = SoloActionToken.DISCARD_YELLOW_DEVELOPMENT_CARD;
		this.solo_action_tokens[4] = SoloActionToken.DISCARD_GREEN_DEVELOPMENT_CARD;
		this.solo_action_tokens[5] = SoloActionToken.DISCARD_PURPLE_DEVELOPMENT_CARD;
		this.solo_action_tokens[6] = SoloActionToken.DISCARD_BLUE_DEVELOPMENT_CARD;
		shuffleSoloActionTokens();
	}

	/**
	 * Shuffle the tokens and put them in a Queue
	 */
	public void shuffleSoloActionTokens() {
		// convert the array to a List, shuffle it then convert it back to an array
		List<SoloActionToken> solo_action_tokens_list = Arrays.asList(this.solo_action_tokens);
        Collections.shuffle(solo_action_tokens_list);
		solo_action_tokens_list.toArray(this.solo_action_tokens);

		// insert the new shuffled array in a Queue
		this.active_tokens = new ArrayDeque<SoloActionToken>(Arrays.asList(this.solo_action_tokens));
	}

	/**
	 * @return SoloActionToken on top of the Queue
	 */
	public SoloActionToken getTopToken() {
		return this.active_tokens.pop();
	}
}
