package it.polimi.ingsw.controller;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.SetupManager;
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
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.server.persistence.PersistenceParser;
import it.polimi.ingsw.server.persistence.PersistenceWriter;
import it.polimi.ingsw.server.persistence.PersistenceUtil;
import it.polimi.ingsw.server.ClientHandler;

public class GameController implements Controller {
	protected ArrayList<ClientHandler> clients;
	protected Game game;
	protected String match_name;

	public GameController(ArrayList<ClientHandler> clients) {
		this.clients = clients;
	}

	/**
	 * Persistence only - recreate a GameController from the match saved in memory
	 */
	public GameController(ArrayList<ClientHandler> clients, String match_name) throws InstantiationException {
		this.clients = clients;
		this.match_name = match_name;
		try {
			this.game = PersistenceParser.parseMatch(match_name);
			new SetupManager().setupPersistenceGame(this.clients, this.game);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
			System.out.println("Game could not start");
			throw new InstantiationException();
		}
	}

	/**
	 * Setup the Game using the SetupManager
	 *
	 * @throws InstantiationException when the SetupManager fails
	 */
	public void setupGame() throws InstantiationException {
		try {
			this.game = new SetupManager().setupGame(this.clients);
		} catch (InstantiationException e) {
			System.out.println("Game could not start");
			throw new InstantiationException();
		}
	}

	public void setMatchName(String match_name) {
		this.match_name = match_name;
	}

	/**
	 * Handle the Message coming from the Client using the pattern Visitor
	 *
	 * @param message the Message to handle
	 */
	public void handleMessage(Message message) {
		message.useMessage(this);
	}

	/**
	 * Handle Errors by notifying ErrorMessages
	 *
	 * @param error_string the error to report
	 * @param player the Player that committed the error
	 */
	protected void handleError(String error_string, Player player){
		this.game.handleError(error_string, player);
	}
	
	/**
	 * @param player is the player who sent the message
	 * @return true only if the player who sent the message is the active player
	 */
	protected boolean checkPlayer(Player player){
		return player.equals(game.getTurn().getPlayer()) && game.getTurn().hasDoneSetup();
	}

	/**
	 * If a VaticanReports was activated by a Player, activate it on other Players
	 *
	 * @param player the Player that activated the VaticanReport
	 * @param report the VaticanReport activated
	 */
	private void handleVaticanReports(Player player, VaticanReports report) {
		if (report == null) {
			return;
		}

		for (Player p: this.game.getPlayers()) {
			if (!p.equals(player)) {
				if (p.whichVaticanReport() != null && p.whichVaticanReport().equals(report)) {
					p.activateVaticanReport();
				}
			}
		}
	}
	
	/**
	 * SETUP RELATED ACTIONS
	 */ 

