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
	DevelopmentCardsOnTable dev_cards_on_table;
	
	public BuyCardTurn (Player player, DevelopmentCardsOnTable cards){
		this.player = player;
		this.dev_cards_on_table = cards;
	}
	
	/**
	* Checks if the given card satisfies the requirements for buying it, considering the discount too.
	* 
	* @param chosen_card is the card the player wants to buy
	* @param discount is the discounted resource to be kept in consideration
	* @return true if the card does satisfy requirements
	*/
	//TODO: find a way to account for the discount before checking the requirements
	private boolean checkRequirements(DevelopmentCard chosen_card, Resource discount) { 
		/*for (boolean bool : player.isBuyable(chosen_card){
			if (bool == true) {
				return true;
			}
		}
		if (discount != null){
			Resource[] applied_discount = chosen_card.getCost();
			for (int i = 0; int < applied_discount.lenght(); i++{
				if (applied_discount[i].equals(discount)){
					applied_discount[i] = null;
					break;
				}
			for (boolean bool : player.isBuyable(applied_discount){
				if (bool == true) {
					return true;
				}
			}
		}*/
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
		DiscountAbility ability = (DiscountAbility) checkAbility (new DiscountAbility(null));
		Resource discount = null;
		if (ability != null){
			discount = ability.getDiscountedResource();
		}	
		DevelopmentCard chosen_card = chooseCardFromTable();
		while (!checkRequirements(chosen_card, discount)){
			chosen_card = chooseCardFromTable();
		} 
		//TODO: choose which slot to fit it in
		//int pos = pickPOS();	
		//bool method = question(warehouse ability?)
		int pos = 0;
		boolean method = false;
		dev_cards_on_table.getFromDeck(chosen_card);
		player.buyCard(chosen_card, pos, method);
		return new FaithController(0,0);
	}	
}
