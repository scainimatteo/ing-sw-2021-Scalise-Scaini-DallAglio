package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.util.ArrayList;
import java.util.Arrays;
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

		this.player = new Player("scainimatteo", new FaithTrack(factory.getAllCells(), factory.getAllTiles()));
		this.dev_card = factory.getAllDevelopmentCards();

		ArrayList<Resource> wh_resources = new ArrayList<Resource>(Arrays.asList(Resource.SHIELD, Resource.SHIELD));
		this.player.storeMiddle(wh_resources);

		ArrayList<Resource> sb_resources = new ArrayList<Resource>(Arrays.asList(Resource.STONE, Resource.SERVANT, Resource.SERVANT));
		this.player.insertResources(sb_resources);
	}

	/**
	 * Test for the creation of the player
	 */
	@Test
	public void creationTest(){
		assertEquals("scainimatteo", this.player.getNickname());
	}
}
