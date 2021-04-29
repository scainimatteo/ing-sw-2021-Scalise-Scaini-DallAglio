package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceHandler;

import java.util.NoSuchElementException;
import java.util.Arrays;

public class MarketTurn extends Turn {
	private Market market;
	private Resource[] whiteMarble;
	private ExtraSpaceAbility[] extra_space;

	public MarketTurn (Player player, Market market){
		this.player = player;	
		this.market = market;
		this.whiteMarble = new Resource[2];
		this.extra_space = new ExtraSpaceAbility[2];
		this.handler = new ChoiceHandler();
	}

	/**
	* @return the index of the column choosen by the player
	*/
	private int chooseColumn(){
		Integer clientreturn = 0;
		Integer[] options = new Integer[] {0, 1, 2, 3};
		clientreturn = (Integer) handler.pickBetween(Arrays.copyOf(options, options.length, Object[].class));
		return clientreturn;
	}
	
	/**
	* @return the index of the row choosen by the player
	*/
	private int chooseRow(){
		Integer clientreturn = 0;
		Integer [] options = new Integer [] {0, 1, 2};
		clientreturn = (Integer) handler.pickBetween(Arrays.copyOf(options, options.length, Object[].class));
		return clientreturn;
	}
	
	/**
	* Adds all bonus resources from the player's LeaderCard deck to the turn's bonuses 
	*/
	private void checkWhiteMarbles(){
		WhiteMarblesAbility test = new WhiteMarblesAbility (null);
		WhiteMarblesAbility cast;
		int index = 0;
		for (LeaderCard card: player.getDeck()){
			if (card.isActive() && card.getAbility().checkAbility(test)){
				cast = (WhiteMarblesAbility) card.getAbility();
				whiteMarble[index] = cast.getResourceType();
				index++;
			}
		}
	}

	/**
	* Adds all extra space from the player's LeaderCard deck to the turn's available extra space
	*/
	private void checkExtraSpace(){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		int index = 0;
		for (LeaderCard card: player.getDeck()){
			if (card.isActive() && card.getAbility().checkAbility(test)){
 				extra_space[index] = (ExtraSpaceAbility) card.getAbility();
;
				index ++;
			}
		}
	}
	
	private boolean hasExtraSpace(Resource res){
		for (ExtraSpaceAbility x : extra_space){
			if (x.getResourceType().equals(res) && x.peekResources() < 2){ 
				return true;
			}
		}
		return false;
	}
	
	/**
	* Applies given bonuses to the argument string by turning null pointers into resources according to the player's request 
	* 
	* @param starting_resources is the ResourceVector to apply the bonuses on
	*/	
	private void applyBonus(Resource[] starting_resources){		
		if (whiteMarble[0] == null && whiteMarble[1] == null){
			return;
		}
		else {
			for (int i = 0; i < starting_resources.length; i++) {
				if (starting_resources[i] == null && handler.pickFlow ("Do you want to use marble bonus?")) { 
					starting_resources[i] = (Resource) handler.pickBetween (Arrays.copyOf(whiteMarble, whiteMarble.length, Object[].class));
				}
			}
			return;
		}
	}	
	
	/**
	* Allows the player to choose if they want to pick a row, a column, and which one, then applies desired LeaderAbility bonuses
	*
	* @return the resources extracted from the market
	*/ 
	private Resource[] getFromMarket(){
		Resource[] bought;
		int picked;
		if (handler.pickFlow(null)){
			bought = market.getColumn(chooseColumn());
		}
		else {
			bought = market.getRow(chooseRow());
		}
		return bought;
	}
	
	/**
	* Counts the amount of Faith type resources in a given resource vector
	*
	* @param resources is the resource in which to count 
	* @return the amount
	*/
	
	private int countFaith(Resource[] resources){
		int count = 0;
		for (Resource x : resources){
			if (x.equals(Resource.FAITH)){
				count++;
			}
		}
		return count;
	}

	/**
	* Allows the player to position the gained resources however they want in their Warehouses
	* 
	* @return the number of discarded resources
	*/
	private int arrangeResources(Resource[] resources){
		boolean has_decided;
		int discarded_resources = 0;
		//TODO: print Warehouse
		for (int i = 0; i < resources.length; i++){
			has_decided = false;
			if (resources[i] != null) {
				while (!has_decided){
					while (handler.pickFlow("Do you want to rearrange the warehouse?")){
						//TODO: add swapping lines method and print
					}
					if (this.hasExtraSpace(resources[i])) {
						if(handler.pickFlow("Do you want to use your extra_space LeaderCard?")){
						has_decided = true;
						//operations
						}
					}
					else {
					//operations on warehouse
						has_decided = true;
					}
					if (has_decided == false && handler.pickFlow ("Do you want to discard the resource?")){
						discarded_resources++;
						has_decided = true;
					}
				}
			}
		}
		return discarded_resources;
	}

	public FaithController playAction (){
		checkWhiteMarbles();
		// TODO: view market before choice
		Resource[] gained_resources = getFromMarket();
		applyBonus(gained_resources);
		int gained_faith = countFaith (gained_resources);	
		// TODO: gestire l'inserimento nel Warehouse	
		return new FaithController(player, 0, 0);
	}
	
}
