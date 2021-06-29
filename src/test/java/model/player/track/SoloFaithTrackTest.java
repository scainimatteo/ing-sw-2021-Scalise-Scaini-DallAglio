package it.polimi.ingsw.model.player.track;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.game.Factory;

import org.json.simple.parser.ParseException;
import java.io.IOException;

public class SoloFaithTrackTest {
	/**
	 * Test the creation and moveForwardBlackMarker
	 */
	@Test
	public void creationTest() throws ParseException, IOException{
		Factory gen_factory = Factory.getInstance();

		Cell[] track = gen_factory.getAllCells();
		
		Tile[] vatican_report_tiles = gen_factory.getAllTiles();

		SoloFaithTrack faith_track = new SoloFaithTrack(track, vatican_report_tiles);

		VaticanReports res_ret = faith_track.moveForwardBlackMarker(3);
		assertEquals(3, faith_track.getBlackMarkerPosition());
		assertEquals(res_ret, null);
	}
	
	/**
	 * Test the activation of the vatican report in two cases:
	 * - moving further than the popeSpace cell
	 * - moving on the popeSpace cell
	 */
	@Test
	public void activateVaticanReportTest() throws ParseException, IOException{
		Factory gen_factory = Factory.getInstance();

		Cell[] track = gen_factory.getAllCells();

		Tile[] vatican_report_tiles = gen_factory.getAllTiles();

		SoloFaithTrack faith_track = new SoloFaithTrack(track, vatican_report_tiles);

		VaticanReports res_ret = null;
		res_ret = faith_track.moveForwardBlackMarker(9);
		assertEquals(res_ret, VaticanReports.REPORT1);
		assertEquals(9, faith_track.getBlackMarkerPosition());

		res_ret = faith_track.moveForwardBlackMarker(7);
		assertEquals(16, faith_track.getBlackMarkerPosition());
		assertEquals(0, faith_track.getMarkerPosition());
		assertEquals(res_ret, VaticanReports.REPORT2);
	}
}
