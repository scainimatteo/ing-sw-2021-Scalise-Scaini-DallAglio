package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Table;
import it.polimi.ingsw.model.card.Deck;

public class DevelopmentCardsSlots {
	private Table<DevelopmentCard> slots;

	public DevelopmentCardsSlots(){
		this.slots = new Table<DevelopmentCard>(1, 3);
		slots.addDeck(new Deck<DevelopmentCard>(3), 0, 0);
		slots.addDeck(new Deck<DevelopmentCard>(3), 0, 1);
		slots.addDeck(new Deck<DevelopmentCard>(3), 0, 2);
	}

	/**
	 * @param card is the card that needs to be added to the slots
	 * @param position is the deck where will be added the card
	 */
	public void buyCard(DevelopmentCard card, int position) {
		this.slots.addElement(card, 0, position);
	}

	/**
	 * @return an array cointaining the first card of every deck
	 */
	public DevelopmentCard[] getTopCards(){
		DevelopmentCard[] to_return = {slots.peekTop(0, 0), slots.peekTop(0, 1), slots.peekTop(0, 2)};
		return to_return;
	}

	/**
	 * @param position is the number of the deck
	 * @return the card on the top of the deck
	 */
	public DevelopmentCard[] getCard(int position){
		DevelopmentCard[] to_return = {slots.peekTop(0, position)};
		return to_return;
	}
}
