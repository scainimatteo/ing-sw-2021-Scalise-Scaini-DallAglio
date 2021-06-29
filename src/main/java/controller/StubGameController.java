package it.polimi.ingsw.controller;

import java.util.ArrayList;

import it.polimi.ingsw.controller.GameController;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.server.ClientHandler;

public class StubGameController extends GameController{
	String error;

	public StubGameController(ArrayList<ClientHandler> clients){
		super(clients);
	}

	@Override
	public void setupGame(){
		try {
			this.game = new StubSetupManager().setupGame(4);
		} catch (InstantiationException e) {
			System.out.println("Game could not start");
		}
	}

	public Game getGame(){
		return this.game;
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
