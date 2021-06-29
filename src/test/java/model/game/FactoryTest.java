package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

public class FactoryTest {
	private Factory factory;

	private int development_cards_number;
	private int leader_cards_number;
	private int cells_number;
	private int tiles_number;
	private int solo_action_tokens_number;

	/**
	 * Save the istance of Factory
	 */
	@BeforeEach
	public void createFactory() throws IOException, ParseException {
		this.factory = Factory.getIstance();
	}

	@Test
	/**
	 * Check if all the json files are parsed correctly
	 */
	public void jsonTest() {
		this.development_cards_number = this.factory.getDevelopmentCardsNumber();
		DevelopmentCard[] all_development_cards = this.factory.getAllDevelopmentCards();
		assertEquals(development_cards_number, all_development_cards.length);

		this.leader_cards_number = this.factory.getLeaderCardsNumber();
		LeaderCard[] all_leader_cards = this.factory.getAllLeaderCards();
		assertEquals(leader_cards_number, all_leader_cards.length);

		this.cells_number = this.factory.getCellsNumber();
		Cell[] all_cells = this.factory.getAllCells();
		assertEquals(cells_number, all_cells.length);

		this.tiles_number = this.factory.getTilesNumber();
		Tile[] all_tiles = this.factory.getAllTiles();
		assertEquals(tiles_number, all_tiles.length);

		this.solo_action_tokens_number = this.factory.getSoloActionTokensNumber();
		SoloActionToken[] all_solo_action_tokens = this.factory.getAllSoloActionTokens();
		assertEquals(solo_action_tokens_number, all_solo_action_tokens.length);
	}

	@Test
	/**
	 * Check that all DevelopmentCards are different
	 */
	public void allDifferentDevelopmentCards() {
		DevelopmentCard[] all_development_cards = this.factory.getAllDevelopmentCards();

		HashSet<DevelopmentCard> set = new HashSet<DevelopmentCard>();
		for (DevelopmentCard card : all_development_cards) {
			// if the element is already in the set set.add will return false
			assertTrue(set.add(card));
		}
	}

	@Test
	/**
	 * Check that all LeaderCards are different
	 */
	public void allDifferentLeaderCards() {
		LeaderCard[] all_leader_cards = this.factory.getAllLeaderCards();

		HashSet<LeaderCard> set = new HashSet<LeaderCard>();
		for (LeaderCard card : all_leader_cards) {
			// if the element is already in the set set.add will return false
			assertTrue(set.add(card));
		}
	}

	@Test
	/**
	 * Check that all Cells are different
	 */
	public void allDifferentCells() {
		Cell[] all_cells = this.factory.getAllCells();

		HashSet<Cell> set = new HashSet<Cell>();
		for (Cell cell : all_cells) {
			// if the element is already in the set set.add will return false
			assertTrue(set.add(cell));
		}
	}
	
	@Test
	/**
	 * Check that all Tiles are different
	 */
	public void allDifferentTiles() {
		Tile[] all_tiles = this.factory.getAllTiles();

		HashSet<Tile> set = new HashSet<Tile>();
		for (Tile tile : all_tiles) {
			// if the element is already in the set set.add will return false
			assertTrue(set.add(tile));
		}
	}

	@Test
	/**
	 * Check that all SoloActionTokens are different
	 */
	public void allDifferentSoloActionTokens() {
		SoloActionToken[] all_solo_action_tokens = this.factory.getAllSoloActionTokens();

		HashSet<SoloActionToken> set = new HashSet<SoloActionToken>();
		for (SoloActionToken solo_action_token : all_solo_action_tokens) {
			// if the element is already in the set set.add will return false
			assertTrue(set.add(solo_action_token));
		}
	}

	@Test
	/**
	 * Check that the LeaderCards arrays are cloned
	 */
	public void clonedLeaderCardsTest() {
		LeaderCard[] first = factory.getAllLeaderCards();
		LeaderCard[] second = factory.getAllLeaderCards();
		for (int i = 0; i < leader_cards_number; i++) {
			assertNotEquals(first[i], second[i]);
		}
	}

	@Test
	/**
	 * Check that the Tiles arrays are cloned
	 */
	public void clonedTilesTest() {
		Tile[] first = factory.getAllTiles();
		Tile[] second = factory.getAllTiles();
		for (int i = 0; i < tiles_number; i++) {
			assertNotEquals(first[i], second[i]);
		}
	}
}
