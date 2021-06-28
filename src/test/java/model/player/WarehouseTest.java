package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;
import java.util.Arrays;

public class WarehouseTest {
	Warehouse test_warehouse;
	
	@BeforeEach
	public void init(){
		test_warehouse = new Warehouse();
	}

	@Test
	public void testSwapRows(){
		ArrayList<Resource> bot_res = new ArrayList<Resource>();
		ArrayList<Resource> mid_res = new ArrayList<Resource>();
		ArrayList<Resource> top_res = new ArrayList<Resource>();
		bot_res.add(Resource.STONE);
		mid_res.add(Resource.COIN);
		top_res.add(Resource.SHIELD);
		test_warehouse.storeBottom(bot_res);
		test_warehouse.storeMiddle(mid_res);
		test_warehouse.storeTop(top_res);
		assertThrows(IllegalArgumentException.class, () -> {test_warehouse.swapRows(0, 2);});
		assertThrows(IllegalArgumentException.class, () -> {test_warehouse.swapRows(1, 4);});
		
		test_warehouse.swapRows(1, 2);
		assertEquals(test_warehouse.getTopResource(), mid_res);
		assertEquals(test_warehouse.getMiddleResources(), top_res);
		test_warehouse.storeMiddle(top_res);
		assertThrows(IllegalArgumentException.class, () -> {test_warehouse.swapRows(1, 2);});

		test_warehouse.swapRows(1, 3);
		assertEquals(test_warehouse.getTopResource(), bot_res);
		assertEquals(test_warehouse.getBottomResources(), mid_res);
		test_warehouse.storeBottom(mid_res);
		assertThrows(IllegalArgumentException.class, () -> {test_warehouse.swapRows(1, 3);});

		top_res.add(Resource.SHIELD);
		mid_res.add(Resource.COIN);
		test_warehouse.swapRows(2, 3);
		assertEquals(test_warehouse.getMiddleResources(), mid_res);
		assertEquals(test_warehouse.getBottomResources(), top_res);
		top_res.clear();
		top_res.add(Resource.SHIELD);
		test_warehouse.storeBottom(top_res);
		assertThrows(IllegalArgumentException.class, () -> {test_warehouse.swapRows(2, 3);});
	}

	/**
	 * Tests the methods to check whether the given resources can be stored in the top shelf and to store them
	 */
	private void topWarehouseStoreTest(){
		ArrayList<Resource> top_res = new ArrayList<Resource>();
		assertTrue(test_warehouse.canBeStoredTop(top_res));

		top_res.add(Resource.STONE);
		assertTrue(test_warehouse.canBeStoredTop(top_res));
		test_warehouse.storeTop(top_res);
		assertEquals(test_warehouse.getTopResource(), top_res);
		assertFalse(test_warehouse.canBeStoredTop(top_res));

		top_res.remove(Resource.STONE);
		top_res.add(Resource.FAITH);
		assertFalse(test_warehouse.canBeStoredTop(top_res));
	}

	/**
	 * Tests the methods to check whether the given resources can be stored in the middle shelf and to store them
	 */
	private void middleWarehouseStoreTest(){
		ArrayList<Resource> mid_res = new ArrayList<Resource>();
		assertTrue(test_warehouse.canBeStoredMiddle(mid_res));

		mid_res.add(Resource.STONE);
		assertFalse(test_warehouse.canBeStoredMiddle(mid_res));
		mid_res.remove(Resource.STONE);

		mid_res.add(Resource.SHIELD);
		assertTrue(test_warehouse.canBeStoredMiddle(mid_res));
		test_warehouse.storeMiddle(mid_res);
		assertEquals(test_warehouse.getMiddleResources(), mid_res);
		mid_res.remove(Resource.SHIELD);

		mid_res.add(Resource.FAITH);
		assertFalse(test_warehouse.canBeStoredMiddle(mid_res));
		mid_res.remove(Resource.FAITH);

		mid_res.add(Resource.SHIELD);
		assertTrue(test_warehouse.canBeStoredMiddle(mid_res));
		test_warehouse.storeMiddle(mid_res);
		mid_res.add(Resource.SHIELD);
		assertEquals(test_warehouse.getMiddleResources(), mid_res);
		assertFalse(test_warehouse.canBeStoredMiddle(mid_res));
	}

	/**
	 * Tests the methods to check whether the given resources can be stored in the bottom shelf and to store them
	 */
	private void bottomWarehouseStoreTest(){
		ArrayList<Resource> bot_res = new ArrayList<Resource>();
		assertTrue(test_warehouse.canBeStoredBottom(bot_res));

		bot_res.add(Resource.SHIELD);
		assertFalse(test_warehouse.canBeStoredBottom(bot_res));
		bot_res.remove(Resource.SHIELD);

		bot_res.add(Resource.COIN);
		bot_res.add(Resource.SERVANT);
		assertFalse(test_warehouse.canBeStoredBottom(bot_res));
		bot_res.remove(Resource.SERVANT);
		assertTrue(test_warehouse.canBeStoredBottom(bot_res));
		test_warehouse.storeBottom(bot_res);
		assertEquals(test_warehouse.getBottomResources(), bot_res);
		
		bot_res.remove(Resource.COIN);
		bot_res.add(Resource.SERVANT);
		assertFalse(test_warehouse.canBeStoredBottom(bot_res));
		bot_res.remove(Resource.SERVANT);

		bot_res.add(Resource.COIN);
		bot_res.add(Resource.COIN);
		assertTrue(test_warehouse.canBeStoredBottom(bot_res));
		test_warehouse.storeBottom(bot_res);
		bot_res.add(Resource.COIN);
		assertEquals(test_warehouse.getBottomResources(), bot_res);
		assertFalse(test_warehouse.canBeStoredBottom(bot_res));
	}

