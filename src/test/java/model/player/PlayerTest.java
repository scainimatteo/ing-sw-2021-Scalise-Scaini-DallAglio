package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.DevelopmentCard;

import org.json.simple.parser.ParseException;
import java.io.IOException;

public class PlayerTest {
	Player player;
	DevelopmentCard[] dev_card;

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
	 * ID: 1 - cost: 2 shield
	 */
	@Test
	public void resourcesInWarehouseTest(){
		boolean[] returned = this.player.isBuyable(dev_card[0]);
		assertTrue(returned[0] || returned[1] || returned[2]);
	}

	/**
	 * Test the method isBuyable
	 * ID: 2 - cost: 2 servant
	 */
	@Test
	public void resourcesInStrongboxTest(){
		boolean[] returned = this.player.isBuyable(dev_card[1]);
		assertTrue(returned[0] || returned[1] || returned[2]);
	}

	/**
	 * Test the method isBuyable
	 * ID: 5 - cost: 1 shield, 1 servant, 1 stone
	 */
	@Test
	public void resourcesInBothTest(){
		boolean[] returned = this.player.isBuyable(dev_card[4]);
		assertTrue(returned[0] || returned[1] || returned[2]);
	}

	/**
	 * Test the method isBuyable
	 * ID: 11 - cost: 3 coin
	 */
	@Test
	public void resourcesNotPresentTest(){
		boolean[] returned = this.player.isBuyable(dev_card[10]);
		assertFalse(returned[0] && returned[1] && returned[2]);
	}

	/**
	 * Test the method buyCard() with warehouse_first = true
	 * ID: 2 - cost: 2 servant level: 1
	 */
	@Test
	public void buyCardTest(){
		this.player.buyCard(dev_card[1], 0, true);

		DevelopmentCard[] expected = {dev_card[1], null, null};
		assertArrayEquals(expected, this.player.getTopCards());
	}
}
