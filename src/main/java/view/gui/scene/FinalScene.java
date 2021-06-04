package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.Initializable;

import java.util.ResourceBundle;
import java.util.HashMap;
import java.net.URL;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

public class FinalScene extends SceneController implements Initializable {
	HashMap<String, Integer> rank;

	public FinalScene(HashMap<String, Integer> rank) {
		this.rank = rank;
	}

	public void initialize(URL location, ResourceBundle resources) {
		return;
	}
}
