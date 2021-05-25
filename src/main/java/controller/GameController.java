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
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Turn;

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
	
	/**
	 * checks whether the player who sent the message is the active player or not
	 */
	private boolean checkPlayer(Player player){
		if (!player.equals(game.getTurn().getPlayer())){
			return false;
		} else {return true;}
	}

	/**
	 * LEADER CARD RELATED ACTIONS
	 */
	public void handleActivateLeader(Player player, LeaderCard card) {
		if (checkPlayer(player)){
			if (game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
				if(player.isActivable(card) && !card.isActive()){
					player.activateLeader(card);	
				} else {handleError();}
			} else {handleError();}
		} else {handleError();}
	}

	public void handleDiscardLeader(Player player, LeaderCard card) {
		if (checkPlayer(player)){
			if (game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
				player.discardLeader(card);
				player.moveForward(1);
			} else {handleError();}
		} else {handleError();}
	}

	/**
	 * MAIN ACTIONS
	 */

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
	 * @param gains is the ArrayList of resources gained by the player
	 * @return an ArrayList with proper bonus applied
	 * @throws IllegalArgumentException if the bonus is too big
	 * */
	private ArrayList<Resource> applyBonus (ArrayList<Resource> bonus, ArrayList<Resource> gains){
		if (bonus.size() > gains.stream().filter(x->x.equals(null)).count()){
			throw new IllegalArgumentException();
		} else {
			for (Resource x : bonus){
				if (x == null){
					gains.remove(x);
				}
			}
			gains.addAll(bonus);
			return gains;
		}
	}

	public void handleMarket(Player player, int row, int column, boolean row_or_column, ArrayList<Resource> bonus) {
		if (checkPlayer(player)){
			if (!game.getTurn().hasDoneAction()){
				if (checkCorrectBonus(player, bonus)){
					try{
						ArrayList<Resource> gains = row_or_column? game.getColumn(column) : game.getRow(row); 
						try{
							gains = applyBonus(bonus, gains);
							game.getTurn().addProducedResources(gains);
							if (row_or_column){
								game.shiftColumn(column);
							} else {
								game.shiftRow(row);
							}
							game.getTurn().setDoneAction(true);
							game.getTurn().setDiscard(true);
						} catch (IllegalArgumentException e) {handleError();}
					} catch (IllegalArgumentException e) {handleError();}
				} else {handleError();}
			} else {handleError();}
		} else {handleError();}
	}

	/**
	 * applies discount to given cost
	 *
	 * @param player is the player whose leader cards need to be checked
	 * @param cost is the ArrayList of resources to be paid
	 * @return an ArrayList with proper discount applied
	 */ 
	private ArrayList<Resource> applyDiscount(Player player, ArrayList<Resource> cost){
		DiscountAbility test = new DiscountAbility(null);
		ArrayList<Resource> discount = new ArrayList<Resource>();
		for (LeaderCard x : player.getDeck()){
			LeaderAbility ability = x.getAbility();
			if (ability.checkAbility(test)){
				discount.add(((DiscountAbility) ability).getDiscountedResource());
			}
		}
		for (Resource res : discount){
			cost.remove(res);
		}
		return cost;
	}

	public void handleBuyCard(Player player, int row, int column, int slot) {
		if (checkPlayer(player)){
			if (!game.getTurn().hasDoneAction()){
				try{
					DevelopmentCard card = game.getTopCards()[row][column]; 
					ArrayList<Resource> cost = new ArrayList<Resource>();
					cost.addAll(card.getCost());
					if (player.hasEnoughResources(cost)){
						if (player.fitsInSlot(card, slot)){
							try { 
								game.getFromDeck(card);
								player.buyCard(card, slot);
								game.getTurn().addRequiredResources(cost);
								game.getTurn().setDoneAction(true);
							} catch (IndexOutOfBoundsException e) {handleError();}
						} else {handleError();}
					} else {handleError();}
				} catch (IndexOutOfBoundsException e) {handleError();}
			} else {handleError();}
		} else {handleError();}
	}

	private ArrayList<Resource> totalProductionCost(ArrayList<ProductionInterface> productions){
		ArrayList<Resource> total = new ArrayList<Resource>();
		for (ProductionInterface x : productions){
			total.addAll(x.getRequiredResources());
		}
		return total;
	}		

	private ArrayList<Resource> totalProductionGain(ArrayList<ProductionInterface> productions){
		ArrayList<Resource> total = new ArrayList<Resource>();
		for (ProductionInterface x : productions){
			total.addAll(x.getProducedResources());
		}
		return total;
	}		

	public void handleProduction(Player player, ArrayList<ProductionInterface> productions) {
		if (checkPlayer(player)){
			if (!game.getTurn().hasDoneAction()){
				ArrayList<Resource> required = new totalProductionCost(productions);
				ArrayList<Resource> produced = new totalProductionGain(productions);
				if(player.hasEnoughResources(required)){
					game.getTurn().addRequiredResources(required);
					game.getTurn().addProducedResources(produced);
					game.getTurn().setDoneAction(true);
				} else {handleError();}
			} else {handleError();}
		} else {handleError();}
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

	private void handleError(){
		this.game.handleError();
	}

}
