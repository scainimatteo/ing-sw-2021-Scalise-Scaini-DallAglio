package it.polimi.ingsw.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.ChooseResourcesMessage;
import it.polimi.ingsw.controller.message.DiscardLeaderMessage;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.StubGameController;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.server.ClientHandler;

public class GameControllerTest {
	StubGameController controller;
	Game game;
	Message message;
	ArrayList<Resource> test_resources;
	Storage test_storage;
	Player player1;
	Player player2;
	Player player3;
	Player player4;

	private void initGame(){
		controller =  new StubGameController(null);
		controller.initializeGame();
		game = controller.getGame();
		test_resources = new ArrayList<Resource>();
		test_storage = new Storage();
		player1 = game.getPlayers().get(0);
		player2 = game.getPlayers().get(1);
		player3 = game.getPlayers().get(2);
		player4 = game.getPlayers().get(3);
	}
	
	@Test
	public void testFirstPlayerInitResources(){
		initGame();
		test_resources.add(Resource.STONE);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player1);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You already have the correct amount of starting resources");
		controller.clearError();
	}

	@Test
	public void testSecondPlayerInitResources(){
		initGame();
		test_resources.add(Resource.STONE);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player2);
		controller.handleMessage(message);
		assertEquals(player2.getTopResource().get(0), Resource.STONE);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You already have the correct amount of starting resources");
		controller.clearError();
	}

	@Test
	public void testThirdPlayerInitResources(){
		initGame();
		test_resources.add(Resource.SHIELD);
		test_resources.add(Resource.SHIELD);
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player3);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot choose so many starting resources");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.SHIELD);
		test_storage = new Storage();
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player3);
		controller.handleMessage(message);
		assertEquals(player3.getMiddleResources().get(0), Resource.SHIELD);
	}

	@Test
	public void testFourthPlayerInitResources(){
		initGame();
		test_resources.add(Resource.SHIELD);
		test_resources.add(Resource.SHIELD);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player4);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot store resources in such a way");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.COIN);
		test_resources.add(Resource.COIN);
		test_resources.add(Resource.COIN);
		test_storage = new Storage();
		test_storage.addToWarehouseBot(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player4);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot choose so many starting resources");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.COIN);
		test_storage = new Storage();
		test_storage.addToWarehouseMid(test_resources);
		test_storage.addToWarehouseBot(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player4);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot store resources in such a way");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.COIN);
		test_resources.add(Resource.COIN);
		test_storage = new Storage();
		test_storage.addToWarehouseBot(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player4);
		controller.handleMessage(message);
		assertEquals(player4.getBottomResources().get(0), Resource.COIN);
		assertEquals(player4.getBottomResources().get(1), Resource.COIN);
	}

	private void distributeResources(){
		initGame();
		test_resources.add(Resource.STONE);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player2);
		controller.handleMessage(message);
		test_resources.clear();

		test_resources.add(Resource.SHIELD);
		test_storage = new Storage();
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player3);
		controller.handleMessage(message);

		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player4);
		controller.handleMessage(message);
	}

	@Test
	public void testInitDiscard(){
		distributeResources();
		message = new DiscardLeaderMessage(player1.getLeaderCards().get(3));
		message.setPlayer(player1);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player1.getLeaderCards().get(0));
		message.setPlayer(player1);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player1.getLeaderCards().get(0));
		message.setPlayer(player1);
		controller.handleMessage(message);
		assertEquals("You cannot discard any more cards", controller.getError());
		assertEquals(player1.getLeaderCards().size(), 2);
		controller.clearError();

		message = new DiscardLeaderMessage(player2.getLeaderCards().get(3));
		message.setPlayer(player2);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player2.getLeaderCards().get(0));
		message.setPlayer(player2);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player2.getLeaderCards().get(0));
		message.setPlayer(player2);
		controller.handleMessage(message);
		assertEquals("You cannot discard any more cards", controller.getError());
		assertEquals(player2.getLeaderCards().size(), 2);
		controller.clearError();

		message = new DiscardLeaderMessage(player3.getLeaderCards().get(3));
		message.setPlayer(player3);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player3.getLeaderCards().get(0));
		message.setPlayer(player3);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player3.getLeaderCards().get(0));
		message.setPlayer(player3);
		controller.handleMessage(message);
		assertEquals("You cannot discard any more cards", controller.getError());
		assertEquals(player1.getLeaderCards().size(), 2);
		controller.clearError();

		message = new DiscardLeaderMessage(player4.getLeaderCards().get(3));
		message.setPlayer(player4);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player4.getLeaderCards().get(0));
		message.setPlayer(player4);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player4.getLeaderCards().get(0));
		message.setPlayer(player4);
		controller.handleMessage(message);
		assertEquals("It is not your turn", controller.getError());
		assertEquals(player1.getLeaderCards().size(), 2);
		controller.clearError();
	}
	
	private void startGame(){
		distributeResources();
		message = new DiscardLeaderMessage(player1.getLeaderCards().get(3));
		message.setPlayer(player1);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player1.getLeaderCards().get(0));
		message.setPlayer(player1);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player2.getLeaderCards().get(3));
		message.setPlayer(player2);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player2.getLeaderCards().get(0));
		message.setPlayer(player2);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player3.getLeaderCards().get(3));
		message.setPlayer(player3);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player3.getLeaderCards().get(0));
		message.setPlayer(player3);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player4.getLeaderCards().get(3));
		message.setPlayer(player4);
		controller.handleMessage(message);
		message = new DiscardLeaderMessage(player4.getLeaderCards().get(0));
		message.setPlayer(player4);
		controller.handleMessage(message);
	}

	private int totalWarehouse(Player player){
		return player.getTopResource().size() + player.getMiddleResources().size() + player.getBottomResources().size();
	}

	@Test 
	public void testStartGame(){
		startGame();
		assertEquals(totalWarehouse(player1), 0);
		assertEquals(totalWarehouse(player2), 1);
		assertEquals(totalWarehouse(player3), 1);
		assertEquals(totalWarehouse(player4), 2);
		assertTrue(game.getTurn().isInitialized());
	}

}
