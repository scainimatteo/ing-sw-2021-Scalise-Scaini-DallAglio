package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceController;
import it.polimi.ingsw.controller.util.ResourceController;

import java.util.NoSuchElementException;
import java.util.Arrays;

public class MarketTurn extends Turn {
	private Market market;
	private Resource[] whiteMarble;

	public MarketTurn (Player player, ChoiceController handler, Market market){
		this.player = player;	
		this.market = market;
		this.whiteMarble = new Resource[2];
		this.handler = handler;
		this.res_controller = new ResourceController(player, handler);
	}

	/**
	 * @return the index of the column choosen by the player
	 */
	private int chooseColumn(){
		Integer clientreturn = 0;
		Integer[] options = new Integer[] {0, 1, 2, 3};
		clientreturn = (Integer) handler.pickBetween(player, "Da quale colonna vuoi prendere le risorse?", options, 1)[0];
		return clientreturn;
	}
	
	/**
	 * @return the index of the row choosen by the player
	 */
	private int chooseRow(){
		Integer clientreturn = 0;
		Integer [] options = new Integer [] {0, 1, 2};
		clientreturn = (Integer) handler.pickBetween(player, "Da quale riga vuoi prendere le risorse?", options, 1)[0];
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
				if (starting_resources[i] == null && handler.pickFlow(player, "Do you want to use marble bonus?")) { 
					starting_resources[i] = (Resource) handler.pickBetween(player, "Which resource would you like to get?", whiteMarble, 1)[0];
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
		if (handler.pickFlow(player, "Do you want to get resources from a column or a row?")){
			bought = market.getColumn(chooseColumn());
		}
		else {
			bought = market.getRow(chooseRow());
		}
		return bought;
	}

	public FaithController playAction (){
		checkWhiteMarbles();
		// TODO: view market before choice
		Resource[] gained_resources = getFromMarket();
		applyBonus(gained_resources);
		int gained_faith = res_controller.countFaith (gained_resources);	
		int lost_faith = res_controller.storeFromMarket(gained_resources);
		return new FaithController(player, gained_faith, lost_faith);
	}
	
}

