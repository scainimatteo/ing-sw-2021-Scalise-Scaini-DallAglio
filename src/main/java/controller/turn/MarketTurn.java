package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import java.util.NoSuchElementException;

public class MarketTurn extends Turn {
	private Market market;
	private Resource[] whiteMarble;
	//private ExtraSpaceAbility[] extra_space;

	public MarketTurn (Player player, Market market){
		this.player = player;	
		this.market = market;
		this.whiteMarble = new Resource[2];
	}

	/**
	* @return the index of the column choosen by the player
	*/
	private int ChooseColumn(){
		int clientreturn = 0;
		int[] options = {0, 1, 2, 3};
		clientreturn = handler.pickBetween(options);
		return clientreturn;
	}
	
	/**
	* @return the index of the row choosen by the player
	*/
	private int ChooseRow(){
		int clientreturn = 0;
		int[] options = {0, 1, 2};
		clientreturn = handler.pickBetween(options);
		return clientreturn;
	}
	
	/**
	* Adds all bonus resources from the player's LeaderCard deck to the turn's bonuses 
	*/
	private Resource checkWhiteMarbles(){
		WhiteMarblesAbility test = new WhiteMarbleAbility (null);
		WhiteMarblesAbility cast;
		int index = 0;
		for (LeaderCard card: player.getDeck()){
			if (card.isActive() && card.getAbility().checkAbility(test)){
				cast = (WhiteMarblesAbility) card.getAbility();
				discount[index] = cast.getResourceType();
				index++;
			}
		}
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
			for (int i = 0; i < starting_resources.lenght(); i++) {
				if (starting_resource[i] == null && handler.pickFlow ("Do you want to use marble bonus?")) { 
					starting_resource[i] = handler.pickBetween (whiteMarble);
				}
			}
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
			picked = ChooseColumn();
			bought = market.getColumn(picked);
			market.shiftColumn(picked);	
		}
		else {
			picked = ChooseRow();
			bought = market.getRow(picked);
			market.shiftRow(picked);
		}
	}
	
	/**
	* Counts the amount of Faith type resources in a given resource vector
	*
	* @param resources is the resource in which to count 
	* @return the amount
	*/
	
	private int countFaith(Resource[] resources){
		int count;
		for (Resource x : resources){
			if (x.equals(Resource.FAITH)){
				count++
			}
		}
		return count;
	}

	public FaithController playAction(){
		checkWhiteMarbles();
		// TODO: view market before choice
		Resource[] gained_resources = getFromMarket();
		applyBonus(gained_resources);
		int gained_faith = countFaith (gained_resources);	
		// TODO: gestire l'inserimento nel Warehouse	
	}
	
}
