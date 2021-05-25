package it.polimi.ingsw.controller;

import java.util.ArrayList;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.server.ClientHandler;

import java.util.ArrayList;

public class GameController implements Runnable, Controller {
	private ArrayList<ClientHandler> clients;
	private Game game;

	public GameController(ArrayList<ClientHandler> clients) throws InstantiationException {
		this.clients = clients;
		try {
			this.game = new Initializer().initializeGame(clients);
		} catch (InstantiationException e) {
			System.out.println("Game could not start");
			throw new InstantiationException();
		}
	}

	public void handleMessage(Message message) {
		message.useMessage(this);
	}

	//TODO: this is empty, should GameController not be a Runnable?
	public void run() {
		return;
	}

	public void handleBuyCard(Player player, int row, int column, int slot) {
	}

	public void handleMarket(Player player, int row, int column, boolean row_or_column, ArrayList<Resource> white_marbles) {
	}

	public void handleProduction(Player player, ArrayList<ProductionInterface> productions) {
	}

	public void handleEndTurn(Player player) {
	}

	public void handlePay(Player player, Storage storage) {
	}

	public void handleStore(Player player, Storage storage) {
	}

	public void handleDiscardResources(Player player) {
	}

	public void handleRearrange(Player player, int swap1, int swap2) {
	}

	public void handleActivateLeader(Player player, LeaderCard leader_card) {
	}

	public void handleDiscardLeader(Player player, LeaderCard leader_card) {
	}
}
