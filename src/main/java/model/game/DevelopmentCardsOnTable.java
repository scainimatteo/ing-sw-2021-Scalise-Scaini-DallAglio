package it.polimi.ingsw.model.game;

import java.util.NoSuchElementException;
import java.util.ArrayList;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.card.Deck;

public class DevelopmentCardsOnTable {
	private Deck[][] development_card_decks;
	private final int dim_rows = 4;
	private final int dim_cols = 3;

	public DevelopmentCardsOnTable(DevelopmentCard[] all_development_cards) {
		this.development_card_decks = createDecks(all_development_cards);
	}

	private Deck[][] createDecks(DevelopmentCard[] all_development_cards) {
		Deck[][] decks = new Deck[dim_cols][dim_rows];
		for (int i = 0; i < dim_cols; i++) {
			for (int j = 0; j < dim_rows; j++) {
				decks[i][j] = new Deck(4, new ArrayList<DevelopmentCard>(), 4 * i + j);
			}
		}
		for (DevelopmentCard card : all_development_cards) {
			CardLevel level = card.getCardLevel();
			decks[level.getLevel() - 1][level.getColor().getRepresentation()].addElement(card);
		}
		return decks;
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
