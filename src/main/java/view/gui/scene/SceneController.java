package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;

import javafx.stage.Stage;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.io.IOException;
import java.util.HashMap;

public abstract class SceneController {
	protected static Stage stage;
	protected HashMap<ImageView, ImageView> drag_and_drop_hashmap = new HashMap<ImageView, ImageView>();
	protected ImageView last_image_dragged;

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
			e.printStackTrace();
		}
	}

	/**
	 * SOURCE METHODS
	 */
	/**
	 * Method called when an image is dragged
	 *
	 * @param event the event that creates the drag gesture
	 * @param source the imageview that creates the drag gesture
	 */
	@FXML
	public void handleDragDetected(MouseEvent event, ImageView source) {
		EventHandler<MouseEvent> event_handler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
				
				ClipboardContent content = new ClipboardContent();
				content.putImage(source.getImage());
				db.setContent(content);
				last_image_dragged = source;
				
				event.consume();
			}
		};

		event_handler.handle(event);
	}

	/**
	 * Method that handle when an image is dropped on a target
	 *
	 * @param event the event drag done when the image is dropped on a target
	 * @param source the imageview that is the source of the drag
	 * @param set_null the boolean that defines if in the source has to be setted to null the image
	 */
	@FXML
	public void handleDragDone(DragEvent event, ImageView source, boolean set_null){
		EventHandler<DragEvent> event_handler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				if (event.getTransferMode() == TransferMode.MOVE && set_null) {
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
	/**
	 * Method called when the drag gesture is over a target
	 *
	 * @param event the event of drag over a target
	 * @param target the imageview on which a drop gesture is passed
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

	/**
	 * Method that handle the drop on a target
	 *
	 * @param event the event of drop on a target
	 * @param target the imageview on which is dropped the image
	 */
	@FXML
	public void handleDragDropped(DragEvent event, ImageView target){
		EventHandler<DragEvent> event_handler = new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;

				if (db.hasImage()) {
				   target.setImage(db.getImage());
				   drag_and_drop_hashmap.put(last_image_dragged, target);
				   success = true;
				}

				event.setDropCompleted(success);
				
				event.consume();
			 }
		};

		event_handler.handle(event);
	}

	/**
	 * Return all the ImageViews from the target back to the source and clear the drag_and_drop_hashmap
	 */
	protected void reverseDragAndDrop() {
		for (ImageView imgview: this.drag_and_drop_hashmap.keySet()) {
			Image tmp_image = imgview.getImage();
			imgview.setImage(this.drag_and_drop_hashmap.get(imgview).getImage());
			this.drag_and_drop_hashmap.get(imgview).setImage(tmp_image);
		}

		this.drag_and_drop_hashmap.clear();
	}
}
