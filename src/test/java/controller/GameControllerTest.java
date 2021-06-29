package it.polimi.ingsw.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.ChooseResourcesMessage;
import it.polimi.ingsw.controller.message.DiscardLeaderMessage;
import it.polimi.ingsw.controller.message.ActivateLeaderMessage;
import it.polimi.ingsw.controller.message.MarketMessage;
import it.polimi.ingsw.controller.message.BuyCardMessage;
import it.polimi.ingsw.controller.message.ProductionMessage;
import it.polimi.ingsw.controller.message.DiscardResourcesMessage;
import it.polimi.ingsw.controller.message.PayMessage;
import it.polimi.ingsw.controller.message.StoreMessage;
import it.polimi.ingsw.controller.message.RearrangeMessage;
import it.polimi.ingsw.controller.message.EndTurnMessage;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.SetupManager;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.StubGameController;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderCardLevelCost;
import it.polimi.ingsw.model.card.LeaderCardResourcesCost;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.CardLevel;
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
	ArrayList <Player> playerOrder; 

	private void initGame(){
		controller =  new StubGameController(null);
		controller.setupGame();
		game = controller.getGame();
		test_resources = new ArrayList<Resource>();
		test_storage = new Storage();
		playerOrder = game.getPlayers();
	}
	
	/**
	 * Tests handleDiscardResources method from first player with full instruction coverage 
	 */
	@Test
	public void testFirstPlayerInitResources(){
		initGame();
		Player player =  playerOrder.get(0);
		test_resources.add(Resource.STONE);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You already have the correct amount of starting resources");
		controller.clearError();
	}

	/**
	 * Tests handleDiscardResources method from second player with full instruction coverage 
	 */
	@Test
	public void testSecondPlayerInitResources(){
		initGame();
		Player player =  playerOrder.get(1);
		test_resources.add(Resource.STONE);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(player.getTopResource().get(0), Resource.STONE);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You already have the correct amount of starting resources");
		controller.clearError();
	}

	/**
	 * Tests handleDiscardResources method from third player with full instruction coverage 
	 */
	@Test
	public void testThirdPlayerInitResources(){
		initGame();
		Player player =  playerOrder.get(2);
		test_resources.add(Resource.SHIELD);
		test_resources.add(Resource.SHIELD);
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot choose so many starting resources");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.SHIELD);
		test_storage = new Storage();
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(player.getMiddleResources().get(0), Resource.SHIELD);
	}

	/**
	 * Tests handleDiscardResources method from fourth player with full instruction coverage 
	 */
	@Test
	public void testFourthPlayerInitResources(){
		initGame();
		Player player =  playerOrder.get(3);
		test_resources.add(Resource.SHIELD);
		test_resources.add(Resource.SHIELD);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
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
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot choose so many starting resources");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.COIN);
		test_storage = new Storage();
		test_storage.addToWarehouseMid(test_resources);
		test_storage.addToWarehouseBot(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(controller.getError(),"You cannot store resources in such a way");
		controller.clearError();

		test_resources.clear();
		test_resources.add(Resource.COIN);
		test_resources.add(Resource.COIN);
		test_storage = new Storage();
		test_storage.addToWarehouseBot(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		assertEquals(player.getBottomResources().get(0), Resource.COIN);
		assertEquals(player.getBottomResources().get(1), Resource.COIN);
	}

	/**
	 * Properly distributes resources to each player
	 */
	private void distributeResources(){
		initGame();
		Player player =  playerOrder.get(1);
		test_resources.add(Resource.STONE);
		test_storage.addToWarehouseTop(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
		test_resources.clear();

		player =  playerOrder.get(2);
		test_resources.add(Resource.SHIELD);
		test_storage = new Storage();
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);

		player =  playerOrder.get(3);
		test_storage.addToWarehouseMid(test_resources);
		message = new ChooseResourcesMessage(test_storage);
		message.setPlayer(player);
		controller.handleMessage(message);
	}

	/**
	 * Tests handleDiscard method, with full instruction coverage, before the game can properly begin
	 */
	@Test
	public void testInitDiscard(){
		distributeResources();
		for (Player p : playerOrder){
			message = new DiscardLeaderMessage(p.getLeaderCards().get(3));
			message.setPlayer(p);
			controller.handleMessage(message);
			message = new DiscardLeaderMessage(p.getLeaderCards().get(0));
			message.setPlayer(p);
			controller.handleMessage(message);
			message = new DiscardLeaderMessage(p.getLeaderCards().get(0));
			message.setPlayer(p);
			controller.handleMessage(message);
			if(playerOrder.indexOf(p) == playerOrder.size() - 1){
				assertEquals("It is not your turn", controller.getError());
			} else {
				assertEquals("You cannot discard any more cards", controller.getError());
			}
			assertEquals(p.getLeaderCards().size(), 2);
			controller.clearError();
		}
	}
	
	/**
	 * @param player is the player whose warehouse will be checked
	 * @return the number of resources in the player's warehouse
	 */
	private int totalWarehouse(Player player){
		return player.getTopResource().size() + player.getMiddleResources().size() + player.getBottomResources().size();
	}

	/**
	 * Tests the correct outcome of the setup phase before the game can properly begin
	 */
	@Test 
	public void testStartGame(){
		testInitDiscard();
		assertEquals(totalWarehouse(playerOrder.get(0)), 0);
		assertEquals(totalWarehouse(playerOrder.get(1)), 1);
		assertEquals(totalWarehouse(playerOrder.get(2)), 1);
		assertEquals(totalWarehouse(playerOrder.get(3)), 2);
		for (Player p : playerOrder){
			assertEquals(p.getLeaderCards().size(), 2);
		}
		assertTrue(game.getTurn().hasDoneSetup());
	}

	/**
	 * Causes the active player to do an always legal action
	 */
	private void emptyAction(){
		Player player = game.getTurn().getPlayer();
		message = new MarketMessage(true, 1);
		message.setPlayer(player);
		controller.handleMessage(message);
		message = new DiscardResourcesMessage(); 
		message.setPlayer(player);
		controller.handleMessage(message);
	}

	/**
	 * Causes the active player to do an always legal action and ends their turn
	 */
	private void skipTurn(){
		Player player = game.getTurn().getPlayer();
		emptyAction();
		message = new EndTurnMessage();
		message.setPlayer(player);
		controller.handleMessage(message);
	}
	
	/**
	 * Tests handleEndTurn method for each player in a round, then tests the game to properly update for the following round 
	 */
	@RepeatedTest(value = 3)
	public void testFullRoundEndTurn(){
		testStartGame();
		ArrayList<Player> prevorder = (ArrayList<Player>) playerOrder.clone();
		for (Player p : playerOrder){
			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
			if (p.equals(game.getTurn().getPlayer())){
				assertEquals(controller.getError(), "You cannot end your turn now");
				controller.clearError();
			} else {
				assertEquals(controller.getError(), "It is not your turn");
				controller.clearError();
			}
		}
		int i = 0;
		for (Player p : playerOrder){
			assertEquals(playerOrder.indexOf(game.getTurn().getPlayer()), i);
			skipTurn();
			i++;
		}
		for (i = 0; i<4; i++){
			assertEquals(playerOrder.get(i), prevorder.get((i+3)%4));
		}

	}

	/**
	 * Tests handleDiscardLeader for each player at the start of their turn
	 * */
	@RepeatedTest(value = 5)
	public void testDiscardLeaderStart(){
		testStartGame();
		int prevpos, prevsize;
		for (int i = 0; i < 4; i++){
			for (Player p : playerOrder){
				message = new DiscardLeaderMessage(p.getLeaderCards().get(0));
				message.setPlayer(p);
				prevpos = p.getMarkerPosition();
				prevsize = p.getLeaderCards().size();
				controller.handleMessage(message);
				if (p.equals(game.getTurn().getPlayer())){
					assertEquals(p.getLeaderCards().size(), prevsize - 1);
					assertEquals(prevpos + 1, p.getMarkerPosition());
				} else {
					assertEquals(controller.getError(), "It is not your turn");
					controller.clearError();
				}
			}
			skipTurn();
		}
	}

	/**
	 * Tests handleDiscardLeader for each player at the end of their turn
	 * */
	@RepeatedTest(value = 5)
	public void testDiscardLeaderEnd(){
		testStartGame();
		int prevpos, prevsize;
		Player p;
		for (int i = 0; i < 4; i++){
			p = game.getTurn().getPlayer();
			prevpos = p.getMarkerPosition();
			prevsize = p.getLeaderCards().size();
			game.getTurn().getProducedResources().add(Resource.SERVANT);
			message = new DiscardLeaderMessage(p.getLeaderCards().get(0));
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(controller.getError(),"You must end your action first");
			controller.clearError();
			game.getTurn().getProducedResources().clear();

			game.getTurn().getRequiredResources().add(Resource.SERVANT);
			message = new DiscardLeaderMessage(p.getLeaderCards().get(0));
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(controller.getError(),"You must end your action first");
			controller.clearError();
			game.getTurn().getRequiredResources().clear();
			
			emptyAction();
			prevpos = p.getMarkerPosition();
			message = new DiscardLeaderMessage(p.getLeaderCards().get(0));
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(p.getLeaderCards().size(), prevsize - 1);
			assertEquals(prevpos + 1, p.getMarkerPosition());

			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
		}
	}

	/**
	 * Grants the active player enough material to activate a leader card
	 * @Param leader is card to be activated
	 */
	private void makeActivable(LeaderCard leader){
		if (leader instanceof LeaderCardResourcesCost){
			LeaderCardResourcesCost true_card = (LeaderCardResourcesCost) leader;
			game.getTurn().getPlayer().insertResources(true_card.getRequirements());
		} else if (leader instanceof LeaderCardLevelCost){
			LeaderCardLevelCost true_card = (LeaderCardLevelCost) leader;
			for (CardLevel lvl : true_card.getRequirements()){
				int slot = 0;
				for (int i = 1; i <= lvl.getLevel(); i++){
					game.getTurn().getPlayer().buyCard(new DevelopmentCard(0, null, null, new CardLevel(i, lvl.getColor()), 0, null), slot);
				}
				slot++;
			}
		}
	}
	
	/**
	 * Tests handleActivateLeader for each player at the start of their turn
	 * */
	@RepeatedTest(value = 5)
	public void testActivateLeaderStart(){
		testStartGame();
		for (int i = 0; i < 4; i++){
			for (Player p : playerOrder){
				LeaderCard leader = p.getLeaderCards().get(0);
				message = new ActivateLeaderMessage(leader);
				message.setPlayer(p);
				controller.handleMessage(message);
				if (p.equals(game.getTurn().getPlayer())){
					assertFalse(p.isActivable(leader));
					assertEquals(controller.getError(), "The requested card cannot be activated");
					controller.clearError();
					makeActivable(leader);
					controller.handleMessage(message);
					assertTrue(p.isActivable(leader));
					assertTrue(leader.isActive());
					controller.handleMessage(message);
					assertTrue(p.isActivable(leader));
					assertEquals(controller.getError(), "The requested card cannot be activated");
					controller.clearError();	
				} else {
					assertEquals(controller.getError(), "It is not your turn");
					controller.clearError();
				}
			}
			skipTurn();
		}
	}

	/**
	 * Tests handleActivateLeader for each player at the end of their turn
	 * */
	@RepeatedTest(value = 5)
	public void testActivateLeaderEnd(){
		testStartGame();
		Player p;
		LeaderCard leader;
		for (int i = 0; i < 4; i++){
			p = game.getTurn().getPlayer();
			leader = p.getLeaderCards().get(0);
			emptyAction();
			game.getTurn().getProducedResources().add(Resource.SERVANT);
			message = new ActivateLeaderMessage(leader);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(controller.getError(),"You must end your action first");
			controller.clearError();
			game.getTurn().getProducedResources().clear();

			game.getTurn().getRequiredResources().add(Resource.SERVANT);
			controller.handleMessage(message);
			assertEquals(controller.getError(),"You must end your action first");
			controller.clearError();
			game.getTurn().getRequiredResources().clear();
			
			controller.handleMessage(message);
			assertEquals(controller.getError(), "The requested card cannot be activated");
			assertFalse(leader.isActive());
			controller.clearError();

			makeActivable(leader);
			controller.handleMessage(message);
			assertTrue(leader.isActive());

			controller.handleMessage(message);
			assertEquals(controller.getError(), "The requested card cannot be activated");
			controller.clearError();

			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
		}
	}
	
	/**
	 * Tests the controller to correctly deny non-active players the requested action
	 * @param message contains the requested action
	 */
	private void checkIsNotYourTurn (Message message){
	    for (Player p : playerOrder){
			if (!p.equals(game.getTurn().getPlayer())){
				message.setPlayer(p);
				controller.handleMessage(message);
				assertEquals(controller.getError(), "It is not your turn");
				controller.clearError();
			}
		}
	}

	/**
	 * Tests handleMarket to properly return requested resource rows for every player
	 */
	@RepeatedTest(value = 5)
	public void testMarketRows(){
		testStartGame();
		message = new MarketMessage(true, 0);
		checkIsNotYourTurn(message);

		ArrayList<Resource> test_market;
		int prevpos, steps;
		for (int i = 1; i <= 4; i++){
			for (Player p: playerOrder){
				message = new MarketMessage(false, i);
				message.setPlayer(p);
				prevpos = p.getMarkerPosition();
				if (i != 4){
					test_market = game.getMarket().getRow(i - 1);
					test_market = (ArrayList<Resource>) test_market.stream().filter(e -> e != null).collect(Collectors.toList());
					steps = (int) test_market.stream().filter(e -> e.equals(Resource.FAITH)).count();
					test_market = (ArrayList<Resource>) test_market.stream().filter(e -> !e.equals(Resource.FAITH)).collect(Collectors.toList());
					controller.handleMessage(message);
					assertEquals(test_market, game.getTurn().getProducedResources());
					assertEquals(prevpos + steps, p.getMarkerPosition());

					controller.handleMessage(message);
					assertEquals("You already played your main action", controller.getError());
					controller.clearError();
				} else {
					controller.handleMessage(message);
					assertEquals("The row or column you requested doesn't exist", controller.getError());
					controller.clearError();
					emptyAction();
				}
				game.getTurn().getProducedResources().clear();
				message = new EndTurnMessage();
				message.setPlayer(p);
				controller.handleMessage(message);
			}
			playerOrder = game.getPlayers();
		}

	}
	
	/**
	 * Tests handleMarket to properly return requested resource columns for every player 
	 */
	@RepeatedTest(value = 5)
	public void testMarketColumns(){
		testStartGame();
		message = new MarketMessage(true, 0);
		checkIsNotYourTurn(message);

		ArrayList<Resource> test_market;
		int prevpos, steps;
		for (int i = 1; i <= 5; i++){
			for (Player p: playerOrder){
				message = new MarketMessage(true, i);
				message.setPlayer(p);
				prevpos = p.getMarkerPosition();
				if (i != 5){
					test_market = game.getMarket().getColumn(i - 1);
					test_market = (ArrayList<Resource>) test_market.stream().filter(e -> e != null).collect(Collectors.toList());
					steps = (int) test_market.stream().filter(e -> e.equals(Resource.FAITH)).count();
					test_market = (ArrayList<Resource>) test_market.stream().filter(e -> !e.equals(Resource.FAITH)).collect(Collectors.toList());
					controller.handleMessage(message);
					assertEquals(test_market, game.getTurn().getProducedResources());
					assertEquals(prevpos + steps, p.getMarkerPosition());

					controller.handleMessage(message);
					assertEquals("You already played your main action", controller.getError());
					controller.clearError();
				} else {
					controller.handleMessage(message);
					assertEquals("The row or column you requested doesn't exist", controller.getError());
					controller.clearError();
					emptyAction();
				}
				game.getTurn().getProducedResources().clear();
				message = new EndTurnMessage();
				message.setPlayer(p);
				controller.handleMessage(message);
			}
			playerOrder = game.getPlayers();
		}

	}

	/**
	 * Grants to the active player enough resources to buy a card
	 * @param card is the card to activate
	 */
	private void makeBuyable(DevelopmentCard card){
		game.getTurn().getPlayer().insertResources(card.getCost());
	}

	/**
	 * Tests handleBuyCard with full instruction coverage for each player
	 */
	@RepeatedTest(value = 5)
	public void testBuyCard(){
		testStartGame();
		message = new BuyCardMessage(0, 0, 0);
		checkIsNotYourTurn(message);

		int column = 0, slot = 0;
		DevelopmentCard card;
		for (Player p : playerOrder){
			slot = slot%3 + 1;

			message = new BuyCardMessage(3, column, slot);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals("The card you requested does not exist", controller.getError());
			controller.clearError();

			message = new BuyCardMessage(2, column, slot);
			card = game.getTopCards()[2][column];
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals("You do not have enough resources to buy this card", controller.getError());
			controller.clearError();

			makeBuyable(card);
			controller.handleMessage(message);
			assertEquals(card, p.getTopCards()[slot-1]);
			assertNotEquals(card, game.getTopCards()[2][column]);
			assertEquals(game.getTurn().getRequiredResources(), card.getCost());
			assertTrue(game.getTurn().hasDoneAction());

			controller.handleMessage(message);
			assertEquals(controller.getError(), "You already played your main action");
			game.getTurn().getRequiredResources().clear();
			game.getTurn().setDoneAction(false);

			message = new BuyCardMessage(1, column, slot%3 + 1);
			message.setPlayer(p);
			card = game.getTopCards()[1][column];
			makeBuyable(card);
			controller.handleMessage(message);
			assertEquals("You cannot put the card in the requested slot", controller.getError());
			controller.clearError();

			message = new BuyCardMessage(1, column, slot);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(card, p.getTopCards()[slot-1]);
			assertNotEquals(card, game.getTopCards()[1][column]);
			assertEquals(game.getTurn().getRequiredResources(), card.getCost());
			assertTrue(game.getTurn().hasDoneAction());
			game.getTurn().getRequiredResources().clear();
			game.getTurn().setDoneAction(false);

			message = new BuyCardMessage(0, column, slot);
			message.setPlayer(p);
			card = game.getTopCards()[0][column];
			makeBuyable(card);
			controller.handleMessage(message);
			assertEquals(card, p.getTopCards()[slot-1]);
			assertNotEquals(card, game.getTopCards()[0][column]);
			assertEquals(game.getTurn().getRequiredResources(), card.getCost());
			assertTrue(game.getTurn().hasDoneAction());
			game.getTurn().getRequiredResources().clear();

			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
			column++;
			slot++;
		}
	}

	/**
	 * Tests handleProduction with full instruction coverage for each player
	 */
	@Test
	public void testActivateProduction(){
		testStartGame();
		ArrayList<ProductionInterface> productions = new ArrayList<ProductionInterface>();
		message = new ProductionMessage(productions);
		checkIsNotYourTurn(message);
		ArrayList<Resource> cost = new ArrayList<Resource>();
		ArrayList<Resource> produced = new ArrayList<Resource>();
		ArrayList<Resource> tot_cost = new ArrayList<Resource>();
		ArrayList<Resource> tot_produced = new ArrayList<Resource>();

		int steps, prevpos;
		for (Player p : playerOrder){
			prevpos = p.getMarkerPosition();
			cost.add(Resource.SERVANT);
			cost.add(Resource.SERVANT);
			produced.add(Resource.SHIELD);
			produced.add(Resource.SHIELD);
			productions.add(new Production ((ArrayList<Resource>)cost.clone(), (ArrayList<Resource>)produced.clone()));
			message = new ProductionMessage(productions);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals("You don't have enough resources to activate these production powers", controller.getError());
			controller.clearError();	
			
			p.insertResources(cost);
			controller.handleMessage(message);
			tot_cost.addAll(cost);
			tot_produced.addAll(produced);
			assertEquals(game.getTurn().getRequiredResources(), tot_cost);
			assertEquals(game.getTurn().getProducedResources(), tot_produced);
			assertTrue(game.getTurn().hasDoneAction());

			cost.clear();
			produced.clear();
			game.getTurn().getRequiredResources().clear();
			game.getTurn().getProducedResources().clear();
			cost.add(Resource.COIN);
			cost.add(Resource.COIN);
			produced.add(Resource.STONE);
			produced.add(Resource.FAITH);
			produced.add(Resource.FAITH);
			produced.add(Resource.FAITH);
			productions.add(new Production (cost, produced));
			message = new ProductionMessage(productions);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals("You already played your main action", controller.getError());
			controller.clearError();	
			
			game.getTurn().setDoneAction(false);
			controller.handleMessage(message);
			assertEquals("You don't have enough resources to activate these production powers", controller.getError());
			controller.clearError();	

			p.insertResources(cost);
			controller.handleMessage(message);
			tot_cost.addAll(cost);
			tot_produced.addAll(produced);
			steps = (int)tot_produced.stream().filter(x->x.equals(Resource.FAITH)).count();
			tot_produced = (ArrayList<Resource>) tot_produced.stream().filter(e -> !e.equals(Resource.FAITH)).collect(Collectors.toList());
			assertEquals(game.getTurn().getRequiredResources(), tot_cost);
			assertEquals(game.getTurn().getProducedResources(), tot_produced);
			assertEquals(p.getMarkerPosition(), prevpos+steps);
			assertTrue(game.getTurn().hasDoneAction());
			game.getTurn().getRequiredResources().clear();
			game.getTurn().getProducedResources().clear();
			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
			
			cost.clear();
			produced.clear();
			tot_cost.clear();
			tot_produced.clear();
			productions.clear();
		}
	}

	/**
	 * Tests handlePay with full instruction coverage for each player
	 */
	@RepeatedTest(value = 10)
	public void testPayResources(){
		testStartGame();
		message = new PayMessage(new Storage());
		checkIsNotYourTurn(message);

		ArrayList<Resource> warehousebot_res = new ArrayList<Resource>();
		ArrayList<Resource> warehousemid_res = new ArrayList<Resource>();
		ArrayList<Resource> strongbox_res = new ArrayList<Resource>();
		for (Player p: playerOrder){
			p.clearWarehouse();
			warehousebot_res.add(Resource.STONE);
			warehousebot_res.add(Resource.STONE);
			warehousebot_res.add(Resource.STONE);
			p.storeBottom(warehousebot_res);
			warehousemid_res.add(Resource.COIN);
			warehousemid_res.add(Resource.COIN);
			strongbox_res.addAll(game.getTopCards()[0][0].getCost());
			p.insertResources(strongbox_res);
			game.getTurn().setDoneAction(true);
			game.getTurn().getRequiredResources().addAll(warehousebot_res);
			game.getTurn().getRequiredResources().addAll(strongbox_res);
			test_storage = new Storage();
			test_storage.addToWarehouseMid(warehousemid_res);
			test_storage.addToStrongbox(strongbox_res);

			message = new PayMessage(test_storage);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals("You cannot pay more than you should", controller.getError());
			controller.clearError();

			game.getTurn().getRequiredResources().addAll(warehousemid_res);
			controller.handleMessage(message);
			assertEquals("The resources you offered for payment aren't available", controller.getError());
			controller.clearError();
			
			p.storeMiddle(warehousemid_res);
			controller.handleMessage(message);
			assertEquals(game.getTurn().getRequiredResources(), warehousebot_res);
			assertTrue(p.getStrongBox().isEmpty());
			assertTrue(p.getMiddleResources().isEmpty());
			
			game.getTurn().getProducedResources().addAll(strongbox_res);
			game.getTurn().setDiscard(false);
			test_storage.getWarehouseMid().clear();
			test_storage.getStrongbox().clear();
			test_storage.addToWarehouseBot(warehousebot_res);
			controller.handleMessage(message);
			assertTrue(game.getTurn().getRequiredResources().isEmpty());
			assertTrue(p.getBottomResources().isEmpty());
			assertTrue(game.getTurn().getProducedResources().isEmpty());
			assertTrue(p.getStrongBox().areContainedInStrongbox(strongbox_res));
			
			game.getTurn().setDoneAction(true);
			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
			
			warehousebot_res.clear();
			warehousemid_res.clear();
			strongbox_res.clear();
		}
	}
	
	/**
	 * Tests handleStore with full instruction coverage for each player
	 */
	@RepeatedTest(value = 10)
	public void testStoreResources(){
		testStartGame();
		message = new StoreMessage(new Storage());
		checkIsNotYourTurn(message);

		ArrayList<Resource> warehousebot_res = new ArrayList<Resource>();
		ArrayList<Resource> warehousemid_res = new ArrayList<Resource>();
		ArrayList<Resource> warehousetop_res = new ArrayList<Resource>();
		for (Player p: playerOrder){
			p.clearWarehouse();
			warehousebot_res.add(Resource.STONE);
			warehousebot_res.add(Resource.STONE);
			warehousebot_res.add(Resource.STONE);
			warehousemid_res.add(Resource.COIN);
			warehousemid_res.add(Resource.COIN);
			warehousetop_res.add(Resource.SHIELD);
			p.storeTop(warehousetop_res);
			game.getTurn().getProducedResources().addAll(warehousebot_res);
			game.getTurn().getProducedResources().addAll(warehousetop_res);
			test_storage = new Storage();
			test_storage.addToWarehouseTop(warehousetop_res);
			test_storage.addToWarehouseMid(warehousemid_res);
			test_storage.addToWarehouseBot(warehousebot_res);
			
			game.getTurn().getRequiredResources().add(Resource.SERVANT);
			message = new StoreMessage(test_storage);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals("You must pay all of the cost first", controller.getError());
			controller.clearError();

			game.getTurn().getRequiredResources().clear();
			controller.handleMessage(message);
			assertEquals("You cannot store more than you should", controller.getError());
			controller.clearError();

			game.getTurn().getProducedResources().addAll(warehousemid_res);
			controller.handleMessage(message);
			assertEquals("There isn't enough room for the resources you are trying to store", controller.getError());
			controller.clearError();
			
			p.getTopResource().clear();
			test_storage.getWarehouseTop().clear();
			message = new StoreMessage(test_storage);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(game.getTurn().getProducedResources(), warehousetop_res);
			assertEquals(p.getBottomResources(), warehousebot_res);
			assertEquals(p.getMiddleResources(), warehousemid_res);
			
			test_storage.getWarehouseMid().clear();
			test_storage.getWarehouseBot().clear();
			test_storage.addToWarehouseTop(warehousetop_res);
			message = new StoreMessage(test_storage);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(p.getTopResource(), warehousetop_res);
			assertTrue(game.getTurn().getProducedResources().isEmpty());
			
			game.getTurn().setDoneAction(true);
			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
			
			warehousebot_res.clear();
			warehousemid_res.clear();
			warehousetop_res.clear();
		}
	}

	/**
	 * Tests handleDiscardResources with full instruction coverage for each player
	 */
	@RepeatedTest(value = 5)
	public void testDiscardResources(){
		testStartGame();
		message = new DiscardResourcesMessage();
		checkIsNotYourTurn(message);
		
		Player p;
		int prevpos[] = {0, 0, 0, 0};
		int steps;
		for (int i = 0; i < playerOrder.size(); i++){
			message = new DiscardResourcesMessage();
			p = playerOrder.get(i);
			game.getTurn().getProducedResources().addAll(game.getTopCards()[0][i].getCost());
			game.getTurn().getRequiredResources().add(Resource.COIN);
			for (int j = 0; j < playerOrder.size() && j!=i; j++){
				prevpos[j] = playerOrder.get(j).getMarkerPosition();
			}
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(controller.getError(), "You must pay all of the cost first");
			
			game.getTurn().getRequiredResources().clear();
			steps = game.getTurn().getProducedResources().size();
			controller.handleMessage(message);
			assertTrue(game.getTurn().getProducedResources().isEmpty());
			for (int j = 0; j < playerOrder.size() && j!=i; j++){
				if (prevpos[j] + steps > 24){
					assertEquals(24, playerOrder.get(j).getMarkerPosition());
				} else {	
					assertEquals(prevpos[j] + steps, playerOrder.get(j).getMarkerPosition());
				}
			}
			game.getTurn().setDoneAction(true);
			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
		}
	}
	
	/**
	 * Tests handleRearrange with full instruction coverage for each player
	 */
	@Test
	public void testRearrange(){
		testStartGame();
		message = new RearrangeMessage(1, 1);
		checkIsNotYourTurn(message);
		
		ArrayList<Resource> bot_res = new ArrayList<Resource>();
		ArrayList<Resource> mid_res = new ArrayList<Resource>();
		ArrayList<Resource> top_res = new ArrayList<Resource>();
		for (Player p : playerOrder){
			p.clearWarehouse();
			bot_res.add(Resource.STONE);
			mid_res.add(Resource.COIN);
			top_res.add(Resource.SHIELD);
			p.storeBottom(bot_res);
			p.storeMiddle(mid_res);
			p.storeTop(top_res);
			
			message = new RearrangeMessage(1,2);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(p.getTopResource(), mid_res);
			assertEquals(p.getMiddleResources(), top_res);
			p.storeMiddle(top_res);
			controller.handleMessage(message);
			assertEquals(controller.getError(), "You cannot swap the selected rows");

			message = new RearrangeMessage(1,3);
			message.setPlayer(p);
			controller.handleMessage(message);
			assertEquals(p.getTopResource(), bot_res);
			assertEquals(p.getBottomResources(), mid_res);
			p.storeBottom(mid_res);
			controller.handleMessage(message);
			assertEquals(controller.getError(), "You cannot swap the selected rows");

			message = new RearrangeMessage(2,3);
			message.setPlayer(p);
			top_res.add(Resource.SHIELD);
			mid_res.add(Resource.COIN);
			controller.handleMessage(message);
			assertEquals(p.getMiddleResources(), mid_res);
			assertEquals(p.getBottomResources(), top_res);
			top_res.clear();
			top_res.add(Resource.SHIELD);
			p.storeBottom(top_res);
			controller.handleMessage(message);
			assertEquals(controller.getError(), "You cannot swap the selected rows");
			game.getTurn().setDoneAction(true);

			message = new EndTurnMessage();
			message.setPlayer(p);
			controller.handleMessage(message);
			bot_res.clear();
			mid_res.clear();
			top_res.clear();
		}
	}
}
