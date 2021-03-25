package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resources.Resource;

import java.io.*;

public class WarehouseTest {
	/**
	 * first test for the class Warehouse
	 */
	@Test
	public void firstWarehouseTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource resource1 = Resource.COIN;
		Resource resource2 = Resource.STONE;
		Resource resource3 = Resource.SHIELD;
		Resource resource4 = Resource.STONE;
		Resource resource5 = Resource.STONE;
		Resource resource6 = Resource.SHIELD;
		Resource[] test_resources1 = {resource1, resource2, resource3, resource4, resource5, resource6};

		test_return = test_warehouse.tryToInsert(test_resources1);

		Resource expected_top = resource1;
		Resource[] expected_middle = {resource3, resource6};
		Resource[] expected_bottom = {resource2, resource4, resource5};

		assertEquals(test_return, 0);
		assertEquals(expected_top, test_warehouse.getTopResource());
		assertArrayEquals(expected_middle, test_warehouse.getMiddleResources());
		assertArrayEquals(expected_bottom, test_warehouse.getBottomResources());
	}

	/**
	 * getFromWarehouse test
	 */
	@Test
	public void getFromWarehouseTest(){
		Warehouse test_warehouse = new Warehouse();

		Resource resource1 = Resource.COIN;
		Resource resource2 = Resource.STONE;
		Resource resource3 = Resource.SHIELD;
		Resource resource4 = Resource.STONE;
		Resource resource5 = Resource.STONE;
		Resource[] test_resources1 = {resource1, resource2, resource3, resource4, resource5};

		int test_return_trytoinsert = test_warehouse.tryToInsert(test_resources1);
		assertEquals(test_return_trytoinsert, 0);

		Resource[] test_return_getfromwarehouse = test_warehouse.getFromWarehouse(resource1, 1);
		Resource[] test_resource_top = {resource1};
		assertEquals(test_return_getfromwarehouse[0].name(), test_resource_top[0].name());

		test_return_getfromwarehouse = test_warehouse.getFromWarehouse(resource3, 1);
		Resource[] test_resource_mid = {resource3};
		assertEquals(test_return_getfromwarehouse[0].name(), test_resource_mid[0].name());
	}
}
