package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*; 
import static org.junit.jupiter.api.Assertions.*;   

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import it.polimi.ingsw.model.card.Deck;

public class TableTest {
	private Table<Object> table;
	private int max_size;
	private int row_size;
	private int column_size;
	private int deck_size;
	private ArrayList<Object> objects;

	/**
	 * @param bound the max number to be returned
	 * @return a random integer greater than zero and smaller than the bound
	 */
	private int randomIntGreaterThanZero(int bound) {
		return new Random().nextInt(bound - 1) + 1;
	}

	/**
	 * Create a random istance of Table
	 */
	@BeforeEach
	public void createTable() {
		this.row_size = randomIntGreaterThanZero(6);
		this.column_size = randomIntGreaterThanZero(6);
		this.max_size = this.row_size * this.column_size;
		this.deck_size = randomIntGreaterThanZero(8);

		this.objects = new ArrayList<Object>();
		this.table = new Table<Object>(this.row_size, this.column_size);
		for (int i = 0; i < this.max_size; i++) {
			// create the decks and fill them with random objects
			Deck<Object> deck = new Deck<Object>(this.deck_size);
			for (int j = 0; j < this.deck_size; j++) {
				Object new_obj = new Object();
				deck.add(new_obj);
				this.objects.add(new_obj);
			}
			this.table.addDeck(deck, i / row_size, i % row_size);
		}
	}

	/**
	 * Check that the iterator returns the cards at the top of the decks
	 */
	@RepeatedTest(value = 3)
	public void IteratorTest() {
		Iterator<Object> iterator = this.table.iterator();
		for (int i = 0; iterator.hasNext(); i++) {
			assertEquals(iterator.next(), this.objects.get((this.deck_size * (i + 1)) - 1));
		}
	}

	@RepeatedTest(value = 3)
	public void peekTopTest() {
		Random random = new Random();
		int row = random.nextInt(this.column_size);
		int column = random.nextInt(this.row_size);

		Object object = this.table.peekTop(row, column);
		assertEquals(object, this.objects.get(deck_size * (row_size * row + column + 1) - 1));
	}

	@RepeatedTest(value = 3)
	public void drawTest() {
		Random random = new Random();
		int row = random.nextInt(this.column_size);
		int column = random.nextInt(this.row_size);

		this.table.draw(row, column);
		Object object = this.table.peekTop(row, column);

		int index = deck_size * (row_size * row + column + 1) - 1;
		if (deck_size != 1 && index != 0) {
			assertEquals(object, this.objects.get(index - 1));
		} else {
			assertNull(object);
		}
	}
}
