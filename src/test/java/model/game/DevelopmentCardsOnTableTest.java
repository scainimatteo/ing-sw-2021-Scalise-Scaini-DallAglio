package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.game.Factory;

public class DevelopmentCardsOnTableTest {
	private DevelopmentCardsOnTable dct;

	/**
	 * Create an instance of DevelopmentCardsOnTable
	 */
	@BeforeEach
	public void createDevelopmentCardsOnTable() throws IOException, ParseException{
		this.dct = new DevelopmentCardsOnTable(Factory.getInstance().getAllDevelopmentCards());
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
		DevelopmentCard new_card = new DevelopmentCard(0, null, null, null, 0, null);
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

	/**
	 * Check that all the cards in a deck are of the right level and color
	 */
	@RepeatedTest(value = 3)
	public void cardsInRightDeckTest() {
		Random random = new Random();
		DevelopmentCard[][] top_cards = dct.getTopCards();

		int row = random.nextInt(3);
		int column = random.nextInt(4);
		//System.out.printf("Random: %d %d\n", row, column);

		for (int i = 0; i < 4; i++) {
			CardLevel cardlevel = top_cards[row][column].getCardLevel();
			assertEquals(cardlevel.getLevel(), 3 - row);
			assertEquals(cardlevel.getColor(), DevelopmentCardsColor.values()[column]);
			dct.getFromDeck(top_cards[row][column]);
			top_cards = dct.getTopCards();
		}
	}
}
