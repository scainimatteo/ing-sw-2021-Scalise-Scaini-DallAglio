package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.resources.Resource;

import java.util.HashMap;

public class StrongBoxTest {
	/**
	 * test for the method insertResources
	 */
	@Test
	public void insertInStrongBoxTest(){
		StrongBox test_sb = new StrongBox();

		Resource test_coin = Resource.COIN;
		Resource test_stone = Resource.STONE;

		Resource[] new_resources = {test_coin, test_coin};
		test_sb.insertResources(new_resources);

		HashMap<Resource, Integer> test_hm = test_sb.getStrongBox();

		assertEquals(test_hm.get(test_coin), 2);
		assertEquals(test_hm.get(test_stone), 0);
	}

	/**
	 * test for the method removeResources
	 */
	@Test
	public void removeResourceTest(){
		StrongBox test_sb = new StrongBox();

		Resource test_coin = Resource.COIN;
		Resource test_stone = Resource.STONE;

		Resource[] new_resources_1 = {test_coin, test_coin};
		Resource[] new_resources_2 = {test_stone, test_stone, test_stone};
		test_sb.insertResources(new_resources_1);
		test_sb.insertResources(new_resources_2);
		
		Resource[] test_return = test_sb.removeResources(test_coin, 2);
		assertArrayEquals(test_return, new_resources_1);
	}

	/**
	 * test for requesting not contained resource
	 */
	@Test
	public void removeNotContainedResourceTest(){
		StrongBox test_sb = new StrongBox();

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;
		Resource faith_resource = Resource.FAITH;

		Resource[] new_resources_1 = {coin_resource, coin_resource};
		Resource[] new_resources_2 = {stone_resource, stone_resource, stone_resource, stone_resource};
		test_sb.insertResources(new_resources_1);
		test_sb.insertResources(new_resources_2);
		
		assertThrows(IllegalArgumentException.class, () -> {Resource[] test_return = test_sb.removeResources(faith_resource, 2);});
	}

	/**
	 * test for requesting not contained resource
	 */
	@Test
	public void removeGreaterQuantityTest(){
		StrongBox test_sb = new StrongBox();

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] new_resources_1 = {coin_resource, coin_resource};
		Resource[] new_resources_2 = {stone_resource, stone_resource, stone_resource, stone_resource};
		test_sb.insertResources(new_resources_1);
		test_sb.insertResources(new_resources_2);
		
		assertThrows(IndexOutOfBoundsException.class, () -> {Resource[] test_return = test_sb.removeResources(coin_resource, 4);});
	}
}
