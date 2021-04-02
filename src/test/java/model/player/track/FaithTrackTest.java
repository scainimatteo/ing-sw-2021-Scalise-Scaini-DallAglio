package it.polimi.ingsw.model.player.track;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.game.Factory;

import java.io.*;
public class FaithTrackTest {
	/**
	 * Test the creation and moveForward
	 */
	@Test
	public void creationTest(){
		Factory gen_factory = Factory.getIstance();

		Cell[] track = gen_factory.getAllCells();
		if(track == null){
			System.out.println("ao");
		}
		VaticanReports report1 = VaticanReports.REPORT1;
		VaticanReports report2 = VaticanReports.REPORT2;
		VaticanReports report3 = VaticanReports.REPORT3;
		Tile tile1 = new Tile(report1, 2);
		Tile tile2 = new Tile(report2, 3);
		Tile tile3 = new Tile(report3, 4);
		Tile[] vatican_report_tiles = {tile1, tile2, tile3};

		FaithTrack faith_track = new FaithTrack(track, vatican_report_tiles);

		faith_track.moveForward(3);
		assertEquals(3, faith_track.getMarkerPosition());
	}
	
}
