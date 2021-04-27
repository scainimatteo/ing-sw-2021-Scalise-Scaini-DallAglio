package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.controller.turn.Turn;
import it.polimi.ingsw.controller.util.FaithController;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

public class BuyCardTurn extends Turn{
	public DevelopmentCardsOnTable dev_cards_on_table;
	public Resource[] discounts;
	
	public BuyCardTurn (Player player, DevelopmentCardsOnTable cards){
		this.player = player;
		this.discounts = new Resource[2]; 
		this.dev_cards_on_table = cards;
	}

	/**
	* Adds all discounted resources from the player's LeaderCard deck to the turn's discounts
	**/
	private void checkDiscounts(){
		DiscountAbility test = new DiscountAbility(null);
		DiscountAbility cast;
		int index = 0;
		for (LeaderCard card : player.getDeck()){
			if (card.isActive() && card.getAbility().checkAbility(test)){
				cast = (DiscountAbility) card.getAbility();
				discounts[index] = cast.getDiscountedResource();
				index++;
			}
		}
	}

	/**
	* Checks if the given card satisfies the requirements for buying it, considering the discount too.
	* 
	* @param chosen_card is the card the player wants to buy
	* @return true if the card does satisfy requirements
	*/
	private boolean checkRequirements(DevelopmentCard chosen_card) { 
		DevelopmentCard temp_card = chosen_card.applyDiscount(discounts);
		for (boolean bool : player.isBuyable(temp_card)){
			if (bool == true) {
				return true;
			}
		}
		return false;
	}
	
	/**
	* Allows the player to choose one card from the table
	*
	* @return the chosen card
	*/
	private DevelopmentCard chooseCardFromTable() {
		DevelopmentCard[][] curr_cards = dev_cards_on_table.getTopCards();
		//TODO: richiesta di quale carta è desiderata dal giocatore
		int i = 0, j = 0;
		return curr_cards[i][j];
	}

	/** 
	 * Removes resources equal to the cost from the player and allows them to choose where to get them out of
	 */

	private void payCost(DevelopmentCard card) {
		DevelopmentCard temp_card = card.applyDiscount(discounts);
		boolean has_decided;
		for (Resource x: temp_card.getCost()){
			has_decided = false;
			while (!has_decided){
				if (handler.pickFlow("Do you want pay this cost from your warehouse?")){
					try {
						player.getFromWarehouse(x, 1);
						has_decided = true;
					} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
						//communicate failure to the player
					}
				}
				else if (handler.pickFlow("Do you want to pay this cost from your leader card?")){
					//try getfromExtraSpace
					//TODO: implement getfromExtraSpace
				}
				else if (handler.pickFlow("Do you want to pay this cost from your strongbox?")){
					
					try {
						player.removeResources(x, 1);
						has_decided = true;
					} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
						//communicate failure to the player
					}
				}
			}
		}
	}

	/**
	* Checks for the presence of abilities, asks the player to choose a card until a fitting card is chosen, then draws it from the table and pays the resources
	* 
	* @return a faith controller set to zero
	*/
	@Override
	protected FaithController playAction(){
		int gained_faith = 0;
		checkDiscounts();
		DevelopmentCard chosen_card = chooseCardFromTable();
		while (!checkRequirements(chosen_card)){
			chosen_card = chooseCardFromTable();
			//what if there is no buyable card?
		} 
		dev_cards_on_table.getFromDeck(chosen_card);
		payCost(chosen_card);
		Integer pos = (Integer) handler.pickBetween( new Integer[] {1,2,3});
		boolean[] fitting_slots = player.isBuyable(chosen_card);
		while (!fitting_slots[pos]){
			pos = (Integer) handler.pickBetween ( new Integer[] {1,2,3});
		}
		player.buyCard(chosen_card, pos);
		return new FaithController(this.player, gained_faith,0);
	}	
}
