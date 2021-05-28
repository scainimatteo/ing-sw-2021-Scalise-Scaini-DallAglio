package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;
import java.util.HashMap;

public class StrongBoxTest {
	/**
	 * Test for the method insertResources
	 */
	@Test
	public void insertInStrongBoxTest(){
		StrongBox test_sb = new StrongBox();

		Resource test_coin = Resource.COIN;
		Resource test_stone = Resource.STONE;

		ArrayList<Resource> new_resources = new ArrayList<Resource>();
		new_resources.add(test_coin);
		new_resources.add(test_coin);
		test_sb.insertResources(new_resources);

		HashMap<Resource, Integer> test_hm = test_sb.getStorage();

		assertEquals(test_hm.get(test_coin), 2);
		assertEquals(test_hm.get(test_stone), 0);
	}

	/**
	 * Test for the method removeResources
	 */
	@Test
	public void removeResourceTest(){
		StrongBox test_sb = new StrongBox();

		Resource test_coin = Resource.COIN;
		Resource test_stone = Resource.STONE;

		ArrayList<Resource> new_resources_1 = new ArrayList<Resource>();
		new_resources_1.add(test_coin);
		new_resources_1.add(test_coin);

		ArrayList<Resource> new_resources_2 = new ArrayList<Resource>();
		new_resources_2.add(test_stone);
		new_resources_2.add(test_stone);
		new_resources_2.add(test_stone);

		test_sb.insertResources(new_resources_1);
		test_sb.insertResources(new_resources_2);
		
		test_sb.removeResources(new_resources_1);

		assertEquals(test_sb.getStorage().get(test_coin), 0);
	}
}
