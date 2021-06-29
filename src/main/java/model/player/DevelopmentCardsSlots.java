package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Table;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.view.cli.Printer;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.Iterator;

public class DevelopmentCardsSlots {
	private Table<DevelopmentCard> slots;

	public DevelopmentCardsSlots(){
		this.slots = new Table<DevelopmentCard>(1, 3);
		slots.addDeck(new Deck<DevelopmentCard>(3), 0, 0);
		slots.addDeck(new Deck<DevelopmentCard>(3), 0, 1);
		slots.addDeck(new Deck<DevelopmentCard>(3), 0, 2);
	}

	/**
	 * Persistence only - recreate a DevelopmentCardsSlots from the match saved in memory
	 */
	public DevelopmentCardsSlots(Table<DevelopmentCard> slots){
		this.slots = slots;
	}

	/**
	 * @param card is the DevelopmentCard that needs to be added to the slots
	 * @param position the Deck where the DevelopmentCard will be added
	 */
	public boolean fitsInSlot(DevelopmentCard card, int pos){
		if (slots.peekTop(0, pos) == null) {
			return card.getCardLevel().getLevel() == 1;
		}

		return pos <= 2 && pos >= 0 && card.getCardLevel().isPlaceable(slots.peekTop(0, pos).getCardLevel());
	}

	/**
	 * Add a DevelopmentCard to the slot
	 *
	 * @param card the DevelopmentCard to add
	 * @param pos the slot to add the DevelopmentCard to
	 */
	public void buyCard(DevelopmentCard card, int pos) {
		this.slots.addElement(card, 0, pos);
	}

	public ArrayList<DevelopmentCard> getDeckAsArrayList(int row, int column){
		return this.slots.getDeckAsArrayList(row, column);
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

	public Iterator<DevelopmentCard> getIterator(){
		return slots.iterator();
	}
}
