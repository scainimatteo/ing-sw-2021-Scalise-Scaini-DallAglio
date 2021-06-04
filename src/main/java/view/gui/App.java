package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXMLLoader;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.util.ArrayList;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.gui.scene.InitialScene;
import it.polimi.ingsw.view.gui.GUI;

public class App extends Application {
	private static SimpleGame game;
	private static ArrayList<SimplePlayer> players;
	private static Turn turn;
	public static GUI gui;

	private InitialScene initial_scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		SceneController.setStage(stage);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/initialscene.fxml"));
		this.initial_scene = new InitialScene();
		loader.setController(this.initial_scene);
		gui.setApp(this);
		Parent initial = loader.load();
		stage.setTitle("Maestri del Rinascimento");
		Scene scene = new Scene(initial, 300, 275);
		stage.setScene(scene);
		stage.show();
	}

	public InitialScene getInitialScene() {
		return this.initial_scene;
	}

	/**
	 * Set the three objects representing the model
	 *
	 * @param view the GUI to get the model from
	 */
	public static void setModel(GUI view) {
		game = view.getSimpleGame();
		players = view.getSimplePlayers();
		turn = view.getTurn();
		gui = view;
	}

	/**
	 * @return the SimplePlayer representing the Client
	 */
	public static SimplePlayer getMyPlayer() {
		return gui.getMyPlayer();
	}

	/**
	 * Show an error alert
	 *
	 * @param error the error String to display
	 */
	public void showError(String error) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
			alert.show();
		});
	}
}
