package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Random;

import it.polimi.ingsw.model.resources.Resource;

public class MarketTest {
	Market market;

	@BeforeEach
	public void createMarket() {
		this.market = new Market();
	}

	/**
	 * Add a resource to a HashMap while updating its index
	 *
	 * @param map HashMap containing the number of times a resource is found
	 * @param r resource to insert to the HashMap
	 */
	private void addToMap(HashMap<Resource, Integer> map, Resource r) {
		if (map.containsKey(r)) {
			map.put(r, map.get(r) + 1);
		} else {
			map.put(r, 1);
		}
	}

	/**
	 * Checks if every item is in the Market, including the free marble
	 */
	@Test
	public void checkEverythingTest() {
		HashMap<Resource, Integer> number_of_resources = new HashMap<Resource, Integer>();

		Resource[][] market_board = market.peekMarket();
		for (Resource[] row : market_board) {
			for (Resource r : row) {
				addToMap(number_of_resources, r);
			}
		}

		addToMap(number_of_resources, market.getFreeMarble());

		assertEquals(2, number_of_resources.get(Resource.COIN));
		assertEquals(2, number_of_resources.get(Resource.SHIELD));
		assertEquals(2, number_of_resources.get(Resource.SERVANT));
		assertEquals(2, number_of_resources.get(Resource.STONE));
		assertEquals(1, number_of_resources.get(Resource.FAITH));
		assertEquals(4, number_of_resources.get(null));
	}

	/**
	 * @param initial_row a row of the Market before it was shifted
	 * @param final_row the same row of the Market after it was shifted
	 * @return true if the shift was correct
	 */
	private boolean checkIfRowShifted(Resource[] initial_row, Resource[] final_row) {
		return (initial_row[1] == final_row[0]) && (initial_row[2] == final_row[1]) && (initial_row[3] == final_row[2]);
	}

	/**
	 * Shift all rows once and check if the shift was successful
	 */
	@Test
	public void shiftAllRowsTest() {
		for (int i = 0; i < 3; i++) {
			Resource[] initial_row = market.getRow(i);
			Resource[] final_row = market.getRow(i);
			assertTrue(checkIfRowShifted(initial_row, final_row));
		}
	}

	/**
	 * Shift the same random row four times and check that the free marble was positioned correctly
	 */
	@RepeatedTest(value = 3)
	public void shiftRowFourTimesTest() {
		Random random = new Random();
		int row = random.nextInt(3);

		Resource[] first = market.getRow(row);
		Resource[] second = market.getRow(row);
		Resource[] third = market.getRow(row);
		Resource[] fourth = market.getRow(row);

		assertTrue(first[0] == third[3]);
		assertTrue(second[0] == fourth[3]);
		assertTrue(first[3] == second[2] && third[1] == fourth[0] && first[3] == fourth[0]);
	}

	/**
	 * @param initial_column a column of the Market before it was shifted
	 * @param final_column the same column of the Market after it was shifted
	 * @return true if the shift was correct
	 */
	private boolean checkIfColumnShifted(Resource[] initial_column, Resource[] final_column) {
		return (initial_column[1] == final_column[0]) && (initial_column[2] == final_column[1]);
	}

	/**
	 * Shift all columns once and check if the shift was successful
	 */
	@Test
	public void shiftAllColumnsTest() {
		for (int i = 0; i < 3; i++) {
			Resource[] initial_column = market.getColumn(i);
			Resource[] final_column = market.getColumn(i);
			assertTrue(checkIfColumnShifted(initial_column, final_column));
		}
	}

	/**
	 * Shift the same random row four times and check that the free marble was positioned correctly
	 */
	@RepeatedTest(value = 3)
	public void shiftColumnFourTimesTest() {
		Random random = new Random();
		int column = random.nextInt(4);

		Resource[] first = market.getColumn(column);
		Resource[] second = market.getColumn(column);
		Resource[] third = market.getColumn(column);

		assertTrue(first[0] == third[2]);
		assertTrue(first[2] == second[1] && third[0] == second[1]);
	}
}
