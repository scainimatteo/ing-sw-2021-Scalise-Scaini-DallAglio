package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.util.ResourceBundle;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.net.URL;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

public class FinalScene extends SceneController implements Initializable {
	// Players and their points in order
	private HashMap<String, Integer> rank;
	private ArrayList<String> nicknames;

	@FXML private VBox endgame_pane;
	@FXML private VBox rank_pane;
	@FXML private VBox lost_pane;
	@FXML private VBox won_pane;
	@FXML private Text score;

	public FinalScene(ArrayList<String> nicknames, HashMap<String, Integer> rank) {
		this.rank = rank;
		this.nicknames = nicknames;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// sologame
		if (this.rank.keySet().size() == 1) {
			if (this.rank.containsValue(0)) {
				lostSoloGame();
			} else {
				wonSoloGame();
			}
		} else {
			// multiplayer game
			endGame();
		}
	}

	/**
	 * Show the lost_pane
	 */
	public void lostSoloGame() {
		hideNode(endgame_pane);
		hideNode(won_pane);
		showNode(lost_pane);
	}

	/**
	 * Show the won_pane, with the points made
	 */
	public void wonSoloGame() {
		hideNode(endgame_pane);
		hideNode(lost_pane);
		showNode(won_pane);

		for (String nick : nicknames){
			score.setText("Your score is " + this.rank.get(nick));
		}
	}

	/**
	 * Show the endgame_pane, with the points made by every Player
	 */
	public void endGame() {
		hideNode(lost_pane);
		hideNode(won_pane);
		showNode(endgame_pane);

		int i = 1;
		Iterator<String> iterator = this.nicknames.iterator();
		for (Node node: rank_pane.getChildren()) {
			if (iterator.hasNext()) {
				String nick = iterator.next();
				((Text) node).setText(nick + ": " + this.rank.get(nick) + " points");
				i++;
			} else {
				hideNode(node);
			}
		}
	}
}
