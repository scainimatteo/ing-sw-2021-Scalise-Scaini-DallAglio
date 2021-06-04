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

import it.polimi.ingsw.util.GameStartObserver;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.gui.scene.InitialScene;
import it.polimi.ingsw.view.gui.App;
import it.polimi.ingsw.view.View;

public class GUI extends View implements GameStartObserver {
	private App app;
	private InitialScene initial_scene;
	private Client client;

	public GUI() {
		this.simple_players = new ArrayList<SimplePlayer>();
	}

	@Override
	public void startView(Client client) {
		this.client = client;
		this.addObserver(this);
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
	public synchronized void handleInitializing(InitializingServerMessage initializing_message) {
		try {
			if (this.initial_scene == null) {
				while(this.app == null) {
					wait();
				};
				this.initial_scene = this.app.getInitialScene();

				while(!this.initialized) {
					wait();
				}
			}

			switch(initializing_message.type) {
				case NICKNAME:
					this.nickname = this.initial_scene.getNickname();
					this.client.sendMessage(new InitializingMessage(this.initial_scene.getNickname()));
					break;
				case CHOOSE_MATCH_NAME:
					this.client.sendMessage(new InitializingMessage(this.initial_scene.getMatchName()));
					break;
				case STARTED_MATCH_NAME:
					this.initial_scene.setMatchName(initializing_message.match_name);
					System.out.println("Started match " + initializing_message.match_name);
					break;
				case NUM_PLAYERS:
					this.client.sendMessage(new InitializingMessage(String.valueOf(this.initial_scene.getNumPlayers())));
					break;
				case START_MATCH:
					System.out.println("Start");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method from GameStartObserver, gets called when the game is started
	 */
	@Override
	public void gameStarted() {
		Platform.runLater(() -> {
			this.initial_scene.changeScene("/fxml/leadercardselectorscene.fxml");
		});
	}

	@Override
	public void handleEndGame(EndGameMessage end_game_message) {
		return;
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
	 * Send a message to the Client
	 *
	 * @param message the Message to send
	 */
	public void sendMessage(Message message) {
		this.client.sendMessage(message);
	}

	/**
	 * Set the app and notify the Thread
	 *
	 * @param app the main JavaFX class
	 */
	public synchronized void setApp(App app) {
		this.app = app;
		notify();
	}

	/**
	 * Notify the Thread that the initialization has finished
	 */
	public synchronized void finishedInitialization() {
		this.initialized = true;
		notify();
	}
}
