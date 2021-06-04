package it.polimi.ingsw.view.gui.scene;

import javafx.application.Platform;

import javafx.fxml.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ResourceBundle;
import java.net.URL;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

public class InitialScene extends SceneController implements Initializable {
	@FXML Label nicknameLabel;
	@FXML TextField nicknameText;
	@FXML ToggleGroup numPlayersGroup;
	@FXML RadioButton rb4;
	@FXML TextField matchText;
	@FXML VBox startLayout;
	@FXML VBox createLayout;
	@FXML VBox joinLayout;
	@FXML VBox matchLayout;
	@FXML Button cancelButton;
	@FXML Label matchNameLabel;

	private String nickname = "";
	private int num_players = -1;
	private String match_name = "";
	private boolean finishedInitialization = false;

	public String getNickname() {
		return this.nickname;
	}

	public int getNumPlayers() {
		return this.num_players;
	}

	public String getMatchName() {
		return this.match_name;
	}

	public void setMatchName(String match_name) {
		this.match_name = match_name;
		Platform.runLater(() -> matchNameLabel.setText(this.match_name));
	}

	public boolean isInitialized() {
		return this.finishedInitialization;
	}

	/**
	 * Hide the layouts
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		hideNode(createLayout);
		hideNode(joinLayout);
		hideNode(cancelButton);
		hideNode(matchLayout);
	}

	/**
	 * Create a new match
	 */
	public void selectCreateHandler() {
		// set 4 players as the default
		numPlayersGroup.selectToggle(rb4);
		this.num_players = Integer.parseInt(rb4.getText());

		// show the createLayout layout
		hideNode(startLayout);
		showNode(createLayout);
		showNode(cancelButton);
	}

	/**
	 * Join an existing match
	 */
	public void selectJoinHandler() {
		// show the startLayout layout
		hideNode(startLayout);
		showNode(joinLayout);
		showNode(cancelButton);
	}

	/**
	 * Create the match and finish the initialization
	 */
	public void createHandler() {
		this.nickname = nicknameText.getText();
		this.num_players = Integer.parseInt(((RadioButton) numPlayersGroup.getSelectedToggle()).getText());
		this.finishedInitialization = true;
		App.finishedInitialization();
		startWaiting();
	}

	/**
	 * Join the match and finish the initialization
	 */
	public void joinHandler() {
		this.nickname = nicknameText.getText();
		this.setMatchName(matchText.getText());
		this.finishedInitialization = true;
		App.finishedInitialization();
		startWaiting();
	}

	/**
	 * Go back to the original layout
	 */
	public void cancelHandler() {
		hideNode(createLayout);
		hideNode(joinLayout);
		hideNode(cancelButton);
		showNode(startLayout);
	}

	/**
	 * Start the waiting layout, until all players are connected to the match
	 */
	private void startWaiting() {
		hideNode(startLayout);
		hideNode(createLayout);
		hideNode(joinLayout);
		hideNode(nicknameLabel);
		hideNode(nicknameText);
		hideNode(cancelButton);
		showNode(matchLayout);
	}
}
