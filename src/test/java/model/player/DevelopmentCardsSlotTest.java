package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.game.Factory;

import org.json.simple.parser.ParseException;
import java.io.IOException;

public class DevelopmentCardsSlotTest {
	/**
	 * Test the methods for the class DevelopmentCardsSlot
	 */
	@Test
	public void creationTest() throws ParseException, IOException{
		DevelopmentCardsSlots test_slot = new DevelopmentCardsSlots();

		Factory factory = Factory.getIstance();
		DevelopmentCard[] test_devcard = factory.getAllDevelopmentCards();

		DevelopmentCard[] expected0 = {null};
		DevelopmentCard[] return0 = test_slot.getCard(0);
		assertArrayEquals(expected0, return0);

		test_slot.buyCard(test_devcard[0], 0);
		DevelopmentCard[] expected1 = {test_devcard[0], null, null};
		assertArrayEquals(expected1, test_slot.getTopCards());

		test_slot.buyCard(test_devcard[3], 1);
		DevelopmentCard[] expected2 = {test_devcard[0], test_devcard[3], null};
		assertArrayEquals(expected2, test_slot.getTopCards());

		test_slot.buyCard(test_devcard[8], 0);
		DevelopmentCard[] expected3 = {test_devcard[8], test_devcard[3], null};
		assertArrayEquals(expected3, test_slot.getTopCards());
	}
}
