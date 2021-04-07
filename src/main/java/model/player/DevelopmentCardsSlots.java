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

	public void buyCard(DevelopmentCard card, int position) {
		this.slots.addElement(card, 0, position);
	}

	public DevelopmentCard[] getTopCards(){
		DevelopmentCard[] to_return = {slots.peekTop(0, 0), slots.peekTop(0, 1), slots.peekTop(0, 2)};
		return to_return;
	}

	public DevelopmentCard[] getCard(int position){
		DevelopmentCard[] to_return = {slots.peekTop(0,0)};
		return to_return;
	}
}
