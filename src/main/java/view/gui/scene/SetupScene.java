package it.polimi.ingsw.view.gui.scene;
import it.polimi.ingsw.view.gui.scene.SceneController;

import it.polimi.ingsw.view.gui.scene.PlayerBoardScene;
import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.controller.message.ChooseResourcesMessage;
import it.polimi.ingsw.controller.message.DiscardLeaderMessage;
import it.polimi.ingsw.controller.message.Storage;

import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.util.observer.ErrorMessageObserver;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;

import java.util.ResourceBundle;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.net.URL;

public class SetupScene extends SceneController implements Initializable, ErrorMessageObserver {
	ArrayList<LeaderCard> to_delete = new ArrayList<LeaderCard>();
	ArrayList<String> order;
	double opacity_percent = 0.5;
	SimplePlayer player;
	SimpleGame game;

	@FXML private Pane select_card_pane;
	@FXML private Pane select_resource_pane;
	@FXML private Pane waiting_pane;
	@FXML private Pane order_pane;
	@FXML private GridPane resource_grid;
	@FXML private HBox leader_card_array;

	@FXML private Text first_player_text;
	@FXML private Text second_player_text;
	@FXML private Text third_player_text;
	@FXML private Text fourth_player_text;

	@FXML private ImageView top1;
	@FXML private ImageView middle1;
	@FXML private ImageView middle2;
	@FXML private ImageView bottom1;
	@FXML private ImageView bottom2;
	@FXML private ImageView bottom3;

	@Override
	public void initialize(URL location, ResourceBundle resources){
		this.player = App.getMyPlayer();
		this.order = App.getSimpleGame().getOrder();

		// get updated everytime an ErrorMessage is received
		App.setErrorMessageObserver(this);

		setOrderPane();
		setChooseResources();
		initializeWarehouseDrop();
		chooseWhatToShowAndHide();
	}

	/**
	 * Hide and show panels based on many factors, mainly order of the Player, SoloGame or Persistence (the Player could have completed the setup partially or fully)
	 */
	private void chooseWhatToShowAndHide() {
		hideNode(order_pane);
		hideNode(select_card_pane);
		hideNode(select_resource_pane);
		hideNode(waiting_pane);

		// if not sologame, show the order of the Players
		if (order.size() != 1){
			showNode(order_pane);
		} else {
			// if the LeaderCards weren't discarded already
			if (player.getLeaderCards().size() > 2) {
				setLeaderCards();
				showNode(select_card_pane);
			} else {
				// if the setup is done wait until the PlayerBoardScene gets called
				showNode(waiting_pane);
			}
		}
	}

	/**
	 * Show the random order of Players
	 */
	private void setOrderPane() {
		first_player_text.setText("[1] " + order.get(0));

		if (order.size() >= 2){
			second_player_text.setText("[2] " + order.get(1));
		} 

		if (order.size() >= 3){
			third_player_text.setText("[3] " + order.get(2));
		} 

		if (order.size() >= 4){
			fourth_player_text.setText("[4] " + order.get(3));
		} 
	}

	/**
	 * Show the Player four or three LeaderCards to discard
	 */
	private void setLeaderCards() {
		ArrayList<LeaderCard> leader_cards = this.player.getLeaderCards();
		for (int i = 0; i < leader_cards.size(); i++) {
			ImageView leader_card_imageview = (ImageView) this.leader_card_array.getChildren().get(i);
			leader_card_imageview.setImage(new Image(leader_cards.get(i).getFrontPath()));

			// set the method to discard them
			final int index = i;
			leader_card_imageview.setOnMouseClicked(click -> leaderCardClick(click, index));
		}
	}

	/**
	 * Set the methods to drag resources in the Warehouse
	 */
	private void setChooseResources() {
		for (Node node: this.resource_grid.getChildren()) {
			((ImageView) node).setOnDragDetected(detected -> handleDragDetected(detected, (ImageView) node));
			((ImageView) node).setOnDragDone(done -> handleDragDone(done, (ImageView) node, true));
		}
	}

	/**
	 * Set the methods to drop the resources in the Warehouse
	 */
	private void initializeWarehouseDrop() {
		top1.setOnDragOver(over1 -> handleDragOver(over1, top1));
		middle1.setOnDragOver(over2 -> handleDragOver(over2, middle1));
		middle2.setOnDragOver(over3 -> handleDragOver(over3, middle2));
		bottom1.setOnDragOver(over4 -> handleDragOver(over4, bottom1));
		bottom2.setOnDragOver(over5 -> handleDragOver(over5, bottom2));
		bottom3.setOnDragOver(over6 -> handleDragOver(over6, bottom3));

		top1.setOnDragDropped(dropped1 -> handleDragDropped(dropped1, top1));
		middle1.setOnDragDropped(dropped2 -> handleDragDropped(dropped2, middle1));
		middle2.setOnDragDropped(dropped3 -> handleDragDropped(dropped3, middle2));
		bottom1.setOnDragDropped(dropped4 -> handleDragDropped(dropped4, bottom1));
		bottom2.setOnDragDropped(dropped5 -> handleDragDropped(dropped5, bottom2));
		bottom3.setOnDragDropped(dropped6 -> handleDragDropped(dropped6, bottom3));
	}

