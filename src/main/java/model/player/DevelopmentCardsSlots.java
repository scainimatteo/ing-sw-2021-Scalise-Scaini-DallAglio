package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Table;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.view.cli.Printer;
import it.polimi.ingsw.view.Viewable;

import java.lang.IllegalArgumentException;
import java.util.Iterator;

import java.io.Serializable;

public class DevelopmentCardsSlots implements Viewable, Serializable {
	private static final long serialVersionUID = 77367L;
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
	public void buyCard(DevelopmentCard card, int pos) throws IllegalArgumentException {
		if (pos > 3 || pos < 0 ||(card.getCardLevel().getLevel() - slots.peekTop(0, pos).getCardLevel().getLevel() != 1)){
			throw new IllegalArgumentException();
		} else {
			this.slots.addElement(card, 0, pos);
		}
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

	public String printText() {
		DevelopmentCard[] development_cards_on_top = this.getTopCards();
		return Printer.printArray(development_cards_on_top);
	}

	public String printText(int index) {
		Deck<DevelopmentCard> deck = this.slots.getDeck(0, index);
		DevelopmentCard[] development_cards_in_deck = new DevelopmentCard[deck.size()];
		Iterator<DevelopmentCard> iterator = deck.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			development_cards_in_deck[i] = iterator.next();
		}
		return Printer.printArray(development_cards_in_deck);
	}
}
