package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.resources.Resource;

public class WarehouseTest {
	/**
	 * test for the methods tryToInsert, rearrangeWarehouse and the getters
	 * rearranged 2 middle and 2 bottom inserting 1 middle
	 */
	@Test
	public void insertAndRearrangeTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] input_resources_1 = {coin_resource, stone_resource, shield_resource, shield_resource};
		test_return = test_warehouse.tryToInsert(input_resources_1);
		assertEquals(test_return, 0);

		Resource[] input_resources_2 = {stone_resource, stone_resource};
		test_return = test_warehouse.tryToInsert(input_resources_2);
		assertEquals(test_return, 0);

		Resource expected_top = coin_resource;
		Resource[] expected_middle = {shield_resource, shield_resource};
		Resource[] expected_bottom = {stone_resource, stone_resource, stone_resource};

		assertEquals(expected_top, test_warehouse.getTopResource());
		assertArrayEquals(expected_middle, test_warehouse.getMiddleResources());
		assertArrayEquals(expected_bottom, test_warehouse.getBottomResources());
	}

	/**
	 * test rearrange inserting 1 top in a warehouse containing 1 top and 1 middle
	 */
	@Test
	public void rearrangeTopAndMiddleTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] input_resources_1 = {coin_resource, stone_resource};
		test_return = test_warehouse.tryToInsert(input_resources_1);
		assertEquals(test_return, 0);

		Resource[] input_resources_2 = {coin_resource};
		test_return = test_warehouse.tryToInsert(input_resources_2);
		assertEquals(test_return, 0);

		Resource expected_top = null;
		Resource[] expected_middle = {stone_resource, null};
		Resource[] expected_bottom = {coin_resource, coin_resource, null};

		assertEquals(expected_top, test_warehouse.getTopResource());
		assertArrayEquals(expected_middle, test_warehouse.getMiddleResources());
		assertArrayEquals(expected_bottom, test_warehouse.getBottomResources());
	}

	/**
	 * test insert in a full warehouse
	 */
	@Test
	public void fullWarehouseTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] input_resources_1 = {coin_resource, stone_resource, stone_resource, shield_resource, shield_resource, shield_resource};
		test_return = test_warehouse.tryToInsert(input_resources_1);
		assertEquals(test_return, 0);

		Resource[] input_resources_2 = {stone_resource};
		test_return = test_warehouse.tryToInsert(input_resources_2);
		assertEquals(test_return, 1);

		Resource expected_top = coin_resource;
		Resource[] expected_middle = {stone_resource, stone_resource};
		Resource[] expected_bottom = {shield_resource, shield_resource, shield_resource};

		assertEquals(expected_top, test_warehouse.getTopResource());
		assertArrayEquals(expected_middle, test_warehouse.getMiddleResources());
		assertArrayEquals(expected_bottom, test_warehouse.getBottomResources());
	}

	/**
	 * test insertion of 4 different resources
	 */
	@Test
	public void allDifferentResourcesTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] input_resources_1 = {coin_resource, stone_resource, shield_resource, servant_resource};
		test_return = test_warehouse.tryToInsert(input_resources_1);
		assertEquals(test_return, 1);

		Resource expected_top = coin_resource;
		Resource[] expected_middle = {stone_resource, null};
		Resource[] expected_bottom = {shield_resource, null, null};

		assertEquals(expected_top, test_warehouse.getTopResource());
		assertArrayEquals(expected_middle, test_warehouse.getMiddleResources());
		assertArrayEquals(expected_bottom, test_warehouse.getBottomResources());
	}

	/**
	 * test for the method getFromWarehouse
	 */
	@Test
	public void getFromWarehouseTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] test_resources1 = {coin_resource, stone_resource, shield_resource, stone_resource, stone_resource};

		test_return = test_warehouse.tryToInsert(test_resources1);
		assertEquals(test_return, 0);

		Resource[] test_return_getfromwarehouse = test_warehouse.getFromWarehouse(coin_resource, 1);
		Resource[] test_resource_top = {coin_resource};
		assertEquals(test_return_getfromwarehouse[0].name(), test_resource_top[0].name());
		assertArrayEquals(test_return_getfromwarehouse, test_resource_top);

		test_return_getfromwarehouse = test_warehouse.getFromWarehouse(shield_resource, 1);
		Resource[] test_resource_mid = {shield_resource};
		assertArrayEquals(test_return_getfromwarehouse, test_resource_mid);
	}

	/**
	 * test requesting not contained resource
	 */
	@Test
	public void notContainedResourceTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] test_resources1 = {coin_resource, stone_resource, shield_resource, stone_resource, stone_resource};

		test_return = test_warehouse.tryToInsert(test_resources1);
		assertEquals(test_return, 0);

		assertThrows(IllegalArgumentException.class, ()->{Resource[] test_return_getfromwarehouse = test_warehouse.getFromWarehouse(servant_resource, 1);});
	}

	/**
	 * test requesting greater quantity of resources
	 */
	@Test
	public void greaterQuantityTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		Resource[] test_resources1 = {coin_resource, stone_resource, shield_resource, stone_resource, stone_resource};

		test_return = test_warehouse.tryToInsert(test_resources1);
		assertEquals(test_return, 0);

		assertThrows(IndexOutOfBoundsException.class, ()->{Resource[] test_return_getfromwarehouse = test_warehouse.getFromWarehouse(coin_resource, 2);});
	}
}
