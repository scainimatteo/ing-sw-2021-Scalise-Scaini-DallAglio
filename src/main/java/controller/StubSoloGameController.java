package it.polimi.ingsw.controller;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;

import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.Initializer;
import it.polimi.ingsw.controller.StubInitializer;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GameController;

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
import it.polimi.ingsw.model.game.sologame.SoloGame;

import it.polimi.ingsw.server.ClientHandler;


public class StubSoloGameController extends SoloGameController{
	String error;

	public StubSoloGameController(ArrayList<ClientHandler> clients){
		super(clients);
	}

	public void initializeGame(){
		try {
			this.game = new StubInitializer().initializeSoloGame();
		} catch (InstantiationException e) {
			System.out.println("Game could not start");
		}
	}
	
	public SoloGame getGame(){
		return (SoloGame) this.game;
	}

	@Override
	protected void handleError(String error_string, Player player){;
		this.error = error_string;
	}

	protected void clearError(){
		this.error = null;
	}

	protected String getError(){
		return this.error;
	}
}
