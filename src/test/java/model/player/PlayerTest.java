package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.HashMap;

import org.json.simple.parser.ParseException;
import java.io.IOException;

public class PlayerTest {
	Player player;
	DevelopmentCard[] dev_card;

	/**
	 * wh: 2 SHIELD
	 * sb: 1 STONE, 2 SERVANT
	 */
	@BeforeEach
	public void createPlayer() throws ParseException, IOException{
		Factory factory = Factory.getIstance();

		this.player = new Player("scainimatteo", factory.getAllCells(), factory.getAllTiles());
		this.dev_card = factory.getAllDevelopmentCards();

		Resource[] wh_resources = {Resource.SHIELD, Resource.SHIELD};
		this.player.tryToInsert(wh_resources);

		Resource[] sb_resources = {Resource.STONE, Resource.SERVANT, Resource.SERVANT};
		this.player.insertResources(sb_resources);
	}

	/**
	 * Test for the creation of the player
	 */
	@Test
	public void creationTest(){
		assertEquals("scainimatteo", this.player.getNickname());
	}

	/**
	 * Test the method isBuyable
	 * ID: 1 | cost: 2 shield | level: 1
	 */
	@Test
	public void resourcesInWarehouseTest(){
		boolean[] returned = this.player.isBuyable(dev_card[0]);
		assertTrue(returned[0] || returned[1] || returned[2]);
	}

	/**
	 * Test the method isBuyable
	 * ID: 2 | cost: 2 servant | level: 1
	 */
	@Test
	public void resourcesInStrongboxTest(){
		boolean[] returned = this.player.isBuyable(dev_card[1]);
		assertTrue(returned[0] || returned[1] || returned[2]);
	}

	/**
	 * Test the method isBuyable
	 * ID: 5 | cost: 1 shield, 1 servant, 1 stone | level: 1
	 */
	@Test
	public void resourcesInBothTest(){
		boolean[] returned = this.player.isBuyable(dev_card[4]);
		assertTrue(returned[0] || returned[1] || returned[2]);
	}

	/**
	 * Test the method isBuyable
	 * ID: 11 | cost: 3 coin | level: 1
	 */
	@Test
	public void resourcesNotPresentTest(){
		boolean[] returned = this.player.isBuyable(dev_card[10]);
		assertFalse(returned[0] && returned[1] && returned[2]);
	}

	/**
	 * Test the method buyCard() with warehouse_first = true
	 * ID: 2 | cost: 2 servant level: 1 | level: 1
	 */
	@Test
	public void buyCardTest(){
		this.player.buyCard(dev_card[1], 0, true);

		DevelopmentCard[] expected = {dev_card[1], null, null};
		assertArrayEquals(expected, this.player.getTopCards());
		
		HashMap<Resource, Integer> stored_res = this.player.getStrongBox();
		assertEquals(0, stored_res.get(Resource.SERVANT));
		assertEquals(1, stored_res.get(Resource.STONE));
	}

	/**
	 * Test the insertion of a card level 1 on a card level 1
	 * ID: 2 | cost: 2 servant | level: 1
	 * ID: 5 | cost: 1 shield, 1 servant, 1 stone | level: 1
	 */
	@Test
	public void cardWithSameLevelTest(){
		this.player.buyCard(dev_card[1], 0, true);

		DevelopmentCard[] expected1 = {dev_card[1], null, null};
		assertArrayEquals(expected1, this.player.getTopCards());

		Resource[] sb_resources = {Resource.SERVANT};
		this.player.insertResources(sb_resources);

		assertThrows(IllegalArgumentException.class, ()->{ this.player.buyCard(dev_card[4], 0, true); });

		DevelopmentCard[] expected2 = {dev_card[1], null, null};
		assertArrayEquals(expected2, this.player.getTopCards());
	}

	/**
	 * Test the insertion of a card level 2 on a null space
	 * ID: 20 | cost: 4 stone | level: 2
	 */
	@Test
	public void cardLevel2Test(){
		Resource[] wh_resources = {Resource.STONE};
		this.player.tryToInsert(wh_resources);

		Resource[] sb_resources = {Resource.STONE, Resource.STONE};
		this.player.insertResources(sb_resources);

		assertThrows(IllegalArgumentException.class, ()->{ this.player.buyCard(dev_card[19], 0, true); });

		DevelopmentCard[] expected = {null, null, null};
		assertArrayEquals(expected, this.player.getTopCards());
	}

	/**
	 * Test the insertion of a card level 3 on a card level 1
	 * ID: 2 | cost: 2 servant | level: 1
	 * ID: 35 | cost: 6 coin | level: 3
	 */
	@Test
	public void greaterLevelTest(){
		this.player.buyCard(dev_card[1], 0, true);

		DevelopmentCard[] expected = {dev_card[1], null, null};
		assertArrayEquals(expected, this.player.getTopCards());

		Resource[] sb_resources = {Resource.COIN, Resource.COIN, Resource.COIN, Resource.COIN, Resource.COIN, Resource.COIN};
		this.player.insertResources(sb_resources);

		assertThrows(IllegalArgumentException.class, ()->{ this.player.buyCard(dev_card[29], 0, true) ;});
	}
}
