package it.polimi.ingsw.controller;

import java.util.ArrayList;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.server.ClientHandler;

import java.util.ArrayList;

public class GameController implements Runnable, Controller {
	private ArrayList<ClientHandler> clients;
	private Game game;
	private Turn turn;

	public GameController(ArrayList<ClientHandler> clients) throws InstantiationException {
		this.clients = clients;
		try {
			this.game = new Initializer().initializeGame(clients);
			this.turn = game.getTurn();
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

	/**
	 * checks if the bonus can be extracted from the player's leader cards
	 *
	 * @param player is the player whose leader cards need to be checked
	 * @param bonus is the bonus requested by the player 
	 * @return true if the bonus is legal
	 */
	private boolean checkCorrectBonus(Player player, ArrayList<Resource> bonus){
		WhiteMarblesAbility test = new WhiteMarblesAbility(null);
		ArrayList<Resource> allowed_bonus = new ArrayList<Resource>();
		for (LeaderCard x : player.getDeck()){
			LeaderAbility ability = x.getAbility();
			if (ability.checkAbility(test)){
				allowed_bonus.add(((WhiteMarblesAbility) ability).getResourceType());
			}
		}
		for (Resource res : bonus){
			if (!allowed_bonus.contains(res)){
				return false;
			}
		}
		return true;
	}

	/**
	 * applies the bonus if it is appliable
	 *
	 * @param player is the player whose leader cards need to be checked
	 * @param bonus is the bonus requested by the player 
	 * @return true if the bonus is legal
	 */
	private boolean applyBonus (ArrayList<Resource> bonus, ArrayList<Resource> gains){
		if (bonus.size() > gains.stream().filter(x->x.equals(null)).count()){
			return false;
		} else {
			for (Resource x : bonus){
				if (x == null){
					gains.remove(x);
				}
			}
			gains.addAll(bonus);
			return true;
		}
	}

	public void handleMarket(Player player, int row, int column, boolean row_or_column, ArrayList<Resource> bonus) {
		if (!checkPlayer(player)){
			handleError();
		} else if (!checkCorrectBonus(player, bonus)){
			handleError();
		} else {
			try{
				if (row_or_column){
					ArrayList<Resource> gains = game.getColumn(column);
					if (applyBonus(bonus, gains)){
						turn.setProducedResources(gains);
						game.shiftColumn(column);
					} else {handleError();}
				} else {
					ArrayList<Resource> gains = game.getRow(row);
					if (applyBonus(bonus, gains)){
						turn.setProducedResources(gains);
						game.shiftRow(row);
					} else {handleError();}
				}
			} catch (IllegalArgumentException e){
				handleError();
			}
		}
	}

	public void handleProduction(Player player, ProductionInterface production) {
	}

	public void handleEndTurn(Player player) {
	}

	public void handlePay(Player player, ArrayList<Resource> warehouse_top, ArrayList<Resource> warehouse_mid, ArrayList<Resource> warehouse_bot, ArrayList<Resource> strongbox, ArrayList<Resource> extraspace) {
	}

	public void handleStore(Player player, ArrayList<Resource> warehouse_top, ArrayList<Resource> warehouse_mid, ArrayList<Resource> warehouse_bot, ArrayList<Resource> strongbox, ArrayList<Resource> extraspace, int shelf) {
	}

	public void handleDiscardResources(Player player) {
	}

	public void handleRearrange(Player player, int swap1, int swap2) {
	}

	public void handleActivateLeader(Player player, LeaderCard leader_card) {
	}

	public void handleDiscardLeader(Player player, LeaderCard leader_card) {
	}

	private void handleError(){
		this.game.handleError();
	}

	private boolean checkPlayer(Player player){
		if (!player.equals(game.getTurn().getPlayer())){
			return false;
		} else {return true;}
	}

	



}
