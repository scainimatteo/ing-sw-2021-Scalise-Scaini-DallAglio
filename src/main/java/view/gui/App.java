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
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.util.observer.ErrorMessageObserver;
import it.polimi.ingsw.util.observer.ViewUpdateObserver;
import it.polimi.ingsw.util.observer.SetupGameObserver;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.gui.scene.InitialScene;
import it.polimi.ingsw.view.gui.GUI;

public class App extends Application {
	private static GUI gui;

	private InitialScene initial_scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		SceneController.setStage(stage);
		this.initial_scene = new InitialScene();
		this.initial_scene.changeScene("/fxml/initialscene.fxml");
		gui.setApp(this);
		stage.setTitle("Maestri del Rinascimento");
		stage.setMaximized(true);
		stage.show();
	}

	public InitialScene getInitialScene() {
		return this.initial_scene;
	}

	/**
	 * Set the GUI that has the model
	 *
	 * @param view the GUI to get the model from
	 */
	public static void setModel(GUI view) {
		gui = view;
	}

	/**
	 * @return the SimpleGame in the GUI
	 */
	public static SimpleGame getSimpleGame() {
		return gui.getSimpleGame();
	}

	/**
	 * @return the ArrayList of SimplePlayers in the GUI
	 */
	public static ArrayList<SimplePlayer> getSimplePlayers() {
		return gui.getSimplePlayers();
	}

	/**
	 * @return the Turn in the GUI
	 */
	public static Turn getTurn() {
		return gui.getTurn();
	}

	/**
	 * @return the SimplePlayer representing the Client
	 */
	public static SimplePlayer getMyPlayer() {
		return gui.getMyPlayer();
	}

	/**
	 * @return true if there is a SimpleGame going on
	 */
	public static boolean isSoloGame() {
		return gui.getSimplePlayers().size() == 1;
	}

	/**
	 * @return true if the gui has finished the initialization phase
	 */
	public static boolean isInitialized() {
		return App.gui.isInitialized();
	}

	/**
	 * @return true if the gui has finished the setup phase
	 */
	public static boolean hasDoneSetup() {
		return App.gui.hasDoneSetup();
	}

	/**
	 * Send a message to the Server
	 *
	 * @param message the Message to send
	 */
	public static void sendMessage(Message message) {
		App.gui.sendMessage(message);
	}

	/**
	 * OBSERVERS
	 */

	/**
	 * Notify the GUI that the initialization has finished
	 */
	public static void finishedInitialization() {
		App.gui.finishedInitialization();
	}

	/**
	 * Set a ViewUpdateObserver to be updated when there's a ViewUpdate
	 *
	 * @param observer the ViewUpdateObserver to set
	 */
	public static void setViewUpdateObserver(ViewUpdateObserver observer) {
		gui.addViewUpdateObserver(observer);
	}

	/**
	 * Remove a ViewUpdateObserver from the GUI array of ViewUpdateObservers
	 *
	 * @param observer the ViewUpdateObserver to remove
	 */
	public static void removeViewUpdateObserver(ViewUpdateObserver observer) {
		gui.removeViewUpdateObserver(observer);
	}

	/**
	 * Set a SetupGameObserver to be updated when the Game has finished the setup phase
	 *
	 * @param observer the SetupGameObserver to set
	 */
	public static void setSetupGameObserver(SetupGameObserver observer) {
		gui.addSetupGameObserver(observer);
	}

	/**
	 * Set a ErrorMessageObserver to be updated when the Player receives an ErrorMessage
	 *
	 * @param observer the ErrorMessageObserver to set
	 */
	public static void setErrorMessageObserver(ErrorMessageObserver observer) {
		gui.addErrorMessageObserver(observer);
	}

	/**
	 * Remove a ErrorMessageObserver from the GUI array of ErrorMessageObservers
	 *
	 * @param observer the ErrorMessageObserver to remove
	 */
	public static void removeErrorMessageObserver(ErrorMessageObserver observer) {
		gui.removeErrorMessageObserver(observer);
	}
}