	/**
	 * Runs consecutively all the shelf store tests plus tests clearWarehouse method
	 */
	@Test
	public void fullStoreTest(){
		topWarehouseStoreTest();
		middleWarehouseStoreTest();
		bottomWarehouseStoreTest();

		test_warehouse.clearWarehouse();
		assertTrue(test_warehouse.getTopResource().isEmpty());
		assertTrue(test_warehouse.getMiddleResources().isEmpty());
		assertTrue(test_warehouse.getBottomResources().isEmpty());
	}

	/**
	 * Test for the method getFromWarehouse
	 */
	@Test
	public void getFromWarehouseTest(){
		Warehouse test_warehouse = new Warehouse();
		int test_return = 0;

		Resource coin_resource = Resource.COIN;
		Resource stone_resource = Resource.STONE;
		Resource shield_resource = Resource.SHIELD;
		Resource servant_resource = Resource.SERVANT;
	}

	/**
	 * Tests the methods to check whether the given resources are contained in the top shelf and to get them
	 */
	private void topWarehouseGetTest(){
		ArrayList<Resource> top_res = new ArrayList<Resource>();
		assertTrue(test_warehouse.isContainedTop(top_res));

		top_res.add(Resource.STONE);
		assertFalse(test_warehouse.isContainedTop(top_res));

		test_warehouse.getTopResource().add(Resource.STONE);
		assertTrue(test_warehouse.isContainedTop(top_res));
		test_warehouse.getFromTop(top_res);
		assertTrue(test_warehouse.getTopResource().isEmpty());
		assertFalse(test_warehouse.isContainedTop(top_res));

		top_res.remove(Resource.STONE);
		top_res.add(Resource.FAITH);
		test_warehouse.getTopResource().add(Resource.STONE);
		assertFalse(test_warehouse.isContainedTop(top_res));
	}
	
	/**
	 * Tests the methods to check whether the given resources are contained in the top shelf and to get them
	 */
	private void middleWarehouseGetTest(){
		ArrayList<Resource> mid_res = new ArrayList<Resource>();
		assertTrue(test_warehouse.isContainedMiddle(mid_res));

		mid_res.add(Resource.SHIELD);
		assertFalse(test_warehouse.isContainedMiddle(mid_res));

		test_warehouse.getMiddleResources().add(Resource.SHIELD);
		assertTrue(test_warehouse.isContainedMiddle(mid_res));

		test_warehouse.getMiddleResources().add(Resource.SHIELD);
		test_warehouse.getFromMiddle(mid_res);
		assertEquals(test_warehouse.getMiddleResources(), mid_res);
		assertTrue(test_warehouse.isContainedMiddle(mid_res));
		
		mid_res.add(Resource.FAITH);
		assertFalse(test_warehouse.isContainedMiddle(mid_res));
		test_warehouse.getMiddleResources().add(Resource.SHIELD);
		assertFalse(test_warehouse.isContainedMiddle(mid_res));
	}

	/**
	 * Tests the methods to check whether the given resources are contained in the top shelf and to get them
	 */
	private void bottomWarehouseGetTest(){
		ArrayList<Resource> bot_res = new ArrayList<Resource>();
		assertTrue(test_warehouse.isContainedBottom(bot_res));

		bot_res.add(Resource.COIN);
		assertFalse(test_warehouse.isContainedBottom(bot_res));
		
		test_warehouse.getBottomResources().add(Resource.COIN);
		assertTrue(test_warehouse.isContainedBottom(bot_res));
		
		test_warehouse.getBottomResources().add(Resource.COIN);
		test_warehouse.getBottomResources().add(Resource.COIN);
		bot_res.add(Resource.COIN);
		assertTrue(test_warehouse.isContainedBottom(bot_res));
		
		bot_res.add(Resource.FAITH);
		assertFalse(test_warehouse.isContainedBottom(bot_res));
		bot_res.remove(Resource.FAITH);

		bot_res.add(Resource.COIN);
		bot_res.add(Resource.COIN);
		assertFalse(test_warehouse.isContainedBottom(bot_res));
		
		bot_res.remove(Resource.COIN);
		bot_res.remove(Resource.COIN);
		assertTrue(test_warehouse.isContainedBottom(bot_res));
		test_warehouse.getFromBottom(bot_res);
		bot_res.remove(Resource.COIN);
		assertEquals(test_warehouse.getBottomResources(), bot_res);

		assertTrue(test_warehouse.isContainedBottom(bot_res));
		test_warehouse.getFromBottom(bot_res);
		assertTrue(test_warehouse.getBottomResources().isEmpty());
	}

	/**
	 * Runs consecutively all the shelf get tests
	 * */
	@Test
	public void fullGetTest(){
		topWarehouseGetTest();
		test_warehouse.clearWarehouse();
		middleWarehouseGetTest();
		test_warehouse.clearWarehouse();
		bottomWarehouseGetTest();
		test_warehouse.clearWarehouse();
	}
}
