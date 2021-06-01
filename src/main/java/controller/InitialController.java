package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.Storage;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

public class InitialController implements Controller {
	String message = null;

	public void handleMessage(Message message) {
		message.useMessage(this);
	}

	/**
	 * Set the message and notify the threads in wait
	 *
	 * @param message the message to set
	 */
	public synchronized void setReceivedMessage(String message) {
		this.message = message;
		notify();
	}

	/**
	 * Receive a message from the client, wait if the message is null
	 *
	 * @return the message received from the client
	 */
	public synchronized String receiveMessage() throws InterruptedException {
		while (this.message == null) {
			wait();
		}
		String to_return = this.message;
		this.message = null;
		return to_return;
	}

	/**
	 * Controller methods
	 */
	public void handleDiscardResources(Player player){
		return;
	}

	public void handleStore(Player player, Storage storage) {
		return;
	}

	public void handleChooseResources(Player player, Storage storage) {
		return;
	}

	public void handleBuyCard(Player player, int row, int column, int slot) {
		return;
	}

	public void handleMarket(Player player, int row, int column, boolean row_or_column, ArrayList<Resource> white_marbles) {
		return;
	}

	public void handleProduction(Player player, ArrayList<ProductionInterface> productions) {
		return;
	}

	public void handleActivateLeader(Player player, LeaderCard leader_card) {
		return;
	}

	public void handleRearrange(Player player, int swap1, int swap2) {
		return;
	}

	public void handleEndTurn(Player player) {
		return;
	}

	public void handleDiscardLeader(Player player, LeaderCard leader_card) {
		return;
	}

	public void handlePay(Player player, Storage storage) {
		return;
	}
}
