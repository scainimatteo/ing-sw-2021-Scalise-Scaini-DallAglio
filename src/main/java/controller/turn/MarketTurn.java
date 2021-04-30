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
	 * @param is the resource to be put away
	 * @return if the given resource can be put in extra space
	 */
	private boolean hasExtraSpace(Resource res){
		for (ExtraSpaceAbility x : extra_space){
			if (x.getResourceType().equals(res) && x.peekResources() < 2){ 
				return true;
			}
		}
		return false;
	}
	
	/**
 	 * Allows the player to position the gained resources however they want in their Warehouses and Strongboxes
	 * 
	 * @param resources are the resources to be inserted
	 * @param must_discard flags wether resources have to be discarded or can be put into the strongbox
	 * @return the number of discarded resources
	 */
	protected int storeResources(Resource[] resources){
		boolean has_decided;
		int discarded_resources = 0;
		Resource[] single_resource = new Resource[1];
		//TODO: print Warehouse
		for (Resource x : resources){
			has_decided = false;
			if (x != null) {
				while (!has_decided){
					while (handler.pickFlow("Do you want to rearrange the warehouse?")){
						//da cambiare in un array di due!
						int arg = (Integer)handler.pickBetween (new Integer[] {1, 2, 3});
						player.swapRows(arg, arg);
						//TODO: print
						
					}
					if (this.hasExtraSpace(x)) {
						if(handler.pickFlow("Do you want to use your extra_space LeaderCard?")){
							try {
								this.extra_space[0].putResource(x);
							} catch (IllegalArgumentException e) {

								this.extra_space[1].putResource(x);
							}
							has_decided = true;
						}
					} else {
						//TODO: view informs the player that they cannot use extra space abilities
						//TODO: change warehouse method arguments to take single resources
						if (player.isPossibleToInsert(x)){
							if (handler.pickFlow("Do you want to use your warehouse?")){
								single_resource[0] = x;
								player.tryToInsert(single_resource);
								has_decided = true;
							}
						} else {
							//TODO: view informs the player that they cannot put the resource in the warehouse
						}
					}
					if (!has_decided && handler.pickFlow ("Do you want to discard the resource?")){
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
		int lost_faith = storeResources(gained_resources);
		return new FaithController(player, gained_faith, lost_faith);
	}
	
}
