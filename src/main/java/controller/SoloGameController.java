package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.Iterator;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.controller.GameController;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.sologame.SoloGame;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.persistence.PersistenceParser;
import it.polimi.ingsw.server.persistence.PersistenceWriter;
import it.polimi.ingsw.server.ClientHandler;

public class SoloGameController extends GameController {
	private boolean last_card_discarded = false;

	public SoloGameController(ArrayList<ClientHandler> clients) {
		super(clients);
	}

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public SoloGameController(ArrayList<ClientHandler> clients, String match_name) throws InstantiationException {
		super(clients);
		super.match_name = match_name;
		try {
			super.game = PersistenceParser.parseSoloMatch(match_name);
			new Initializer().initializePersistenceSoloGame(clients, super.game);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			System.out.println("SoloGame could not start");
			throw new InstantiationException();
		}
	}


	/**
	 * Initialize the SoloGame using the Initializer
	 *
	 * @throws InstantiationException when the Initializer fails
	 */
	@Override
	public void initializeGame() throws InstantiationException {
		try {
			super.game = new Initializer().initializeSoloGame(super.clients);
		} catch (InstantiationException e) {
			System.out.println("SoloGame could not start");
			throw new InstantiationException();
		}
	}

	public boolean wasLastCardDiscarded(){
		return last_card_discarded;
	}

	/**
	 * Checks if the Player more than 7 development cards or has reached the end of the track and triggers the round to be the last
	 * First it checks the losing conditions:
	 * 	if all the cards of a certain color where discarded (using the last_card_discarded variable)
	 * 	if the Black Cross reached the end of the FaithTrack
	 * If either one of these conditions are true, the Player lost the game
	 *
	 * Then it checks the winning conditions:
	 *  if a Player has bought at least 7 DevelopmentCards
	 *  if the Player reached the end of the FaithTrack
	 * If either one of these conditions are true, the Player won
	 *
	 * If none of these apply, the game continues normally, otherwise the game will end
	 *
	 * @param player is the active Player
	 */
	@Override
	protected void checkLastTurn(Player player) {
		SoloGame sologame = (SoloGame) super.game;
		if (this.last_card_discarded || sologame.isBlackCrossAtTheEnd()) {
			// the player lost the game
			sologame.lostGame();
			return;
		}
		
		// count the DevelopmentCards of the player
		int count = 0;
		Iterator<DevelopmentCard> iterator = player.getDevCardIterator();
		while(iterator.hasNext()){
			iterator.next();
			count++;
		}
		if (player.endOfTrack() || count >= 7){
			// the player won the game
			sologame.endGame();
		}
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active,
	 * if they have played a main action, if they paid the cost of their action completely and
	 * completely stored all of their gain.
	 * Get the SoloActionToken on top of the stack and apply its function
	 * End the turn and starts the next player's turn if conditions are met, triggering the last round if needed, raises corresponding error otherwise.
	 *
	 * @param player is the Player who requested the action
	 *
	 */
	@Override
	public void handleEndTurn(Player player) {
		if (game.getTurn().hasDoneAction() && game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
			SoloActionToken last_token = ((SoloGame) super.game).getTopToken();
			last_token.useToken(this);
			checkLastTurn(player);
			game.endTurn();
		} else {handleError("You cannot end your turn now", player);}
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active and if they already paid every cost,
	 * Discards all the remaining resources from the turn state and moves forward the Black Cross if conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 */
	@Override
	public void handleDiscardResources(Player player) {
		if (super.game.getTurn().getRequiredResources().isEmpty()){
			int discarded = super.game.getTurn().getProducedResources().size();
			handleBlackMarkerVaticanReports(((SoloGame) super.game).moveForwardBlackMarker(discarded));
			super.game.getTurn().clearProducedResources();
		} else {handleError("You must pay all of the cost first", player);}
	}

	/**
	 * If a VaticanReports was activated by the Black Cross, activate it on the Player
	 *
	 * @param report the VaticanReport activated
	 */
	private void handleBlackMarkerVaticanReports(VaticanReports report) {
		if (report == null) {
			return;
		}
	
		Player player = super.game.getPlayers().get(0);
		if (player.whichVaticanReport() != null && player.whichVaticanReport().equals(report)) {
			player.activateVaticanReport();
		}
	}

	/**
	 * Called by the SoloActionToken using the Visitor pattern, move the Black Cross two spaces forward
	 */
	public void moveBlackCrossTwoSpaces() {
		handleBlackMarkerVaticanReports(((SoloGame) super.game).moveForwardBlackMarker(2));
	}

	/**
	 * Called by the SoloActionToken using the Visitor pattern, move the Black Cross one space forward
	 * and shuffle the Queue of SoloActionTokens
	 */
	public void moveBlackCrossOneSpace() {
		((SoloGame) super.game).shuffleSoloActionTokens();
		handleBlackMarkerVaticanReports(((SoloGame) super.game).moveForwardBlackMarker(1));
	}

	/**
	 * Discard a card of the DevelopmentCardsOnTable of a color starting from the lowest level
	 *
	 * @param order the integer rapresenting the color and the column of the DevelopmentCardsOnTable
	 */
	private void discardOneCard(int order) {
		for (int j = 2; j >= 0; j--) {
			DevelopmentCard[][] development_cards_on_table = super.game.getTopCards();
			if (development_cards_on_table[j][order] == null) {
				// if this card is null and we've reached the row 0, it means we've discarded all the cards of a color
				this.last_card_discarded = (j == 0) ? true : false;
			} else {
				// remove the card from the DevelopmentCardsOnTable
				super.game.getFromDeck(development_cards_on_table[j][order]);
				break;
			}
		}
	}

	/**
	 * Called by the SoloActionToken using the Visitor pattern, discard two DevelopmentCards of a specific DevelopmentCardsColor
	 */
	public void discardDevelopmentCards(DevelopmentCardsColor color) {
		int order = color.getOrder();

		for (int i = 0; i < 2; i++) {
			discardOneCard(order);
		}
	}

	/**
	 * PERSISTENCE
	 */

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	@Override
	public void handlePersistence(Player player) {
		PersistenceWriter.writeSoloPersistenceFile(this.match_name, (SoloGame) this.game);
	}
}
