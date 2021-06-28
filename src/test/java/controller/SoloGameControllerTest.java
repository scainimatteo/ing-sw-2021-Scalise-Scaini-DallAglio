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
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.StubGameController;
import it.polimi.ingsw.controller.StubSoloGameController;

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
import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.model.game.sologame.SoloGame;
import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossTwoSpaces;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossOneSpace;
import it.polimi.ingsw.model.game.sologame.DiscardDevelopmentCards;

import it.polimi.ingsw.server.ClientHandler;

public class SoloGameControllerTest{
	StubSoloGameController controller;
	SoloGame game;
	Message message;
	SoloPlayer p;
	ArrayList<Resource> test_resources;
	Storage test_storage;

	private void initGame(){
		controller =  new StubSoloGameController(null);
		controller.initializeGame();
		game = controller.getGame();
		test_resources = new ArrayList<Resource>();
		test_storage = new Storage();
		p = (SoloPlayer) game.getTurn().getPlayer();
	}

	@RepeatedTest(value = 10)
	public void testEndTurn(){
		initGame();
		message = new EndTurnMessage();
		message.setPlayer(p);
		controller.handleMessage(message);
		assertEquals(controller.getError(), "You cannot end your turn now");
		controller.clearError();
		
		for (int i = 0; i < 5; i++){
			game.getTurn().setDoneAction(true);
			int prevsize = game.getTokenAmount();
			controller.handleMessage(message);
			assertTrue(game.getTokenAmount() == prevsize - 1 || game.getTokenAmount() == 7); 
			assertFalse(game.getTurn().hasDoneAction());
		}
	}

	@RepeatedTest(value = 5)
	public void testDiscardResources(){
		initGame();
		int steps, prevpos;
		game.getTurn().getProducedResources().addAll(game.getTopCards()[0][0].getCost());
		game.getTurn().getRequiredResources().add(Resource.COIN);
		message = new DiscardResourcesMessage();
		message.setPlayer(p);
		controller.handleMessage(message);
		assertEquals(controller.getError(), "You must pay all of the cost first");
		controller.clearError();

		game.getTurn().getRequiredResources().clear();
		steps = game.getTurn().getProducedResources().size();
		prevpos = p.getBlackMarkerPosition();
		controller.handleMessage(message);
		assertTrue(game.getTurn().getProducedResources().isEmpty());
		assertEquals(p.getBlackMarkerPosition(), prevpos + steps);

		game.getTurn().setDoneAction(true);
		message = new EndTurnMessage();
		message.setPlayer(p);
		controller.handleMessage(message);
	}
	
	@Test
	public void testMoveTwoSpaces(){
		initGame();
		SoloActionToken token; 
		int prevpos = 0;
		for (int i = 0; i < 5; i++){
			token = new MoveBlackCrossTwoSpaces();
			prevpos = p.getBlackMarkerPosition();
			token.useToken(controller);
			assertEquals(p.getBlackMarkerPosition(), prevpos + 2);
		}
	}
	
	@Test
	public void testMoveOneSpace(){
		initGame();
		SoloActionToken token; 
		int prevpos = 0;
		for (int i = 1; i <= 7; i++){
			for (int j = 1; j <= i; j++){
				token = game.getTopToken();
				assertEquals(game.getTokenAmount(), 7 - j);
			}
			token = new MoveBlackCrossOneSpace();
			prevpos = p.getBlackMarkerPosition();
			token.useToken(controller);
			assertEquals(p.getBlackMarkerPosition(), prevpos + 1);
			assertEquals(game.getTokenAmount(), 7);
		}
	}

	@Test
	public void testDiscardDevelopmentCards(){
		initGame();
		DevelopmentCardsColor allcolors[] = {DevelopmentCardsColor.GREEN, DevelopmentCardsColor.BLUE, DevelopmentCardsColor.YELLOW, DevelopmentCardsColor.PURPLE};
		int order;
		assertFalse(controller.wasLastCardDiscarded());
		for (DevelopmentCardsColor color : allcolors){
			DiscardDevelopmentCards token = new DiscardDevelopmentCards(color);
			order = color.getOrder();
			game.getFromDeck(game.getTopCards()[2][order]);
			assertEquals(3, game.getDevelopmentCardsOnTable().getDeckSize(2, order));
			
			token.useToken(controller);
			assertEquals(1, game.getDevelopmentCardsOnTable().getDeckSize(2, order));

			token.useToken(controller);
			assertEquals(0, game.getDevelopmentCardsOnTable().getDeckSize(2, order));
			assertEquals(3, game.getDevelopmentCardsOnTable().getDeckSize(1, order));

			token.useToken(controller);
			assertEquals(1, game.getDevelopmentCardsOnTable().getDeckSize(1, order));

			token.useToken(controller);
			assertEquals(0, game.getDevelopmentCardsOnTable().getDeckSize(1, order));
			assertEquals(3, game.getDevelopmentCardsOnTable().getDeckSize(0, order));

			token.useToken(controller);
			assertEquals(1, game.getDevelopmentCardsOnTable().getDeckSize(0, order));

			token.useToken(controller);
			assertEquals(0, game.getDevelopmentCardsOnTable().getDeckSize(0, order));
			assertTrue(controller.wasLastCardDiscarded());
		}
	}
}

