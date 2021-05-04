package it.polimi.ingsw.controller.util;

import java.util.Arrays;

import it.polimi.ingsw.controller.turn.ProductionTurn;
import it.polimi.ingsw.controller.turn.BuyCardTurn;
import it.polimi.ingsw.controller.turn.MarketTurn;
import it.polimi.ingsw.controller.turn.Turn;

import it.polimi.ingsw.controller.util.CommunicationController;
import it.polimi.ingsw.controller.util.ArrayChooser;
import it.polimi.ingsw.controller.util.TurnSelector;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;
import it.polimi.ingsw.controller.util.Choice;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;

import it.polimi.ingsw.model.player.Player;

public class ChoiceController {
	private CommunicationController comm_controller;

	public ChoiceController(CommunicationController comm_controller) {
		this.comm_controller = comm_controller;
	}

	/**
	 * Choose one Object between an array of Objects
	 *
	 * @param player the Player that has to choose
	 * @param selection the array of Objects to choose from
	 * @return the chosen Object
	 */
	public Object[] pickBetween(Player player, String message_string, Object[] selection, int to_choose){
		selection = Arrays.stream(selection).filter(x -> x != null).toArray();
		if (selection.length == 1){
			return selection;
		} else { 
			ArrayChooser array_chooser = new ArrayChooser(message_string, selection, to_choose);
			Message message = new Message(MessageType.ARRAYCHOOSER, array_chooser);
			this.comm_controller.sendToPlayer(player, message);
			Message message_response = (Message) this.comm_controller.receiveFromPlayer(player);
			ArrayChooser response = (ArrayChooser) message_response.getMessage();
			return response.getChosenArray();
		}
	}

	/**
	 * Make a Player choose an option
	 *
	 * @param player the Player that has to choose
	 * @param message a String describing what to choose from
	 * @return a boolean rappresenting the chosen option
	 */
	public boolean pickFlow(Player player, String message_string){
		Choice choice = new Choice(message_string);
		Message message = new Message(MessageType.CHOICE, choice);
		this.comm_controller.sendToPlayer(player, message);
		Message message_response = (Message) this.comm_controller.receiveFromPlayer(player);
		Choice response = (Choice) message_response.getMessage();
		return response.getResponse();
	}

	/**
	 * Send a message to a player
	 *
	 * @param player the Player to send the message to
	 * @param message the message to send
	 */
	public void sendMessage(Player player, String message_string) {
		Message message = new Message(MessageType.STRING, message_string);
		this.comm_controller.sendToPlayer(player, message);
	}

	/**
	 * Pick a Turn between the three possible Turns
	 *
	 * @param player the Player that has to choose
	 * @param development_cards_on_table the cards for BuyCardTurn
	 * @param market the market for MarketTurn
	 * @return the Turn chosen by the Player
	 */
	public Turn pickTurn(Player player, DevelopmentCardsOnTable development_cards_on_table, Market market) {
		TurnSelector turn_selector = new TurnSelector();
		Message message = new Message(MessageType.TURNSELECTOR, turn_selector);
		this.comm_controller.sendToPlayer(player, message);
		Message message_response = (Message) this.comm_controller.receiveFromPlayer(player);
		TurnSelector response = (TurnSelector) message_response.getMessage();

		Turn to_return;
		switch(response.getChosen()) {
			case 1:
				to_return = new BuyCardTurn(player, this, development_cards_on_table);
				break;
			case 2:
				to_return = new MarketTurn(player, this, market);
				break;
			case 3:
				to_return =  new ProductionTurn(player, this);
				break;
			default:
				//TODO: better error handling
				to_return = null;
		}

		return to_return;
	}
}
