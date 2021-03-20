package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Deck;

public class DevelopmentCardsOnTableTest {
	DevelopmentCardsOnTable dct;
	Deck[][] test_development_card_decks;

	/**
	 * Create an istance of DevelopmentCardsOnTable
	 */
	@BeforeEach
	public void createDevelopmentCardsOnTable() {
		//TODO: Functional?
		this.test_development_card_decks = new Deck[3][4];
		for (int i = 0; i < 12; i++) {
			ArrayList<DevelopmentCard> a = new ArrayList<DevelopmentCard>();
			for (int j = 0; j < 4; j++) {
				DevelopmentCard c = new DevelopmentCard(4 * i + j);
				a.add(c);
			}
			this.test_development_card_decks[i / 4][i % 4] = new Deck(4, a, i);
		}
		this.dct = new DevelopmentCardsOnTable(test_development_card_decks);
	}

	/**
	 * Check if the top cards returned are the same as the one used to create the table
	 */
	@Test
	public void getTopCardsTest() {
		DevelopmentCard[][] top_cards = dct.getTopCards();
		assertFalse(top_cards == null);
		for (int i = 0; i < top_cards.length; i++) {
			for (int j = 0; j < top_cards[i].length; j++) {
				assertEquals(top_cards[i][j], test_development_card_decks[i][j].getTopCard());
			}
		}
	}

	/**
	 * Check if all the cards returned are different from each other
	 */
	@Test
	public void allCardsDifferentTest() {
		DevelopmentCard[][] top_cards = dct.getTopCards();
		HashSet<DevelopmentCard> set = new HashSet<DevelopmentCard>();
		for (DevelopmentCard[] cards_row : top_cards) {
			for (DevelopmentCard card : cards_row) {
				// if the element is already in the set set.add will return false
				assertTrue(set.add(card));
			}
		}
	}

	/**
	 * Get a card from a random Deck and check that the card got removed
	 */
	@RepeatedTest(value = 3)
	public void getFromDeckTest() {
		Random random = new Random();
		DevelopmentCard[][] top_cards_before = dct.getTopCards();

		int row = random.nextInt(3);
		int column = random.nextInt(4);
		//System.out.printf("Random: %d %d\n", row, column);
		dct.getFromDeck(top_cards_before[row][column]);
		DevelopmentCard[][] top_cards_after = dct.getTopCards();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == row && j == column) {
					assertNotEquals(top_cards_before[row][column], top_cards_after[row][column]);
					//System.out.printf("Different: %d %d\n", top_cards_before[row][column].getId(), top_cards_after[row][column].getId());
				} else {
					assertEquals(top_cards_before[i][j], top_cards_after[i][j]);
					//System.out.printf("Equal: %d %d\n", top_cards_before[i][j].getId(), top_cards_after[i][j].getId());
				}
			}
		}
	}

	/**
	 * Check if a NoSuchElementException is thrown when a card that's not in the deck is passed
	 */
	@Test
	public void getCardNotInDeckTest() {
		DevelopmentCard new_card = new DevelopmentCard(0);
		assertThrows(NoSuchElementException.class, () -> dct.getFromDeck(new_card));
	}

	@RepeatedTest(value = 3)
	public void emptyDeck() {
		Random random = new Random();
		DevelopmentCard[][] top_cards = dct.getTopCards();

		int row = random.nextInt(3);
		int column = random.nextInt(4);
		//System.out.printf("Random: %d %d\n", row, column);

		for (int i = 0; i < 4; i++) {
			dct.getFromDeck(top_cards[row][column]);
			top_cards = dct.getTopCards();
		}

		assertNull(top_cards[row][column]);
	}
}
