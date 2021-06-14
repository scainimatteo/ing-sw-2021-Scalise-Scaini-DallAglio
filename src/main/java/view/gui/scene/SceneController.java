package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.event.EventHandler;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;

import java.util.ArrayList;
import java.io.IOException;

public abstract class SceneController {
	protected static Stage stage;
	protected ArrayList<String> drag_and_drop_arraylist = new ArrayList<String>();
	protected String last_resource_dragged;

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

	/**
	 * SOURCE METHODS
	 */
	@FXML
	public void handleDragDetected(MouseEvent event, ImageView source) {
		EventHandler<MouseEvent> event_handler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
				
				ClipboardContent content = new ClipboardContent();
				content.putImage(source.getImage());
				db.setContent(content);
				last_resource_dragged = source.getId();
				
				event.consume();
			}
		};

		event_handler.handle(event);
	}

	@FXML
	public void handleDragDone(DragEvent event, ImageView source){
		EventHandler<DragEvent> event_handler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getTransferMode() == TransferMode.MOVE) {
					source.setImage(null);
				}

				event.consume();
			}
		};

		event_handler.handle(event);
	}

	/**
	 * TARGET METHODS
	 */
	@FXML
	public void handleDragOver(DragEvent event, ImageView target){
		EventHandler<DragEvent> event_handler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getGestureSource() != target) {
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				
				event.consume();
			}
		};

		event_handler.handle(event);
	}

	@FXML
	public void handleDragDropped(DragEvent event, ImageView target){
		EventHandler<DragEvent> event_handler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;

				if (db.hasImage()) {
				   target.setImage(db.getImage());
				   last_resource_dragged += " | " + target.getId();
				   drag_and_drop_arraylist.add(last_resource_dragged);
				   success = true;
				}

				event.setDropCompleted(success);
				
				event.consume();
			 }
		};

		event_handler.handle(event);
	}
}
