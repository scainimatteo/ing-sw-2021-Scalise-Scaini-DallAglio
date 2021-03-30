package it.polimi.ingsw.model.player.track;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.VaticanReports;

public class CellTest {
	/**
	 * First test for the class Cell
	 */
	@Test
	public void fristCellTest(){
		VaticanReports test_vat_rep = VaticanReports.REPORT1;

		Cell test_cell = new Cell(8, 3, test_vat_rep, true, false);

		assertTrue(test_cell.isPopeSpace());
	}
}
