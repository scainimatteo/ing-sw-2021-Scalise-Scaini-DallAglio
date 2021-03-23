package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import java.util.Collection;

public abstract class Deck implements Collection {
	private int max_dimension;

	public Card getTopCard() {
		return null;
	}

	public Card peekTopCard() {
		return null;
	}
}
