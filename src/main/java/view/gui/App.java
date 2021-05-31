package it.polimi.ingsw.view.gui;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.File;

public class App extends Application {
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
}
