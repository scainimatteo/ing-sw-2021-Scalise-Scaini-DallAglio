package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;
import java.util.Arrays;

public class WarehouseTest {
	/**
	 * Test insert in a full warehouse
	 */
	@Test
	public void fullWarehouseTest(){
		Warehouse test_warehouse = new Warehouse();

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;

		ArrayList<Resource> input_resources_1 = new ArrayList<Resource>(Arrays.asList(coin_resource));
		ArrayList<Resource> input_resources_2 = new ArrayList<Resource>(Arrays.asList(stone_resource, stone_resource));
		ArrayList<Resource> input_resources_3 = new ArrayList<Resource>(Arrays.asList(shield_resource, shield_resource, shield_resource));
		test_warehouse.storeTop(input_resources_1);
		test_warehouse.storeMiddle(input_resources_2);
		test_warehouse.storeBottom(input_resources_3);

		ArrayList<Resource> expected_top = new ArrayList<Resource>(Arrays.asList(coin_resource));
		ArrayList<Resource> expected_middle = new ArrayList<Resource>(Arrays.asList(stone_resource, stone_resource));
		ArrayList<Resource> expected_bottom = new ArrayList<Resource>(Arrays.asList(shield_resource, shield_resource, shield_resource));

		assertEquals(expected_top, test_warehouse.getTopResource());
		assertEquals(expected_middle, test_warehouse.getMiddleResources());
		assertEquals(expected_bottom, test_warehouse.getBottomResources());
	}

	/**
	 * Test for the method getFromWarehouse
	 *
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
	 * Test requesting not contained resource
	 *
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

		assertThrows(IllegalArgumentException.class, ()->{ Resource[] test_return_getfromwarehouse = test_warehouse.getFromWarehouse(servant_resource, 1); });
	}

	/**
	 * Test requesting greater quantity of resources
	 *
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

		assertThrows(IndexOutOfBoundsException.class, ()->{ Resource[] test_return_getfromwarehouse = test_warehouse.getFromWarehouse(coin_resource, 2); });
	}
	*/
}
