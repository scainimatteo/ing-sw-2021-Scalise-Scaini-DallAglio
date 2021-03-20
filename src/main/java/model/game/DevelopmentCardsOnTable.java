package it.polimi.ingsw.model.game;

import java.util.NoSuchElementException;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Deck;

public class DevelopmentCardsOnTable {
	private Deck[][] development_card_decks;

	public DevelopmentCardsOnTable(Deck[][] development_card_decks) {
		this.development_card_decks = development_card_decks;
	}

	/**
	 * @return a matrix of cards at the top of the development cards deck
	 */
	public DevelopmentCard[][] getTopCards() {
		DevelopmentCard[][] development_card_on_top = new DevelopmentCard[3][4];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				development_card_on_top[i][j] = development_card_decks[i][j].getTopCard();
			}
		}
		return development_card_on_top;
	}

	/**
	 * Removes the chosen card from the development cards deck
	 *
	 * @param chosen_card the card chosen from the one displayed
	 * @throws NoSuchElementException if the chosen card is not at the top of any deck
	 */
	public void getFromDeck(DevelopmentCard chosen_card) {
		boolean done = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (development_card_decks[i][j].getTopCard().equals(chosen_card)) {
					development_card_decks[i][j].removeTopCard();
					done = true;
					break;
				}
			}
		}

		if (!done) {
			throw new NoSuchElementException();
		}
	}
}
