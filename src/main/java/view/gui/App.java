package it.polimi.ingsw.view.gui;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.util.ArrayList;
import java.io.File;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.gui.GUI;

public class App extends Application {
	private static SimpleGame game;
	private static ArrayList<SimplePlayer> players;
	private static Turn turn;

    public static void main(String[] args) {
		launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        Parent initial = FXMLLoader.load(new File("src/main/resources/fxml/initial.fxml").toURI().toURL());
        stage.setTitle("Maestri del Rinascimento");
		Scene scene = new Scene(initial, 300, 275);
        stage.setScene(scene);
        stage.show();
    }

	public static void setModel(GUI view) {
		game = view.getSimpleGame();
		players = view.getSimplePlayers();
		turn = view.getTurn();
	}
}
