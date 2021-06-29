package it.polimi.ingsw.view.gui;

import javafx.application.Platform;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;

import java.util.ArrayList;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.InitializingMessageType;
import it.polimi.ingsw.controller.servermessage.EndGameMessage;
import it.polimi.ingsw.controller.message.InitializingMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.util.observer.SetupGameObserver;
import it.polimi.ingsw.util.observer.GameStartObserver;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

import it.polimi.ingsw.view.gui.scene.PlayerBoardScene;
import it.polimi.ingsw.view.gui.scene.InitialScene;
import it.polimi.ingsw.view.gui.scene.SetupScene;
import it.polimi.ingsw.view.gui.scene.FinalScene;

import it.polimi.ingsw.view.gui.App;
import it.polimi.ingsw.view.View;

public class GUI extends View implements GameStartObserver, SetupGameObserver {
	private App app;
	private InitialScene initial_scene;
	private Client client;
	private ArrayList<InitializingServerMessage> messages_queued;

	public GUI() {
		this.simple_players = new ArrayList<SimplePlayer>();
		this.messages_queued = new ArrayList<InitializingServerMessage>();
	}

	@Override
	public void startView(Client client) {
		this.client = client;
		this.addGameStartObserver(this);
		this.addSetupGameObserver(this);
		App.setModel(this);
		new Thread(() -> {
			App.main(null);
		}).start();
	}

	@Override
	public void handleError(ErrorMessage error_message) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR, error_message.error_string, ButtonType.OK);
			if (!App.isInitialized()) {
				alert.showAndWait().ifPresent(response -> {
					 if (response == ButtonType.OK) {
						 System.exit(1);
					 }
				});
			} else if (error_message.nickname == null || error_message.nickname.equals(this.getMyPlayer().getNickname())) {
				alert.show();
			}
		});
	}

	@Override
	public void handleInitializing(InitializingServerMessage initializing_message) {
		// if the game isn't initialized yet, add the InitializingServerMessages to an ArrayList
		// the method will be called when the initialization is done using the pattern Observer
		if (!this.initialized) {
			this.messages_queued.add(initializing_message);
			return;
		}

		// get all the answers from the InitialScene
		switch(initializing_message.type) {
			case NICKNAME:
				this.nickname = this.initial_scene.getNickname();
				this.client.sendMessage(new InitializingMessage(this.initial_scene.getNickname()));
				break;
			case CHOOSE_MATCH_NAME:
				this.client.sendMessage(new InitializingMessage(this.initial_scene.getMatchName()));
				break;
			case STARTED_MATCH_NAME:
				// show the match_name to the Client
				this.initial_scene.setMatchName(initializing_message.match_name);
				System.out.println("Started match " + initializing_message.match_name);
				break;
			case NUM_PLAYERS:
				this.client.sendMessage(new InitializingMessage(String.valueOf(this.initial_scene.getNumPlayers())));
				break;
			case START_MATCH:
				System.out.println("Start");
		}
	}

	@Override
	public void handleEndGame(EndGameMessage end_game_message) {
		Platform.runLater(() -> {
			FinalScene final_scene = new FinalScene(end_game_message.rank);
			final_scene.changeScene("/fxml/finalscene.fxml");
		});
	}

	/**
	 * Method from GameStartObserver, gets called when the game is started
	 */
	@Override
	public void gameStarted() {
		Platform.runLater(() -> {
			//new SetupScene().changeScene("/fxml/setupscene.fxml");
			new PlayerBoardScene().changeScene("/fxml/playerboardscene.fxml");	
		});
	}

	public SimpleGame getSimpleGame() {
		return this.simple_game;
	}

	public ArrayList<SimplePlayer> getSimplePlayers() {
		return this.simple_players;
	}

	public Turn getTurn() {
		return this.turn;
	}

	/**
	 * Send a message to the Server
	 *
	 * @param message the Message to send
	 */
	public void sendMessage(Message message) {
		this.client.sendMessage(message);
	}

	/**
	 * Set the app and and the initial scene
	 *
	 * @param app the main JavaFX class
	 */
	public void setApp(App app) {
		this.app = app;
		this.initial_scene = this.app.getInitialScene();
	}

	/**
	 * Now that the initialization has finished, call handleInitializing again with the queued messages
	 */
	public void finishedInitialization() {
		this.initialized = true;
		for (InitializingServerMessage message: this.messages_queued) {
			handleInitializing(message);
		}
		this.messages_queued.clear();
	}

	/**
	 * Method from SetupGameObserver, gets called when the game is initialized
	 */
	@Override
	public void gameHasDoneSetup(){
		Platform.runLater(() -> {
			new PlayerBoardScene().changeScene("/fxml/playerboardscene.fxml");
		});
	}
}
