package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Warehouse;
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
	 * Checks if the bonus can be extracted from the player's leader cards
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
	 * Applies the bonus if it is appliable
	 *
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
	 * Applies discount to given cost
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

	/**
	 * Calculates the total cost of the selected production powers
	 *
	 * @param productions is the ArrayList of productions to check
	 * @return an ArrayList of resources containing the total cost of the productions
	 * */
	private ArrayList<Resource> totalProductionCost(ArrayList<ProductionInterface> productions){
		ArrayList<Resource> total = new ArrayList<Resource>();
		for (ProductionInterface x : productions){
			total.addAll(x.getRequiredResources());
		}
		return total;
	}

	/**
	 * Calculates the total gain of the selected production powers
	 *
	 * @param productions is the ArrayList of productions to check
	 * @return an ArrayList of resources containing the total gain of the productions
	 * */
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
				ArrayList<Resource> required = totalProductionCost(productions);
				ArrayList<Resource> produced = totalProductionGain(productions);
				if(player.hasEnoughResources(required)){
					game.getTurn().addRequiredResources(required);
					player.moveForward((int)produced.stream().filter(x->x.equals(Resource.FAITH)).count());
					game.getTurn().addProducedResources((ArrayList<Resource>)produced.stream().filter(x->!x.equals(Resource.FAITH)).collect(Collectors.toList()));
					game.getTurn().setDoneAction(true);
				} else {handleError();}
			} else {handleError();}
		} else {handleError();}
	}

	/**
	 * RESOURCE RELATED ACTIONS
	 */

	private boolean checkCorrectPayAmount(Player player, Storage storage){
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		ArrayList<Resource> total = storage.getStrongbox();
		total.addAll(storage.getWarehouseTop());
		total.addAll(storage.getWarehouseMid());
		total.addAll(storage.getWarehouseBot());
		total.addAll(storage.getExtraspace());
		for (Resource res : check){
			if ((int) total.stream().filter(x->x.equals(res)).count() > (int) game.getTurn().getRequiredResources().stream().filter(x->x.equals(res)).count()){
				return false;
			}
		}
		return true;
	}

	private boolean isContainedExtra(Player player, Storage storage){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		LeaderAbility ability;
		boolean exists = false;
		ArrayList<Resource> resource = storage.getExtraspace(); 
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource res : check){
			exists = false;
			for (LeaderCard x : player.getDeck()){
				ability = x.getAbility();
				if (ability.checkAbility(test)){
					exists = exists? exists : ((ExtraSpaceAbility) ability).isContainedExtra((ArrayList<Resource>) resource.stream().filter(e->e.equals(res)).collect(Collectors.toList()));
				}
			}
			if (!exists){
				return false;
			}
		}
		return true;
	}

	private boolean isContainedWarehouse(Player player, Storage storage){
		Warehouse warehouse = player.getWarehouse();
		return warehouse.isContainedTop(storage.getWarehouseTop()) && warehouse.isContainedMiddle(storage.getWarehouseMid()) && warehouse.isContainedBottom(storage.getWarehouseBot());    
	}
	
	private boolean isContainedStrongbox(Player player, Storage storage){
		return player.getPlayerStrongBox().areContainedInStrongbox(storage.getStrongbox());
	}

	private boolean isContained(Player player, Storage storage){
		return isContainedStrongbox(player,storage) && isContainedWarehouse(player, storage) && isContainedExtra(player, storage);
	}
	
	public void handlePay(Player player, Storage storage) {
		if (checkPlayer(player)){
			if (checkCorrectPayAmount(player, storage)){
				if (isContained(player, storage)){
					player.removeResources(storage.getStrongbox());
					game.getTurn().removeRequiredResources(storage.getStrongbox());
					player.getFromTop(storage.getWarehouseTop());
					game.getTurn().removeRequiredResources(storage.getWarehouseTop());
					player.getFromMiddle(storage.getWarehouseMid());
					game.getTurn().removeRequiredResources(storage.getWarehouseMid());
					player.getFromBottom(storage.getWarehouseBot());
					game.getTurn().removeRequiredResources(storage.getWarehouseBot());
					player.getFromExtra(storage.getExtraspace());
					game.getTurn().removeRequiredResources(storage.getExtraspace());
					//If the payment comes as the result of production all resources must go in the Strongbox
					if (!game.getTurn().mustDiscard() && game.getTurn().getRequiredResources().isEmpty()){
						player.insertResources(game.getTurn().getProducedResources());
						game.getTurn().getProducedResources().clear();
					}
				} else {handleError();}
			} else {handleError();}
		} else {handleError();}
	}

	private boolean checkCorrectStoreAmount(Player player, Storage storage){
		if (!storage.getStrongbox().isEmpty()){
			return false;
		}
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		ArrayList<Resource> total = storage.getWarehouseTop();
		total.addAll(storage.getWarehouseMid());
		total.addAll(storage.getWarehouseBot());
		total.addAll(storage.getExtraspace());
		for (Resource res : check){
			if ((int) total.stream().filter(x->x.equals(res)).count() > (int) game.getTurn().getRequiredResources().stream().filter(x->x.equals(res)).count()){
				return false;
			}
		}
		return true;
	}

	private boolean canBeStoredExtra(Player player, Storage storage){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		LeaderAbility ability;
		boolean exists = false;
		ArrayList<Resource> resource = storage.getExtraspace(); 
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource res : check){
			exists = false;
			for (LeaderCard x : player.getDeck()){
				ability = x.getAbility();
				if (ability.checkAbility(test)){
					exists = exists? exists : ((ExtraSpaceAbility) ability).canBeStoredExtra((ArrayList<Resource>) resource.stream().filter(e->e.equals(res)).collect(Collectors.toList()));
				}
			}
			if (!exists){
				return false;
			}
		}
		return true;
	}

	private boolean canBeStoredWarehouse(Player player, Storage storage){
		Warehouse warehouse = player.getWarehouse();
		return warehouse.canBeStoredTop(storage.getWarehouseTop()) && warehouse.canBeStoredMiddle(storage.getWarehouseMid()) && warehouse.canBeStoredBottom(storage.getWarehouseBot());    
	}

	private boolean canBeStored(Player player, Storage storage){
		return canBeStoredExtra(player, storage) && canBeStoredWarehouse(player, storage);
	}

	public void handleStore(Player player, Storage storage) {
		if (checkPlayer(player)){
			if (game.getTurn().getRequiredResources().isEmpty()){
				if (checkCorrectStoreAmount(player, storage)){
					if (canBeStored(player, storage)){
						player.storeTop(storage.getWarehouseTop());
						game.getTurn().removeProducedResources(storage.getWarehouseTop());
						player.storeMiddle(storage.getWarehouseMid());
						game.getTurn().removeProducedResources(storage.getWarehouseMid());
						player.storeBottom(storage.getWarehouseBot());
						game.getTurn().removeProducedResources(storage.getWarehouseBot());
						player.storeExtra(storage.getExtraspace());
						game.getTurn().removeProducedResources(storage.getExtraspace());
					} else {handleError();}
				} else {handleError();}
			} else {handleError();}
		} else {handleError();}
	}

	public void handleDiscardResources(Player player) {
		if (checkPlayer(player)){
			if (game.getTurn().getRequiredResources().isEmpty()){
				int discarded = game.getTurn().getProducedResources().size();
				for (Player p : game.getPlayers()){
					if (!p.equals(player)){
						p.moveForward(discarded);
					}
				}
				game.getTurn().getProducedResources().clear();
			} else {handleError();}
		} else {handleError();}
	}

	public void handleRearrange(Player player, int swap1, int swap2) {
		if (checkPlayer(player)){
			try {
				player.swapRows(swap1, swap2);
			} catch (IllegalArgumentException e){handleError();}
		} else {handleError();}
	}

	/**
	 * END TURN
	 * */
	private void checkLastTurn(Player player){
		int count = 0;
		Iterator<DevelopmentCard> iterator = player.getDevCardIterator();
		while(iterator.hasNext()){
			iterator.next();
			count++;
		}
		if (player.endOfTrack() || count >= 7){
			game.getTurn().setFinal(true);
		}
	}

	public void handleEndTurn(Player player) {
		if (checkPlayer(player)){
			if (game.getTurn().hasDoneAction() && game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
				checkLastTurn(player);
				game.endTurn();
			}
		} else {handleError();}
	}

	/**
	 * HANDLE VARIOUS ERRORS
	 */
	private void handleError(){
		this.game.handleError("");
	}

}
