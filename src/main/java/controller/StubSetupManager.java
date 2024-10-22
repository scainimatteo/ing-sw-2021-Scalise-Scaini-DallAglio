package it.polimi.ingsw.controller;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

import java.lang.IndexOutOfBoundsException;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.controller.RemoteView;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.sologame.SoloGame;
import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.track.SoloFaithTrack;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.controller.SetupManager;


public class StubSetupManager extends SetupManager {
	public Game setupGame(int numofplayers) throws InstantiationException {
		try {
			createPlayers(numofplayers);
			distributeLeaderCards();
			chooseMatchOrder();
			movePlayerForward();
			Game game = createGame();
			return game;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InstantiationException();
		}
	}

	public SoloGame setupSoloGame() throws InstantiationException {
		try {
			createSoloPlayer();
			distributeLeaderCards();
			SoloGame game = createSoloGame();
			return game;
		} catch (Exception e) {
			e.printStackTrace();
			throw new InstantiationException();
		}
	}

	private void createPlayers(int numofplayers) throws ParseException, IOException {
		Factory factory = Factory.getInstance();
		Cell[] all_cells = factory.getAllCells();
		this.players = new ArrayList<Player>();
		for (int i = 1; i <= numofplayers; i++) {
			Tile[] all_tiles = factory.getAllTiles();
			players.add(new Player("player" + String.valueOf(i), new FaithTrack(all_cells, all_tiles)));
		}
	}

	private void createSoloPlayer() throws ParseException, IOException {
		Factory factory = Factory.getInstance();
		Cell[] all_cells = factory.getAllCells();
		Tile[] all_tiles = factory.getAllTiles();
		this.players = new ArrayList<Player>();
		players.add(new SoloPlayer("SoloPlayer", new SoloFaithTrack(all_cells, all_tiles)));
	}
}
