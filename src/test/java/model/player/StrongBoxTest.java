package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.resources.Resource;

import java.util.HashMap;

public class StrongBoxTest {
	/**
	 * first test for the class StrongBox
	 */
	@Test
	public void firstStrongBoxTest(){
		StrongBox test_sb = new StrongBox();

		Resource test_coin = Resource.COIN;
		Resource test_stone = Resource.STONE;

		Resource[] new_resources = {test_coin, test_coin};
		test_sb.insertResources(new_resources);

		HashMap<Resource, Integer> test_hm = test_sb.getStrongBox();

		assertEquals(test_hm.get(test_coin), 2);
		assertEquals(test_hm.get(test_stone), 0);
	}
}
