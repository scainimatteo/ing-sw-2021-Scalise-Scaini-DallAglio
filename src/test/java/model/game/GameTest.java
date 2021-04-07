package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.Player;

public class GameTest {
	Game game;
	int number_of_players;

	/**
	 * Create an istance of Game
	 */
	@BeforeEach
	public void createGame() throws IOException, ParseException {
		Random random = new Random();
		this.number_of_players = random.nextInt(4) + 1;

		Player[] players = new Player[this.number_of_players];
		for (int i = 0; i < this.number_of_players; i++) {
			players[i] = new Player();
		}

		this.game = new Game(players, Factory.getIstance().getAllDevelopmentCards());
	}

	/**
	 * Check that all players correctly shifted
	 */
	@RepeatedTest(value = 3)
	public void shiftPlayersTest() {
		Player[] before = this.game.getPlayers().clone();
		this.game.shiftPlayers();
		Player[] after = this.game.getPlayers();

		assertEquals(before[this.number_of_players - 1], after[0]);
		for (int i = 1; i < this.number_of_players; i++) {
			assertEquals(before[i], after[i - 1]);
		}
	}
}
