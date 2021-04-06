package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Table;

public class DevelopmentCardsSlots {
	private Table<DevelopmentCard> slots;

	public DevelopmentCardsSlots(){
		this.slots = new Table<DevelopmentCard>(1, 3);
	}

	public void buyCard(DevelopmentCard card, int position) {
		this.slots.addElement(card, 1, position);
	}

	public DevelopmentCard[] getTopCards(){
		DevelopmentCard[] to_return = {slots.peekTop(1, 0), slots.peekTop(1, 1), slots.peekTop(1, 2)};
		return to_return;
	}
}
