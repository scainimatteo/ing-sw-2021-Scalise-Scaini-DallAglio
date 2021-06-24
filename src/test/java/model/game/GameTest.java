package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.player.track.FaithTrack;
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

		Factory factory = Factory.getIstance();
		ArrayList<Player> players = new ArrayList<Player>();
		for (int i = 0; i < this.number_of_players; i++) {
			players.add(new Player(Integer.toString(i), new FaithTrack(factory.getAllCells(), factory.getAllTiles())));
		}

		this.game = new Game(players, factory.getAllDevelopmentCards());
	}
}
