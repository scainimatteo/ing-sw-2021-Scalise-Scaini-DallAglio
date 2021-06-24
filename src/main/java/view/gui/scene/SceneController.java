package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.FXMLLoader;

import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.IOException;

public abstract class SceneController {
	private static Stage stage;

	public static void setStage(Stage stage) {
		SceneController.stage = stage;
	}

	/**
	 * Make a node invisible
	 *
	 * @param node the node to hide
	 */
	public void hideNode(Node node) {
		node.setVisible(false);
		node.setManaged(false);
	}

	/**
	 * Make a node visible
	 *
	 * @param node the node to show
	 */
	public void showNode(Node node) {
		node.setVisible(true);
		node.setManaged(true);
	}

	/**
	 * Change the current Scene with the one specified
	 *
	 * @param layout_fxml the path to the FXML file of the new Scene
	 */
	public void changeScene(String layout_fxml) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(layout_fxml));
			loader.setController(this);
			Parent layout = loader.load();
			Scene new_scene = new Scene(layout, 300, 275);
			SceneController.stage.setScene(new_scene);
		} catch (IOException e) {
			//TODO: better exception handling
			e.printStackTrace();
		}
	}
}
