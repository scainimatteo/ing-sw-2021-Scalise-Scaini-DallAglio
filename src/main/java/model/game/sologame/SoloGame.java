package it.polimi.ingsw.model.game.sologame;

import java.util.Collections;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.sologame.DiscardDevelopmentCards;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossTwoSpaces;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossOneSpace;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.Player;

public class SoloGame extends Game {
	private SoloActionToken[] solo_action_tokens;
	private ArrayDeque<SoloActionToken> active_tokens;

	public SoloGame(Player[] player, DevelopmentCard[] all_development_cards) {
		super(player, all_development_cards);
		this.solo_action_tokens = new SoloActionToken[7];
		this.solo_action_tokens[0] = new MoveBlackCrossTwoSpaces();
		this.solo_action_tokens[1] = new MoveBlackCrossTwoSpaces();
		this.solo_action_tokens[2] = new MoveBlackCrossOneSpace();
		this.solo_action_tokens[3] = new DiscardDevelopmentCards(DevelopmentCardsColor.GREEN);
		this.solo_action_tokens[4] = new DiscardDevelopmentCards(DevelopmentCardsColor.BLUE);
		this.solo_action_tokens[5] = new DiscardDevelopmentCards(DevelopmentCardsColor.PURPLE);
		this.solo_action_tokens[6] = new DiscardDevelopmentCards(DevelopmentCardsColor.YELLOW);
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
