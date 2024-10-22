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

import it.polimi.ingsw.util.observer.ErrorMessageObservable;
import it.polimi.ingsw.util.observer.ErrorMessageObserver;
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

public class GUI extends View implements GameStartObserver, SetupGameObserver, ErrorMessageObservable {
	private App app;
	private InitialScene initial_scene;
	private Client client;
	private boolean game_started = false;
	private ArrayList<InitializingServerMessage> messages_queued;
	private ArrayList<ErrorMessageObserver> error_message_observers = new ArrayList<ErrorMessageObserver>();

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
			if (!this.game_started) {
				alert.showAndWait().ifPresent(response -> {
					 if (response == ButtonType.OK) {
						 System.exit(1);
					 }
				});
			} else if (error_message.nickname == null || error_message.nickname.equals(this.getMyPlayer().getNickname())) {
				alert.show();
				notifyReceivedErrorMessage();
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
			case JOINED_MATCH:
				System.out.println("Joined match");
				break;
			case START_MATCH:
				System.out.println("Start");
		}
	}

	@Override
	public void handleEndGame(EndGameMessage end_game_message) {
		Platform.runLater(() -> {
			FinalScene final_scene = new FinalScene(end_game_message.nicknames, end_game_message.rank);
			final_scene.changeScene("/fxml/finalscene.fxml");
		});
	}

	/**
	 * Method from GameStartObserver, gets called when the game is started
	 */
	@Override
	public void gameStarted() {
		this.game_started = true;
		Platform.runLater(() -> {
			new SetupScene().changeScene("/fxml/setupscene.fxml");
		});
	}

	/**
	 * Set the GUI to uninitialized to exit after an ErrorMessage is delivered
	 */
	@Override
	public void setUninitialized() {
		this.initialized = false;
		this.game_started = false;
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
	 * OBSERVERS
	 */

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
		this.game_started = true;
		Platform.runLater(() -> {
			new PlayerBoardScene().changeScene("/fxml/playerboardscene.fxml");
		});
	}

	/**
	 * Add a ErrorMessageObserver to the error_message_observers array
	 *
	 * @param observer the ErrorMessageObserver to add
	 */
	@Override
	public void addErrorMessageObserver(ErrorMessageObserver observer){
		this.error_message_observers.add(observer);
	}

	/**
	 * Remove a ErrorMessageObserver from the error_message_observers array
	 *
	 * @param observer the ErrorMessageObserver to remove
	 */
	@Override
	public void removeErrorMessageObserver(ErrorMessageObserver observer) {
		this.error_message_observers.remove(observer);
	}

	/**
	 * Notify to the ErrorMessageObserver listening that the Player received an ErrorMessage
	 *
	 * @param error_message the ErrorMessage received
	 */
	@Override
	public void notifyReceivedErrorMessage(){
		for (ErrorMessageObserver e: this.error_message_observers){
			e.receivedErrorMessage();
		}
	}
}
