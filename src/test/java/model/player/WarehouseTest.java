package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resources.Resource;

import java.io.*;

public class WarehouseTest {
	/**
	 * first test
	 */
	@Test
	public void firstWarehouseTest () {
		Warehouse test_warehouse = new Warehouse();

		// System.out.println(test_warehouse.toString());
		
		Resource resource1 = Resource.COIN;
		Resource resource2 = Resource.STONE;
		Resource resource3 = Resource.SHIELD;
		Resource resource4 = Resource.STONE;
		Resource resource5 = Resource.STONE;
		Resource resource6 = Resource.SERVANT;
		Resource[] test_resources1 = {resource1, resource2, resource3, resource4, resource5, resource6};

		test_warehouse.tryToInsert(test_resources1);

		System.out.println(test_warehouse.toString());
	
		assertTrue(true);
	}
}
