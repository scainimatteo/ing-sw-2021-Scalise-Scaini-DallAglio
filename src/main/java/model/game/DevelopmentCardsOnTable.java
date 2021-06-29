package it.polimi.ingsw.model.game;

import java.util.NoSuchElementException;
import java.util.ArrayList;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.card.Table;
import it.polimi.ingsw.model.card.Deck;

public class DevelopmentCardsOnTable {
	private Table<DevelopmentCard> development_cards_table;
	public static final int dim_rows = 4;
	public static final int dim_cols = 3;

	public DevelopmentCardsOnTable(DevelopmentCard[] all_development_cards) {
		this.development_cards_table = createDecks(all_development_cards);
	}

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public DevelopmentCardsOnTable(Table<DevelopmentCard> development_cards_table) {
		this.development_cards_table = development_cards_table;
	}

	private Table<DevelopmentCard> createDecks(DevelopmentCard[] all_development_cards) {
		Table<DevelopmentCard> table = new Table<DevelopmentCard>(dim_rows, dim_cols);
		for (int i = 0; i < dim_cols; i++) {
			for (int j = 0; j < dim_rows; j++) {
				table.addDeck(new Deck<DevelopmentCard>(4), i, j);
			}
		}
		for (DevelopmentCard card : all_development_cards) {
			CardLevel level = card.getCardLevel();
			table.addElement(card, 3 - level.getLevel(), level.getColor().getOrder());
		}
		table.shuffleAllDecks();
		return table;
	}

	/**
	 * Persistence
	 * TODO: Better comment
	 */
	public ArrayList<DevelopmentCard> getDeckAsArrayList(int row, int column) {
		return this.development_cards_table.getDeckAsArrayList(row, column);
	}

	/**
	 * @return a matrix of cards at the top of the development cards deck
	 */
	public DevelopmentCard[][] getTopCards() {
		DevelopmentCard[][] development_cards_on_top = new DevelopmentCard[3][4];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				development_cards_on_top[i][j] = this.development_cards_table.peekTop(i, j);
			}
		}
		return development_cards_on_top;
	}

	/**
	 * Removes the chosen card from the development cards deck
	 *
	 * @param chosen_card the card chosen from the one displayed
	 * @throws NoSuchElementException if the chosen card is not at the top of any deck
	 */
	public void getFromDeck(DevelopmentCard chosen_card) throws NoSuchElementException {
		boolean done = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				DevelopmentCard card_on_top = development_cards_table.peekTop(i, j);
				if (card_on_top != null && card_on_top.equals(chosen_card)) {
					development_cards_table.draw(i, j);
					done = true;
					break;
				}
			}
		}

		if (!done) {
			throw new NoSuchElementException();
		}
	}

	/**
	 * Returns the size of the given deck
	 *
	 * testing only
	 */
	public int getDeckSize(int i, int j){
		return development_cards_table.getDeck(i,j).size();
	}
}