	/**
	 * Reverse the drag and drop if an ErrorMessage is received while choosing the Resources
	 */
	public void receivedErrorMessage() {
		reverseDragAndDrop();
		this.drag_and_drop_hashmap.clear();

		showNode(select_resource_pane);
		hideNode(waiting_pane);
	}

	/**
	 * Show the right panel after showing the order of the Players
	 */
	@FXML
	public void endOrder(){
		hideNode(order_pane);

		// if the LeaderCards weren't discarded already
		if (this.player.getLeaderCards().size() > 2) {
			setLeaderCards();
			showNode(select_card_pane);
		// if the Player has to choose Resources to put in their Warehouse
		} else if (!checkChooseResourceDone(this.player.numberOfResourcesInWarehouse())) {
			showNode(select_resource_pane);
		} else {
			showNode(waiting_pane);
		}
	}

	/**
	 * Set which LeaderCard to discard
	 *
	 * @param event the click on a LeaderCard
	 * @param index the index of the LeaderCard clicked
	 */
	@FXML
	public void leaderCardClick(MouseEvent event, int index){
		ImageView source = (ImageView) event.getSource();

		to_delete.add(player.getLeaderCards().get(index));
		source.setDisable(true);
		source.setOpacity(opacity_percent);

		// if all the LeaderCards are discarded, change panel
		if (checkLeader()){
			endLeaderScene();
		} 
	}

	/**
	 * @return true if the number of LeaderCard the Player has is 2
	 */
	private boolean checkLeader(){
		int number_of_leader_cards = this.player.getLeaderCards().size() - this.to_delete.size();
		return number_of_leader_cards == 2;
	}

	/**
	 * If the Player has discarded all the LeaderCards, change panel
	 */
	private void endLeaderScene(){
		DiscardLeaderMessage message;

		for (int i = 0; i < to_delete.size(); i++) {
			message = new DiscardLeaderMessage(to_delete.get(i));
			App.sendMessage(message);
		}

		hideNode(select_card_pane);

		// if the Player is the first one, it doesn't have to choose Resources
		if (this.player.getNickname().equals(this.order.get(0))){
			showNode(waiting_pane);
		// if the Player hasn't already chosen the Resources (in a Persistence Game)
		} else if (!checkChooseResourceDone(this.player.numberOfResourcesInWarehouse())) {
			showNode(select_resource_pane);
		} else {
			showNode(waiting_pane);
		}
	}

	/**
	 * Send the chosen Resources to the Server
	 */
	@FXML
	public void endResourcesScene(){
		// if the sum of the Player's Resources and the one that they chose are different than the supposed one, reverse the drag and drop
		if (!checkChooseResourceDone(this.drag_and_drop_hashmap.keySet().size() + this.player.numberOfResourcesInWarehouse())) {
			reverseDragAndDrop();
			return;
		}

		Storage storage = new Storage();
		for (ImageView key: this.drag_and_drop_hashmap.keySet()){
			ArrayList<Resource> resource_to_add = new ArrayList<Resource>();

			// get the name of the Resource from the userdata in the FXML
			resource_to_add.add(Resource.valueOf(key.getUserData().toString()));

			// select which shelf of the Warehouse to put the Resources in
			switch (drag_and_drop_hashmap.get(key).getParent().getId()){
				case "warehouse_top":
					storage.addToWarehouseTop(resource_to_add);
					break;
				case "warehouse_middle":
					storage.addToWarehouseMid(resource_to_add);
					break;
				case "warehouse_bottom":
					storage.addToWarehouseBot(resource_to_add);
					break;
			}
		}

		ChooseResourcesMessage message = new ChooseResourcesMessage(storage);
		App.sendMessage(message);

		hideNode(select_resource_pane);
		showNode(waiting_pane);
	}

	/**
	 * @param number_of_chosen_resources how many Resources the Player has or has chosen
	 * @return true if the Player has chosen the right amount of Resources
	 */
	private boolean checkChooseResourceDone(int number_of_chosen_resources) {
		switch(getMyOrder()) {
			case 0:
				return true;
			case 1:
			case 2:
				return number_of_chosen_resources == 1;
			case 3:
				return number_of_chosen_resources == 2;
			// unreachable
			default:
				return false;
		}
	}

	/**
	 * @return the position in the order array
	 */
	private int getMyOrder() {
		int i = 0;
		for (String nickname: this.order) {
			if (this.player.getNickname().equals(nickname)) {
				return i;
			}
			i++;
		}
		// unreachable
		return 0;
	}
}
