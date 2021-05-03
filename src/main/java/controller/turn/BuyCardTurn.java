package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceController;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

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

	private boolean hasResourceInExtraSpace (Resource res){
		return true;
	}

	/**
	 * @param card is the DevelopmentCard to be checked
	 * @return true if the warehouse or the strongbox contains the resources requested for the buy
	 * TODO: fix this
	 *
	public boolean[] isBuyable(DevelopmentCard card){
		Resource[] tmp = card.getCost();
		boolean tmp_boolean = true;
		boolean[] to_return = {false, false, false};
		int card_level = card.getCardLevel().getLevel();

		if ( !(player.areContainedInWarehouse(tmp) || player.areContainedInStrongbox(tmp)) ){
			for (Resource res : tmp){
				if (res != null){
					tmp_boolean = false;
				}
			}
		}

		if (tmp_boolean){
			DevelopmentCard[] devcard = this.development_card_slots.getTopCards();

			if (devcard[0] != null){
				if (card_level - devcard[0].getCardLevel().getLevel() == 1){
					to_return[0] = true;
				}
			} else if (card_level == 1){ 
				to_return[0] = true;
			}

			if (devcard[1] != null){
				if (card_level - devcard[1].getCardLevel().getLevel() == 1){
					to_return[1] = true;
				}
			} else if (card_level == 1){
				to_return[1] = true;
			} 

			if (devcard[2] != null){
				if (card_level - devcard[2].getCardLevel().getLevel() == 1){
					to_return[2] = true;
				}
			} else if (card_level == 1){
				to_return[2] = true;
			}
		}

		return to_return;
	} */


	/**
	* Checks if the given card satisfies the requirements for buying it, considering the discount too.
	* 
	* @param chosen_card is the card the player wants to buy
	* @return true if the card does satisfy requirements
	*/
	private boolean checkRequirements(DevelopmentCard chosen_card) { 
		//DevelopmentCard temp_card = chosen_card.applyDiscount(discounts);
		//for (boolean bool : player.isBuyable(temp_card)){
		//	if (bool == true) {
		//		return true;
		//	}
		//}
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
	//TODO: implement with pickBetween and implicit representation
	@Override
	protected void payResources(Resource[] resources) {
		boolean has_decided;
		for (Resource x: resources) {
			has_decided = false;
			while (!has_decided){
				if (player.getPlayerWarehouse().areContainedInWarehouse(new Resource[] {x})){
					if (handler.pickFlow(player, "Do you want pay this cost from your warehouse?")){
						player.getFromWarehouse(x, 1);
						has_decided = true;
					}
				} else {
				//warn the player of impossibility
					if (hasResourceInExtraSpace(x)){
						if (handler.pickFlow(player, "Do you want to pay this cost from your leader card?")){
							if (extra_space[0].getResourceType().equals(x)){
								extra_space[0].getResource(1);
							} else {
								extra_space[1].getResource(1);
							}
						}
					} else {
					//TODO: warn the player of impossibility
						if (player.getPlayerStrongbox().areContainedInStrongbox(new Resource[] {x})){
							if (handler.pickFlow(player, "Do you want to pay this cost from your strongbox?")){
						
								player.removeResources(x, 1);
								has_decided = true;
							}
						} else {
						//communicate failure to the player
						}
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
		payResources(chosen_card.applyDiscount(discounts).getCost());
		Integer pos = (Integer) handler.pickBetween(player, "In which slot do you want to put it?", new Integer[] {1,2,3}, 1)[0];
		boolean[] fitting_slots = null;//player.isBuyable(chosen_card);
		while (!fitting_slots[pos]){
			pos = (Integer) handler.pickBetween(player, "In which slot do you want to put it?", new Integer[] {1,2,3}, 1)[0];
		}
		player.buyCard(chosen_card, pos.intValue());
		return new FaithController(this.player, gained_faith,0);
	}	
}
