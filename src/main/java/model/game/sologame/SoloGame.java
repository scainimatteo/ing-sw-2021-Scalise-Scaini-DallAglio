package it.polimi.ingsw.model.game.sologame;

import java.util.Collections;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.controller.servermessage.EndGameMessage;
import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.sologame.DiscardDevelopmentCards;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossTwoSpaces;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossOneSpace;
import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.view.simplemodel.SimpleSoloGame;

public class SoloGame extends Game {
	private SoloActionToken[] solo_action_tokens;
	private ArrayDeque<SoloActionToken> active_tokens;
	private SoloActionToken last_token;
	private SoloPlayer player;

	public SoloGame(ArrayList<Player> player, DevelopmentCard[] all_development_cards, SoloActionToken[] all_solo_action_tokens) {
		super(player, all_development_cards);
		this.player = (SoloPlayer) player.get(0);
		this.solo_action_tokens = all_solo_action_tokens;
		shuffleSoloActionTokens();
	}

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public SoloGame(ArrayList<Player> player, Market market, DevelopmentCardsOnTable development_cards_on_table, Turn turn, SoloActionToken[] all_solo_action_tokens, ArrayDeque<SoloActionToken> active_tokens, SoloActionToken last_token) {
		super(player, market, development_cards_on_table, turn);
		this.player = (SoloPlayer) player.get(0);
		this.solo_action_tokens = all_solo_action_tokens;
		this.active_tokens = active_tokens;
		this.last_token = last_token;
	}

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public ArrayDeque<SoloActionToken> getActiveTokens() {
		return new ArrayDeque<SoloActionToken>(this.active_tokens);
	}

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public SoloActionToken getLastToken() {
		return this.last_token;
	}

	/**
	 * Send a ViewUpdate with the current Game
	 */
	@Override
	public void notifyGame() {
		notifyModel(new ViewUpdate(this.simplify()));
	}

	/**
	 * Simplify the SoloGame to send it to the Client
	 *
	 * @return the SimpleSoloGame used to represent this SoloGame
	 */
	private SimpleSoloGame simplify() {
		ArrayList<String> order = new ArrayList<String>();
		order.add(this.player.getNickname());
		return new SimpleSoloGame(order, super.market.peekMarket(), super.market.getFreeMarble(), super.development_cards_on_table.getTopCards(), this.last_token, this.active_tokens.size());
	}

	/**
	 * Shuffle the tokens and put them in a Queue
	 */
	public void shuffleSoloActionTokens() {
		// convert the full token array to a List, shuffle it then convert it back to an array
		List<SoloActionToken> solo_action_tokens_list = Arrays.asList(this.solo_action_tokens);
        Collections.shuffle(solo_action_tokens_list);
		solo_action_tokens_list.toArray(this.solo_action_tokens);

		// insert the new shuffled array in a Queue
		this.active_tokens = new ArrayDeque<SoloActionToken>(Arrays.asList(this.solo_action_tokens));
		notifyGame();
	}

	/**
	 * @return SoloActionToken on top of the Queue
	 */
	public SoloActionToken getTopToken() {
		this.last_token = this.active_tokens.pop();
		notifyGame();
		return this.last_token;
	}

	/**
	 * @return the size of the token queue
	 * testing only
	 */
	public int getTokenAmount(){
		return active_tokens.size();
	}

	/**
	 * Move the Black Cross marker forward
	 *
	 * @param number_of_times how many Cells to go forward
	 * @return the VaticanReports activated, null is none was activated
	 */
	public VaticanReports moveForwardBlackMarker(int number_of_times) {
		return player.moveForwardBlackMarker(number_of_times);
	}

	/**
	 * @return true if the Black Cross is at the end of the FaithTrack
	 */
	public boolean isBlackCrossAtTheEnd() {
		return this.player.isBlackCrossAtTheEnd();
	}

	/**
	 * Send a EndGameMessage to the Player saying that they lost the game
	 */
	public void lostGame() {
		// send a ranking containing 0 points for them
		HashMap<String, Integer> rank = new HashMap<String, Integer>();
		rank.put(this.player.getNickname(), 0);
		notifyModel(new EndGameMessage(rank));
	}
}
