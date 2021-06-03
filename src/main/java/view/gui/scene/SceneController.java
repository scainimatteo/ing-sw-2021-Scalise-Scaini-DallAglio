package it.polimi.ingsw.view.gui.scene;

import javafx.scene.Node;

public abstract class SceneController {
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
}
