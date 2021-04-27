package it.polimi.ingsw.controller.util;

import java.util.Arrays;

import it.polimi.ingsw.controller.util.CommunicationController;
import it.polimi.ingsw.controller.util.ArrayChooser;
import it.polimi.ingsw.controller.util.Choice;

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
	public Object[] pickBetween(Player player, String message, Object[] selection, int to_choose){
		selection = Arrays.stream(selection).filter(x -> x != null).toArray();
		if (selection.length == 1){
			return selection;
		} else { 
			ArrayChooser array_chooser = new ArrayChooser(message, selection, to_choose);
			this.comm_controller.sendToPlayer(player, array_chooser);
			ArrayChooser response = (ArrayChooser) this.comm_controller.receiveFromPlayer(player);
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
	public boolean pickFlow(Player player, String message){
		Choice choice = new Choice(message);
		this.comm_controller.sendToPlayer(player, choice);
		Choice response = (Choice) this.comm_controller.receiveFromPlayer(player);
		return response.getResponse();
	}

	/**
	 * Send a message to a player
	 *
	 * @param player the Player to send the message to
	 * @param message the message to send
	 */
	public void sendMessage(Player player, String message) {
		this.comm_controller.sendToPlayer(player, message);
	}
}
