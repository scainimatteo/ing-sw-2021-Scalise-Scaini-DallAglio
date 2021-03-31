package it.polimi.ingsw.model.game.sologame;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.HashMap;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.Player;

public class SoloGameTest {
	SoloGame game;

	/**
	 * Add a SoloActionToken to a HashMap while updating its index
	 *
	 * @param map HashMap containing the number of times a SoloActionToken is found
	 * @param s String representing a SoloActionToken to insert to the HashMap
	 */
	private void addToMap(HashMap<String, Integer> map, String s) {
		if (map.containsKey(s)) {
			map.put(s, map.get(s) + 1);
		} else {
			map.put(s, 1);
		}
	}

	/**
	 * Create an istance of SoloGame
	 */
	@BeforeEach
	public void createSoloGame() {
		Player[] players = {new Player()};
		try {
			this.game = new SoloGame(players, Factory.getIstance().getAllDevelopmentCards());
		} catch (ParseException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Check that there is only one active player
	 */
	@Test
	public void checkOnlyOnePlayer() {
		assertEquals(1, this.game.getPlayers().length);
	}

	/**
	 * Check if the right amount of tokens are present and that trying to take more than 7 throws a NoSuchElementException
	 */
	@Test
	public void checkAllTokens() {
		HashMap<String, Integer> number_of_tokens = new HashMap<String, Integer>();

		for (int i = 0; i < 7; i++) {
			addToMap(number_of_tokens, this.game.getTopToken().getClass().getName());
		}

		//TODO: non mi piace
		assertEquals(2, number_of_tokens.get("it.polimi.ingsw.model.game.sologame.MoveBlackCrossTwoSpaces"));
		assertEquals(1, number_of_tokens.get("it.polimi.ingsw.model.game.sologame.MoveBlackCrossOneSpace"));
		assertEquals(4, number_of_tokens.get("it.polimi.ingsw.model.game.sologame.DiscardDevelopmentCards"));

		assertThrows(NoSuchElementException.class, () -> this.game.getTopToken());
	}

	/**
	 * Check if two consecutive new array of tokens are different from each other
	 */
	@RepeatedTest(value = 3)
	public void shuffleSoloActionTokensTest() {
		SoloActionToken[] initial_configuration = new SoloActionToken[7];
		SoloActionToken[] final_configuration = new SoloActionToken[7];

		for (int i = 0; i < 7; i++) {
			initial_configuration[i] = this.game.getTopToken();
		}

		this.game.shuffleSoloActionTokens();

		for (int i = 0; i < 7; i++) {
			final_configuration[i] = this.game.getTopToken();
		}

		assertFalse(initial_configuration.equals(final_configuration));
	}
}
