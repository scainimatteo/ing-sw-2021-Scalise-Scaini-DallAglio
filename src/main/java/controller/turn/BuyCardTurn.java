package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceController;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Storage;

import it.polimi.ingsw.model.resources.Resource;
import java.util.HashMap;

public class BuyCardTurn extends Turn{
	private DevelopmentCardsOnTable dev_cards_on_table;
	private Resource[] discounts;
	
	public BuyCardTurn (Player player, ChoiceController handler, DevelopmentCardsOnTable cards){
		this.player = player;
		this.dev_cards_on_table = cards;
		this.discounts = new Resource[2]; 
		this.handler = handler;
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
	 * Checks if the player has the proper resources
	 *
	 * @param card is the DevelopmentCard to be checked
	 * @return true if storage has enough resources to pay for the entirety of the cost 
	 */
	private boolean hasResources(DevelopmentCard card){
		boolean enough_resources = true;
		HashMap <Resource, Integer> total = player.totalResources();	
		for (ExtraSpaceAbility x : extra_space){
			if (x != null){
				total.put(x.getResourceType(), total.get(x.getResourceType()) + x.peekResources());
			}
		}
		for (Resource r: card.getCost()){
			total.put(r, total.get(r) - 1);
			if (total.get(r) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the player's slots satisfy requirements
	 *
	 * @param card is the DevelopmentCard to be checked
	 * @return true if the card can be fit in any of the player slots 
	 */

	private boolean hasSlots(DevelopmentCard card){
		int card_level = card.getCardLevel().getLevel();
		DevelopmentCard[] slots = player.getTopCards();
		for (DevelopmentCard x : slots){
			if (x!= null) {	
				if (card_level - x.getCardLevel().getLevel() == 1){
					return true;
				}
			} else if (card_level == 1){
				return true;
			}
		}
		return false;
	}


	/**
	* Checks if the given card satisfies the requirements for buying it, considering the discount too.
	* 
	* @param chosen_card is the card the player wants to buy
	* @return true if the card does satisfy requirements
	*/
	private boolean checkRequirements(DevelopmentCard chosen_card) { 
		DevelopmentCard discounted_card = chosen_card.applyDiscount(discounts);
		return hasSlots(discounted_card) && hasResources(discounted_card);
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
	//TODO: implement with pickBetween and implicit representation
	@Override
	protected void payResources(Resource[] resources) {
		boolean has_decided;
		Storage storage;
		for (Resource x: resources) {
			has_decided = false;
			while (!has_decided){
				storage = (Storage) handler.pickBetween(player, "where do you want to get your resources?", new Storage[] {player.getPlayerWarehouse(), player.getPlayerStrongbox(), extra_space[0], extra_space[1]}, 1)[0];
				try {
					storage.getResource(x);
					has_decided = true;
				} catch (IllegalArgumentException e){}
			}
		}
	}
	
	private void placeInSlot(DevelopmentCard chosen_card){	
		boolean has_decided = false;
		while (!has_decided){
			Integer pos = (Integer) handler.pickBetween(player, "On top of which slot do you want to position the card", player.getTopCards(), 1)[0];
			try {
				player.buyCard(chosen_card, pos.intValue());
				has_decided = true;
				//notify player of correct execution
			} catch (IllegalArgumentException e){
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
		checkDiscounts();
		checkExtraSpace();
		DevelopmentCard chosen_card = chooseCardFromTable();
		while (!checkRequirements(chosen_card)){
			chosen_card = chooseCardFromTable();
			//what if there is no buyable card?
		} 
		dev_cards_on_table.getFromDeck(chosen_card);
		payResources(chosen_card.applyDiscount(discounts).getCost());
		placeInSlot(chosen_card);
		return new FaithController(this.player, 0, 0);
	}	
}