	/**
	 * @return true only if each player has less than 2 leader cards
	 */
	private boolean checkCardNumber(){
		for (Player p : game.getPlayers()){
			if (p.getLeaderCards().size() > 2) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param player is the player who requested to choose their starting resources
	 * @return true only if the given player has the proper amount of starting resources, according to the rules
	 */
	private boolean checkCorrectResources(Player player){
		int totalWarehouse = player.getTopResource().size() + player.getMiddleResources().size() + player.getBottomResources().size();
		if (game.getPlayers().indexOf(player) == 1 || game.getPlayers().indexOf(player) == 2){
			if (totalWarehouse < 1){
				return false;
			}
		} else if (game.getPlayers().indexOf(player) == 3){
			if (totalWarehouse < 2){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return true only if every player has the proper amount of starting resources
	 */
	private boolean checkCorrectTotalResources(){
		int totalWarehouse = 0;
		for (Player p : game.getPlayers()){
			if (!checkCorrectResources(p)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the turn to setup_done if each player has the correct amount of starting resources and leader cards
	 */
	private void checkEndSetup(){
		if(checkCardNumber() && checkCorrectTotalResources()){
			game.getTurn().setupDone(true);
		}
	}

	/**
	 * @param player is the player who requested to choose their starting resources
	 * @param storage contains all the resources involved in the action
	 * @return true only if the chosen resources aren't more than the proper starting amount, according to the rules
	 */
	private boolean checkLegalRequestedResources(Player player, Storage storage){
		int totalWarehouse = player.getTopResource().size() + player.getMiddleResources().size() + player.getBottomResources().size();
		int totalStorage = storage.getWarehouseTop().size() + storage.getWarehouseMid().size() + storage.getWarehouseBot().size();
		if (game.getPlayers().indexOf(player) == 0) {
			return totalStorage == 0;
		} else if (game.getPlayers().indexOf(player) == 1 || game.getPlayers().indexOf(player) == 2){
			return totalStorage <= 1 - totalWarehouse;
		} else if (game.getPlayers().indexOf(player) == 3){
			return totalStorage <= 2 - totalWarehouse;
		} else {
			return false;
		}
	}

	/**
	 * Upon receiving the corresponding message, checks if the game is yet to start, the player who requested the resources already has enough and the resources being asked are within the rules. 
	 * Stores them in the warehouse and checks if the game can start if the conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 */
	public void handleChooseResources(Player player, Storage storage){
		if (!game.getTurn().hasDoneSetup()){
			if (!checkCorrectResources(player)){
				if (checkLegalRequestedResources(player, storage)){
					if (canBeStoredWarehouse(player, storage)) {
						player.storeTop(storage.getWarehouseTop());
						player.storeMiddle(storage.getWarehouseMid());
						player.storeBottom(storage.getWarehouseBot());
						checkEndSetup();
					} else {handleError("You cannot store resources in such a way", player);}
				} else {handleError("You cannot choose so many starting resources", player);}
			} else {handleError("You already have the correct amount of starting resources", player);}
		} else {handleError("The game has already started", player);}
	}

	/**
	 * LEADER CARD RELATED ACTIONS
	 */

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they are at the start or the end of the turn and the requested card is inactive, but
	 * can be activated.
	 * Activates the card if the conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param card is the card which the player is requesting to activate
	 */
	public void handleActivateLeader(Player player, LeaderCard card) {
		if (checkPlayer(player)){
			if (game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
				if(player.isActivable(card) && !card.isActive()){
					player.activateLeader(card);	
				} else {handleError("The requested card cannot be activated", player);}
			} else {handleError("You must end your action first", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * Upon receiving the corresponding message, checks if the game is yet to start.
	 * If it is, checks if the player has more than 2 leader cards. 
	 * Discards the card and checks if the game can start if the conditions are met, raises corresponding error otherwise.
	 * If it is not, checks if the player who requested the action is active and they are at the start or the end of the turn.
	 * Discards the card and moves the player forward if the conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param card is the card which the player is requesting to discard
	 */
	public void handleDiscardLeader(Player player, LeaderCard card) {
		if (!game.getTurn().hasDoneSetup()){
			if	(player.getLeaderCards().size() > 2) {
				player.discardLeader(card.getId());	
				checkEndSetup();
			} else {handleError("You cannot discard any more cards", player);}
		} else if (player.equals(game.getTurn().getPlayer())){ 
			if (game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
				player.discardLeader(card.getId());	
				handleVaticanReports(player, player.moveForward(1));
			} else {handleError("You must end your action first", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * MAIN ACTIONS
	 */

	/**
	 * @param player is the player whose leader cards need to be checked
	 * @param bonus is the bonus requested by the player 
	 * @return true if the bonus can be extracted from the player's leader cards
	 */
	private boolean checkCorrectBonus(Player player, ArrayList<Resource> bonus){
		WhiteMarblesAbility test = new WhiteMarblesAbility(null);
		ArrayList<Resource> allowed_bonus = new ArrayList<Resource>();
		for (LeaderCard x : player.getLeaderCards()){
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
	 * Applies the requested bonus if it is appliable
	 *
	 * @param bonus is the bonus requested by the player 
	 * @param gains is the ArrayList of resources gained by the player
	 * @return an ArrayList with proper bonus applied
	 * @throws IllegalArgumentException if the bonus is too big
	 * */
	private ArrayList<Resource> applyBonus (ArrayList<Resource> bonus, ArrayList<Resource> gains){
		if (bonus.size() > gains.stream().filter(x-> x == null).count()) {
			throw new IllegalArgumentException();
		} else {
			gains.addAll(bonus);
			return gains;
		}
	}
	
	/**
	 * Removes null resources and FAITH resources from the given ArrayList, moving the player forward if necessary
	 *
	 * @param player is the player who gained the given resources
	 * @param raw_resources is the ArrayList of unfiltered gained resources
	 */
	private ArrayList<Resource> filterResources(Player player, ArrayList<Resource> raw_resources){
		ArrayList<Resource> filtered = (ArrayList<Resource>) raw_resources.stream().filter(e -> e != null).collect(Collectors.toList());
		int steps = (int) filtered.stream().filter(e -> e.equals(Resource.FAITH)).count();
		handleVaticanReports(player, player.moveForward(steps));
		return (ArrayList<Resource>) filtered.stream().filter(e -> !e.equals(Resource.FAITH)).collect(Collectors.toList());
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they are yet to play their main action, if the bonus they requested is correctly
	 * granted by their leader cards and appliable and the column or row they want to get is within bounds.
	 * Updates the market, moves the player and adds the gained resources to the turn state is conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param row is the row which the player requested
	 * @param columns is the column which the player requested
	 * @param row_or_column indicates whether the player has requested a row or a column
	 * @param bonus contains all the resources requested through the leader card bonus
	 */
	public void handleMarket(Player player, int row, int column, boolean row_or_column, ArrayList<Resource> bonus) {
		if (checkPlayer(player)){
			if (!game.getTurn().hasDoneAction()){
				if (checkCorrectBonus(player, bonus)){
					try{
						ArrayList<Resource> gains = row_or_column? game.getColumn(column - 1) : game.getRow(row - 1); 
						try{
							gains = applyBonus(bonus, gains);
							gains = filterResources(player, gains);
							game.getTurn().addProducedResources(gains);
							if (row_or_column){
								game.shiftColumn(column - 1);
							} else {
								game.shiftRow(row - 1);
							}
							game.getTurn().setDoneAction(true);
							game.getTurn().setDiscard(true);
						} catch (IllegalArgumentException e) {handleError("The bonus you requested cannot be applied to the resources you have gained", player);}
					} catch (IllegalArgumentException e) {handleError("The row or column you requested doesn't exist", player);}
				} else {handleError("You cannot ask for such a bonus", player);}
			} else {handleError("You already played your main action", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * Applies discount to given resource cost by removing resources from the ArrayList
	 *
	 * @param player is the player whose leader cards need to be checked
	 * @param cost is the ArrayList of resources to be paid
	 * @return an ArrayList with proper discount applied
	 */ 
	private ArrayList<Resource> applyDiscount(Player player, ArrayList<Resource> cost){
		DiscountAbility test = new DiscountAbility(null);
		ArrayList<Resource> discount = new ArrayList<Resource>();
		for (LeaderCard x: player.getLeaderCards()){
			LeaderAbility ability = x.getAbility();
			if (x.isActive() && ability.checkAbility(test)){
				discount.add(((DiscountAbility) ability).getDiscountedResource());
			}
		}
		for (Resource res : discount){
			cost.remove(res);
		}
		return cost;
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they are yet to play their main action, if card they
	 * requested to buy exists and they have enough resources and space to buy it.
	 * Moves the card from the table to the selected slot and adds the cost to the turn state if conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param row is the row of the card which the player requested
	 * @param columns is the column of the card which the player requested
	 * @param slot is the slot requested for the bought card
	 */
	public void handleBuyCard(Player player, int row, int column, int slot) {
		if (checkPlayer(player)){
			if (!game.getTurn().hasDoneAction()){
				try{
					DevelopmentCard card = game.getTopCards()[row][column]; 
					ArrayList<Resource> cost = new ArrayList<Resource>();
					cost.addAll(card.getCost());
					cost = applyDiscount(player, cost);
					if (player.hasEnoughResources(cost)){
						if (player.fitsInSlot(card, slot - 1)){
							game.getFromDeck(card);
							player.buyCard(card, slot - 1);
							game.getTurn().addRequiredResources(cost);
							game.getTurn().setDoneAction(true);
						} else {handleError("You cannot put the card in the requested slot", player);}
					} else {handleError("You do not have enough resources to buy this card", player);}
				} catch (IndexOutOfBoundsException e) {handleError("The card you requested does not exist", player);}
			} else {handleError("You already played your main action", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * Calculates the total cost of the selected production powers
	 *
	 * @param productions is the ArrayList of productions to check
	 * @return an ArrayList of resources containing the total cost of the production
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
	 * @return an ArrayList of resources containing the total gain from the production
	 * */
	private ArrayList<Resource> totalProductionGain(ArrayList<ProductionInterface> productions){
		ArrayList<Resource> total = new ArrayList<Resource>();
		for (ProductionInterface x : productions){
			total.addAll(x.getProducedResources());
		}
		return total;
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they are yet to play their main action and if they have enough resources to activate the production.
	 * Adds the cost and gains of the production to the turn state if conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param productions contains all the production powers the player requested to activate
	 */
	public void handleProduction(Player player, ArrayList<ProductionInterface> productions) {
		if (checkPlayer(player)){
			if (!game.getTurn().hasDoneAction()){
				ArrayList<Resource> required = totalProductionCost(productions);
				ArrayList<Resource> produced = totalProductionGain(productions);
				if(player.hasEnoughResources(required)){
					game.getTurn().addRequiredResources(required);
					int number_of_faith = (int)produced.stream().filter(x->x.equals(Resource.FAITH)).count();
					handleVaticanReports(player, player.moveForward(number_of_faith));
					game.getTurn().addProducedResources((ArrayList<Resource>)produced.stream().filter(x->!x.equals(Resource.FAITH)).collect(Collectors.toList()));
					game.getTurn().setDoneAction(true);
				} else {handleError("You don't have enough resources to activate these production powers", player);}
			} else {handleError("You already played your main action", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * RESOURCE RELATED ACTIONS
	 */

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be paid are less than the cost saved in the turn state 
	 */ 
	private boolean checkCorrectPayAmount(Player player, Storage storage){
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		ArrayList<Resource> total = new ArrayList<Resource>();
		total.addAll(storage.getStrongbox());
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

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be paid from the extra space is present in the extra space 
	 */ 
	private boolean isContainedExtra(Player player, Storage storage){
		if (storage.getExtraspace().isEmpty()){
			return true;
		} else {
			ExtraSpaceAbility test = new ExtraSpaceAbility(null);
			LeaderAbility ability;
			boolean exists = false;
			ArrayList<Resource> resource = storage.getExtraspace(); 
			ArrayList<Resource> card_storage = new ArrayList<Resource>(); 
			Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
			for (Resource res : check){
				exists = false;
				for (LeaderCard l : player.getLeaderCards()){
					if (l.isActive()){ 
						ability = l.getAbility();
						if (ability.checkAbility(test)){
							card_storage = (ArrayList<Resource>) resource.stream().filter(e->e.equals(res)).collect(Collectors.toList());
							exists = exists? exists : ((ExtraSpaceAbility) ability).isContainedExtra(card_storage);
						}
					}
				}
				if (!exists){
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be paid from the warehouse is present in the warehouse 
	 */ 
	private boolean isContainedWarehouse(Player player, Storage storage){
		Warehouse warehouse = player.getWarehouse();
		return warehouse.isContainedTop(storage.getWarehouseTop()) && warehouse.isContainedMiddle(storage.getWarehouseMid()) && warehouse.isContainedBottom(storage.getWarehouseBot());    
	}
	
	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be paid from the strongbox is present in the strongbox 
	 */ 
	private boolean isContainedStrongbox(Player player, Storage storage){
		return player.getStrongBox().areContainedInStrongbox(storage.getStrongbox());
	}

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be paid from each place is present in the corresponding place 
	 */ 
	private boolean isContained(Player player, Storage storage){
		return isContainedStrongbox(player,storage) && isContainedWarehouse(player, storage) && isContainedExtra(player, storage);
	}
	
	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they are requesting to pay a legal amount and if each of the places where resources are
	 * stored contains the resources they are requesting to pay with.
	 * Removes the amount of each resource from both the player and the turn state if conditions are met, storing all produced resources in the strongbox if the player must not discard any, 
	 * raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 */
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
					if (!game.getTurn().mustDiscard() && game.getTurn().getRequiredResources().isEmpty()){
						player.insertResources(game.getTurn().getProducedResources());
						game.getTurn().clearProducedResources();
					}
				} else {handleError("The resources you offered for payment aren't available", player);}
			} else {handleError("You cannot pay more than you should", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be stored are less than the gain saved in the turn state 
	 */ 
	private boolean checkCorrectStoreAmount(Player player, Storage storage){
		if (!storage.getStrongbox().isEmpty()){
			return false;
		}
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		ArrayList<Resource> total = new ArrayList<Resource>();
		total.addAll(storage.getWarehouseTop());
		total.addAll(storage.getWarehouseMid());
		total.addAll(storage.getWarehouseBot());
		total.addAll(storage.getExtraspace());
		for (Resource res : check){
			if ((int) total.stream().filter(x->x.equals(res)).count() > (int) game.getTurn().getProducedResources().stream().filter(x->x.equals(res)).count()){
				return false;
			}
		}
		return true;
	}

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be stored in the extra space can be stored in the extra space 
	 */ 
	private boolean canBeStoredExtra(Player player, Storage storage){
		if (storage.getExtraspace().isEmpty()){
			return true;
		} else {
			ExtraSpaceAbility test = new ExtraSpaceAbility(null);
			LeaderAbility ability;
			boolean exists = false;
			ArrayList<Resource> resource = storage.getExtraspace(); 
			ArrayList<Resource> card_storage = new ArrayList<Resource>(); 
			Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
			for (Resource res : check){
				exists = false;
				for (LeaderCard l : player.getLeaderCards()){
					if (l.isActive()){ 
						ability = l.getAbility();
						if (ability.checkAbility(test)){
							card_storage = (ArrayList<Resource>) resource.stream().filter(e->e.equals(res)).collect(Collectors.toList());
							exists = exists? exists : ((ExtraSpaceAbility) ability).canBeStoredExtra(card_storage);
						}
					}
				}
				if (!exists){
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * checks that each warehouse storage contains different resources
	 * @param storage contains all the resources involved in the action
	 */
	private boolean isAllDifferent(Storage storage){
		ArrayList<Resource> top = storage.getWarehouseTop();
		ArrayList<Resource> mid = storage.getWarehouseMid();
		ArrayList<Resource> bot = storage.getWarehouseBot();
		if (top.size() > 0 && mid.size() > 0){
			if (top.get(0).equals(mid.get(0))){
				return false;
			}
		}
		if (top.size() > 0 && bot.size() > 0){
			if (top.get(0).equals(bot.get(0))){
				return false;
			}
		}
		if (mid.size() > 0 && bot.size() > 0){
			if (mid.get(0).equals(bot.get(0))){
				return false;
			}
		}
		return true;
	}

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be stored in the warehouse can be stored in the warehouse 
	 */ 
	private boolean canBeStoredWarehouse(Player player, Storage storage){
		Warehouse warehouse = player.getWarehouse();
		return isAllDifferent(storage) && warehouse.canBeStoredTop(storage.getWarehouseTop()) && warehouse.canBeStoredMiddle(storage.getWarehouseMid()) && warehouse.canBeStoredBottom(storage.getWarehouseBot());    
	}

	/**
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 * @return true only if the amount of resources requested to be stored in each place can be stored in the corresponding place 
	 */ 
	private boolean canBeStored(Player player, Storage storage){
		return canBeStoredExtra(player, storage) && canBeStoredWarehouse(player, storage);
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they already paid every cost, if they are requesting to store a legal amount 
	 * and if each of the places where resources are stored has enough room for the resources they are requesting to store there.
	 * Adds the resource amount to the player's corresponding place and removes it from the turn state if conditions are met,
	 * raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param storage contains all the resources involved in the action
	 */
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
					} else {handleError("There isn't enough room for the resources you are trying to store", player);}
				} else {handleError("You cannot store more than you should", player);}
			} else {handleError("You must pay all of the cost first", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active and if they already paid every cost,
	 * Discards all the remaining resources from the turn state and moves forward other players if conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 */
	public void handleDiscardResources(Player player) {
		if (checkPlayer(player)){
			if (game.getTurn().getRequiredResources().isEmpty()){
				int discarded = game.getTurn().getProducedResources().size();
				for (Player p : game.getPlayers()){
					if (!p.equals(player)){
						handleVaticanReports(p, p.moveForward(discarded));
					}
				}
				game.getTurn().clearProducedResources();
			} else {handleError("You must pay all of the cost first", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * upon receiving the corresponding message, checks if the player who requested the action is active.
	 * swaps the player's warehouse rows if conditions are met, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 * @param swap1 is the first row to be swapped
	 * @param swap2 is the second row to be swapped
	 *
	 */
	public void handleRearrange(Player player, int swap1, int swap2) {
		if (checkPlayer(player)){
			try {
				player.swapRows(swap1, swap2);
			} catch (IllegalArgumentException e){handleError("You cannot swap the selected rows", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * END TURN
	 * */
	
	/**
	 * Checks if the player has bought at least 7 development cards or has reached the end of the track and triggers the round to be the last
	 *
	 * @param player is the active player
	 */
	protected void checkLastTurn(Player player){
		// count the DevelopmentCards of the player
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

	/**
	 * Upon receiving the corresponding message, checks if the player who requested the action is active, if they have played a main action, if they paid the cost of their action completely and
	 * completely stored all of their gain.
	 * End the turn and starts the next player's turn if conditions are met, triggering the last round if needed, raises corresponding error otherwise.
	 *
	 * @param player is the player who requested the action
	 *
	 */
	public void handleEndTurn(Player player) {
		if (checkPlayer(player)){
			if (game.getTurn().hasDoneAction() && game.getTurn().getRequiredResources().isEmpty() && game.getTurn().getProducedResources().isEmpty()){
				checkLastTurn(player);
				game.endTurn();
			} else {handleError("You cannot end your turn now", player);}
		} else {handleError("It is not your turn", player);}
	}

	/**
	 * PERSISTENCE
	 */

	/**
	 * Upon receiving the corresponding message, save the current state of the match in an appropriate json file
	 *
	 * @param player is the player who requested the action
	 */
	public void handlePersistence(Player player) {
		PersistenceWriter.writePersistenceFile(this.match_name, this.game);
	}
}
