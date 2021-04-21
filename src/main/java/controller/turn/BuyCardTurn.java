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
		} 
		//TODO: choose which slot to fit it in
		//int pos = pickPOS();	
		//bool method = question(warehouse ability?)
		int pos = 0;
		boolean method = false;
		dev_cards_on_table.getFromDeck(chosen_card);
		player.buyCard(chosen_card, pos, method);
		return new FaithController(this.player, gained_faith,0);
	}	
}
