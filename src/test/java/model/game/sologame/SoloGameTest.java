package it.polimi.ingsw.model.game.sologame;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.HashMap;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

public class SoloGameTest {
	SoloGame game;

	/**
	 * Add a SoloActionToken to a HashMap while updating its index
	 *
	 * @param map HashMap containing the number of times a SoloActionToken is found
	 * @param s SoloActionToken to insert to the HashMap
	 */
	private void addToMap(HashMap<SoloActionToken, Integer> map, SoloActionToken s) {
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
		this.game = new SoloGame();
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
		HashMap<SoloActionToken, Integer> number_of_tokens = new HashMap<SoloActionToken, Integer>();

		for (int i = 0; i < 7; i++) {
			addToMap(number_of_tokens, this.game.getTopToken());
		}

		assertEquals(2, number_of_tokens.get(SoloActionToken.MOVE_BLACK_CROSS_TWO_SPACES));
		assertEquals(1, number_of_tokens.get(SoloActionToken.MOVE_BLACK_CROSS_ONE_SPACE));
		assertEquals(1, number_of_tokens.get(SoloActionToken.DISCARD_YELLOW_DEVELOPMENT_CARD));
		assertEquals(1, number_of_tokens.get(SoloActionToken.DISCARD_GREEN_DEVELOPMENT_CARD));
		assertEquals(1, number_of_tokens.get(SoloActionToken.DISCARD_PURPLE_DEVELOPMENT_CARD));
		assertEquals(1, number_of_tokens.get(SoloActionToken.DISCARD_BLUE_DEVELOPMENT_CARD));

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
