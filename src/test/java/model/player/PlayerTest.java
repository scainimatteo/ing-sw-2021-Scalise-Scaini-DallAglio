package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import org.json.simple.parser.ParseException;
import java.io.IOException;

public class PlayerTest {
	/**
	 * Test for the creation of the player
	 */
	@Test
	public void creationTest() throws ParseException, IOException{
		Factory factory = Factory.getIstance();

		Player player1 = new Player("scainimatteo", factory.getAllCells(), factory.getAllTiles());

		assertEquals("scainimatteo", player1.getNickname());
	}
}
