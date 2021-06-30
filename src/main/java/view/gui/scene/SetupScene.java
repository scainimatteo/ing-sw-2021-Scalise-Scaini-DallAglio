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

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

public class SetupScene extends SceneController implements Initializable {
	ArrayList<LeaderCard> to_delete = new ArrayList<LeaderCard>();
	ArrayList<String> order;
	double opacity_percent = 0.5;
	SimplePlayer player;
	SimpleGame game;

	@FXML private Pane select_card_pane;
	@FXML private Pane select_resource_pane;
	@FXML private Pane waiting_pane;
	@FXML private Pane order_pane;

	@FXML private ImageView leader_card_1;
	@FXML private ImageView leader_card_2;
	@FXML private ImageView leader_card_3;
	@FXML private ImageView leader_card_4;

	@FXML private Image leader_card_image_1;
	@FXML private Image leader_card_image_2;
	@FXML private Image leader_card_image_3;
	@FXML private Image leader_card_image_4;

	@FXML private Text first_player_text;
	@FXML private Text second_player_text;
	@FXML private Text third_player_text;
	@FXML private Text fourth_player_text;

	@FXML private ImageView coin1;
	@FXML private ImageView coin2;
	@FXML private ImageView servant1;
	@FXML private ImageView servant2;
	@FXML private ImageView shield1;
	@FXML private ImageView shield2;
	@FXML private ImageView stone1;
	@FXML private ImageView stone2;

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

		leader_card_image_1 = new Image(getClass().getResource(player.getLeaderCards().get(0).getFrontPath()).toString());
		leader_card_image_2 = new Image(getClass().getResource(player.getLeaderCards().get(1).getFrontPath()).toString());
		leader_card_image_3 = new Image(getClass().getResource(player.getLeaderCards().get(2).getFrontPath()).toString());
		leader_card_image_4 = new Image(getClass().getResource(player.getLeaderCards().get(3).getFrontPath()).toString());

		leader_card_1.setImage(leader_card_image_1);
		leader_card_2.setImage(leader_card_image_2);
		leader_card_3.setImage(leader_card_image_3);
		leader_card_4.setImage(leader_card_image_4);

		leader_card_1.setOnMouseClicked(click1 -> leaderCardClick(click1, 0));
		leader_card_2.setOnMouseClicked(click2 -> leaderCardClick(click2, 1));
		leader_card_3.setOnMouseClicked(click3 -> leaderCardClick(click3, 2));
		leader_card_4.setOnMouseClicked(click4 -> leaderCardClick(click4, 3));

		coin1.setOnDragDetected(detected1 -> handleDragDetected(detected1, coin1));
		coin2.setOnDragDetected(detected2 -> handleDragDetected(detected2, coin2));
		servant1.setOnDragDetected(detected3 -> handleDragDetected(detected3, servant1));
		servant2.setOnDragDetected(detected4 -> handleDragDetected(detected4, servant2));
		shield1.setOnDragDetected(detected5 -> handleDragDetected(detected5, shield1));
		shield2.setOnDragDetected(detected6 -> handleDragDetected(detected6, shield2));
		stone1.setOnDragDetected(detected7 -> handleDragDetected(detected7, stone1));
		stone2.setOnDragDetected(detected8 -> handleDragDetected(detected8, stone2));

		coin1.setOnDragDone(done1 -> handleDragDone(done1, coin1, true));
		coin2.setOnDragDone(done2 -> handleDragDone(done2, coin2, true));
		servant1.setOnDragDone(done3 -> handleDragDone(done3, servant1, true));
		servant2.setOnDragDone(done4 -> handleDragDone(done4, servant2, true));
		shield1.setOnDragDone(done5 -> handleDragDone(done5, shield1, true));
		shield2.setOnDragDone(done6 -> handleDragDone(done6, shield2, true));
		stone1.setOnDragDone(done7 -> handleDragDone(done7, stone1, true));
		stone2.setOnDragDone(done8 -> handleDragDone(done8, stone2, true));

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

		hideNode(select_resource_pane);
		hideNode(waiting_pane);

		if (order.size() != 1){
			showNode(order_pane);
			hideNode(select_card_pane);
		} else {
			hideNode(order_pane);
			showNode(select_card_pane);
		}
	}

	/**
	 * method that handles the click on the continue_button
	 */
	@FXML
	public void continue_button_click(){
		hideNode(order_pane);
		showNode(select_card_pane);
	}

	/**
	 * method that handles the click on a leader card
	 *
	 * @param event is the click on a leader card
	 * @param index is the index of the card clicked used to identify the leader card 
	 */
	@FXML
	public void leaderCardClick(MouseEvent event, int index){
		ImageView source = (ImageView) event.getSource();

		to_delete.add(player.getLeaderCards().get(index));
		source.setDisable(true);
		source.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	/**
	 * @return true if the number of leader card choosen is 2
	 */
	private boolean checkLeader(){
		return to_delete.size() == 2;
	}

	/**
	 * method called when the player choose 2 leader cards for the end of the leader card scene
	 */
	private void endLeaderScene(){
		DiscardLeaderMessage message;

		message = new DiscardLeaderMessage(to_delete.get(0));
		App.sendMessage(message);

		message = new DiscardLeaderMessage(to_delete.get(1));
		App.sendMessage(message);

		hideNode(select_card_pane);
		if (player.getNickname().equals(order.get(0))){
			showNode(waiting_pane);
		} else {
			showNode(select_resource_pane);
		}
	}

	/**
	 * method called with the button confirm in the resource scene to send the initial storage
	 */
	@FXML
	@SuppressWarnings("unchecked")
	public void endResourcesScene(){
		HashMap<String, String> resources_hashmap = new HashMap<String, String>();
		ArrayList<Resource> resource_to_add = new ArrayList<Resource>();
		ChooseResourcesMessage message;
		Storage storage = new Storage();
		String resize_string;
		int separator_index;

		for (int i = 0; i < drag_and_drop_arraylist.size(); i ++){
			resize_string = drag_and_drop_arraylist.get(i);
			resize_string = resize_string.substring(0, resize_string.length() - 1);
			separator_index = resize_string.indexOf("|");

			resources_hashmap.put(resize_string.substring(0, separator_index - 1), resize_string.substring(separator_index + 1));
		}

		for (String key : resources_hashmap.keySet()){
			resource_to_add.add(Resource.valueOf((key.substring(0, key.length() - 1).toUpperCase())));

			switch (resources_hashmap.get(key)){
				case "top":
					storage.addToWarehouseTop(resource_to_add);
					break;
				case "middle":
					storage.addToWarehouseMid(resource_to_add);
					break;
				case "bottom":
					storage.addToWarehouseBot(resource_to_add);
					break;
			}

			resource_to_add.clear();
		}

		message = new ChooseResourcesMessage(storage);
		App.sendMessage(message);

		drag_and_drop_arraylist.clear();

		hideNode(select_resource_pane);
		showNode(waiting_pane);
	}
}
