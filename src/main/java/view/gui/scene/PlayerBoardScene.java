package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.ProductionAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.controller.message.DiscardResourcesMessage;
import it.polimi.ingsw.controller.message.ActivateLeaderMessage;
import it.polimi.ingsw.controller.message.DiscardLeaderMessage;
import it.polimi.ingsw.controller.message.ProductionMessage;
import it.polimi.ingsw.controller.message.RearrangeMessage;
import it.polimi.ingsw.controller.message.EndTurnMessage;
import it.polimi.ingsw.controller.message.StoreMessage;
import it.polimi.ingsw.controller.message.PayMessage;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.view.gui.scene.OtherPlayerScene;
import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.scene.GameScene;
import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.*;

import it.polimi.ingsw.util.observer.ErrorMessageObserver;
import it.polimi.ingsw.util.observer.ViewUpdateObserver;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.net.URL;

public class PlayerBoardScene extends SceneController implements ViewUpdateObserver, ErrorMessageObserver, Initializable {
	ArrayList<Resource> all_resources;
	// contains the list of all the DevelopmentCardSlots activated
	ArrayList<Integer> development_card_productions;
	// contains the Resources for the Production base
	ArrayList<Resource> set_resources; 
	// contains the Resources decided by the ProductionAbilities of the LeaderCards
	ArrayList<Resource> leader_card_output;
	// contains the number of Resources decided by the WhiteMarblesAbilities of the LeaderCards
	ArrayList<Integer> white_marbles_numbers;

	@FXML private GridPane faith_track;
	@FXML private HBox development_card_slot;
	@FXML private Pane cost_resources_pane;
	@FXML private Pane leader_card_pane;
	@FXML private HBox leader_card_array;

	@FXML private Button view_player2_button;
	@FXML private Button view_player3_button;
	@FXML private Button view_player4_button;
	@FXML private ToggleButton leaders_button;

	@FXML private Text last_turn_text;
	@FXML private ImageView last_token;

	@FXML private StackPane base_production;
	@FXML private ImageView input1;
	@FXML private ImageView input2;
	@FXML private ImageView output;

	@FXML private Button activate_prod_button;

	@FXML private ImageView top1;
	@FXML private ImageView middle1;
	@FXML private ImageView middle2;
	@FXML private ImageView bottom1;
	@FXML private ImageView bottom2;
	@FXML private ImageView bottom3;
	@FXML private HBox warehouse_top;
	@FXML private HBox warehouse_middle;
	@FXML private HBox warehouse_bottom;

	@FXML private ImageView coin_sprite;
	@FXML private ImageView shield_sprite;
	@FXML private ImageView servant_sprite;
	@FXML private ImageView stone_sprite;
	@FXML private Text coin_amount;
	@FXML private Text shield_amount;
	@FXML private Text stone_amount;
	@FXML private Text servant_amount;

	@FXML private HBox extra_space_ability_1;
	@FXML private HBox extra_space_ability_2;

	@FXML private HBox cost_box;
	@FXML private HBox gain_box;
	@FXML private Text pay_or_store_text;

	@FXML private ImageView tile1;
	@FXML private ImageView tile2;
	@FXML private ImageView tile3;

	@FXML private StackPane first_slot;
	@FXML private StackPane second_slot;
	@FXML private StackPane third_slot;

	public PlayerBoardScene() {
		this.development_card_productions = new ArrayList<Integer>();
		this.all_resources = new ArrayList<Resource>(Arrays.asList(Resource.COIN, Resource.SERVANT, Resource.SHIELD, Resource.STONE));
		this.set_resources = new ArrayList<Resource>(Arrays.asList(null, null, null));
		this.leader_card_output = new ArrayList<Resource>(Arrays.asList(null, null));
		this.white_marbles_numbers = new ArrayList<Integer>(Arrays.asList(0, 0));
	}

	/**
	 * INITIALIZATION
	 */

	@Override
	public void initialize(URL location, ResourceBundle resouces){
		this.updateView();

		// get updated everytime the View gets updated
		App.setViewUpdateObserver(this);

		// get updated everytime an ErrorMessage is received
		App.setErrorMessageObserver(this);

		initializeBaseProduction();

		// set the Drag and Drop
		initializeWarehouseDragAndDrop();
		initializeStrongBoxDragAndDrop();
		initializeExtraSpaceDragAndDrop();
		initializeCostBoxDragAndDrop();
		initializeGainBoxDragAndDrop();

		hideNode(leader_card_pane);

		initializeOtherPlayersButton();
	}

	/**
	 * Set the onMouseClicked methods for the BaseProduction
	 */
	private void initializeBaseProduction() {
		input1.setOnMouseClicked(click -> chooseBaseProductionResource(input1, 0));
		input2.setOnMouseClicked(click -> chooseBaseProductionResource(input2, 1));
		output.setOnMouseClicked(click -> chooseBaseProductionResource(output, 2));
	}

	/**
	 * Set the methods to drag and drop the Resources in and out of the Warehouse
	 */
	private void initializeWarehouseDragAndDrop() {
		// DRAG
		top1.setOnDragDetected(detected -> handleDragDetected(detected, top1));
		middle1.setOnDragDetected(detected -> handleDragDetected(detected, middle1));
		middle2.setOnDragDetected(detected -> handleDragDetected(detected, middle2));
		bottom1.setOnDragDetected(detected -> handleDragDetected(detected, bottom1));
		bottom2.setOnDragDetected(detected -> handleDragDetected(detected, bottom2));
		bottom3.setOnDragDetected(detected -> handleDragDetected(detected, bottom3));

		top1.setOnDragDone(done -> handleDragDone(done, top1, false));
		middle1.setOnDragDone(done -> handleDragDone(done, middle1, false));
		middle2.setOnDragDone(done -> handleDragDone(done, middle2, false));
		bottom1.setOnDragDone(done -> handleDragDone(done, bottom1, false));
		bottom2.setOnDragDone(done -> handleDragDone(done, bottom2, false));
		bottom3.setOnDragDone(done -> handleDragDone(done, bottom3, false));

		// DROP
		top1.setOnDragOver(over -> handleDragOver(over, top1));
		middle1.setOnDragOver(over -> handleDragOver(over, middle1));
		middle2.setOnDragOver(over -> handleDragOver(over, middle2));
		bottom1.setOnDragOver(over -> handleDragOver(over, bottom1));
		bottom2.setOnDragOver(over -> handleDragOver(over, bottom2));
		bottom3.setOnDragOver(over -> handleDragOver(over, bottom3));

		top1.setOnDragDropped(dropped -> handleStoreInWarehouse(dropped, top1));
		middle1.setOnDragDropped(dropped -> handleStoreInWarehouse(dropped, middle1));
		middle2.setOnDragDropped(dropped -> handleStoreInWarehouse(dropped, middle2));
		bottom1.setOnDragDropped(dropped -> handleStoreInWarehouse(dropped, bottom1));
		bottom2.setOnDragDropped(dropped -> handleStoreInWarehouse(dropped, bottom2));
		bottom3.setOnDragDropped(dropped -> handleStoreInWarehouse(dropped, bottom3));
	}

	/**
	 * Set the methods to drag and drop the Resources in and out of the StrongBox
	 */
	private void initializeStrongBoxDragAndDrop() {
		// DRAG
		coin_sprite.setOnDragDetected(detected -> handleDragDetected(detected, coin_sprite));
		shield_sprite.setOnDragDetected(detected -> handleDragDetected(detected, shield_sprite));
		servant_sprite.setOnDragDetected(detected -> handleDragDetected(detected, servant_sprite));
		stone_sprite.setOnDragDetected(detected -> handleDragDetected(detected, stone_sprite));

		// DROP
		coin_sprite.setOnDragDone(done -> handleDragDone(done, coin_sprite, false));
		shield_sprite.setOnDragDone(done -> handleDragDone(done, shield_sprite, false));
		servant_sprite.setOnDragDone(done -> handleDragDone(done, servant_sprite, false));
		stone_sprite.setOnDragDone(done -> handleDragDone(done, stone_sprite, false));

	}

	/**
	 * Set the methods to drag and drop the Resources in and out of the ExtraSpaceAbility
	 */
	private void initializeExtraSpaceDragAndDrop() {
		for (Node node: this.extra_space_ability_1.getChildren()) {
			ImageView extra_space_image_1 = (ImageView) node;
			extra_space_image_1.setOnDragDetected(detected -> handleDragDetected(detected, extra_space_image_1));
			extra_space_image_1.setOnDragDone(done -> handleDragDone(done, extra_space_image_1, false));
			extra_space_image_1.setOnDragOver(over -> handleDragOver(over, extra_space_image_1));
			extra_space_image_1.setOnDragDropped(dropped -> handleStoreInExtraSpace(dropped, extra_space_image_1, 0));
		}

		for (Node node: this.extra_space_ability_2.getChildren()) {
			ImageView extra_space_image_2 = (ImageView) node;
			extra_space_image_2.setOnDragDetected(detected -> handleDragDetected(detected, extra_space_image_2));
			extra_space_image_2.setOnDragDone(done -> handleDragDone(done, extra_space_image_2, false));
			extra_space_image_2.setOnDragOver(over -> handleDragOver(over, extra_space_image_2));
			extra_space_image_2.setOnDragDropped(dropped -> handleStoreInExtraSpace(dropped, extra_space_image_2, 1));
		}
	}

	/**
	 * Set the methods to drag and drop the Resources in and out of the cost box
	 */
	private void initializeCostBoxDragAndDrop() {
		for (Node node: this.cost_box.getChildren()) {
			node.setOnDragOver(over -> handleDragOver(over, (ImageView) node));
			node.setOnDragDropped(dropped -> handlePay(dropped, (ImageView) node));
		}
	}

	/**
	 * Set the methods to drag and drop the Resources in and out of the gain box
	 */
	private void initializeGainBoxDragAndDrop(){
		for (Node node : this.gain_box.getChildren()){
			node.setOnDragDetected(detected -> handleDragDetected(detected, (ImageView) node));
			node.setOnDragDone(done -> handleDragDone(done, (ImageView) node, true));
		}
	}

	/**
	 * Handle the drop on a cost box ImageView and send a PayMessage to the Server
	 *
	 * @param event the DragEvent raised
	 * @param target the ImageView on which the Image of the Resource is dropped
	 */
	private void handlePay(DragEvent event, ImageView target){
		handleDragDropped(event, target);

		SimplePlayer player = App.getMyPlayer();
		Storage storage = new Storage();
		Resource dropped_resource;

		for (ImageView key: drag_and_drop_hashmap.keySet()){
			switch (key.getParent().getId()){
				case "warehouse_top":
					dropped_resource = player.getTopResource().get(0);
					storage.addToWarehouseTop(new ArrayList<Resource>(Arrays.asList(dropped_resource)));
					break;
				case "warehouse_middle":
					dropped_resource = player.getMiddleResources().get(0);
					storage.addToWarehouseMid(new ArrayList<Resource>(Arrays.asList(dropped_resource)));
					break;
				case "warehouse_bottom":
					dropped_resource = player.getBottomResources().get(0);
					storage.addToWarehouseBot(new ArrayList<Resource>(Arrays.asList(dropped_resource)));
					break;
				case "strongbox":
					dropped_resource = Resource.valueOf((String) key.getUserData());
					storage.addToStrongbox(new ArrayList<Resource>(Arrays.asList(dropped_resource)));
					break;
				case "extra_space_ability_1":
					dropped_resource = ((ExtraSpaceAbility) player.getLeaderCards().get(0).getAbility()).getResourceType();
					storage.addToExtraspace(new ArrayList<Resource>(Arrays.asList(dropped_resource)));
					break;
				case "extra_space_ability_2":
					dropped_resource = ((ExtraSpaceAbility) player.getLeaderCards().get(1).getAbility()).getResourceType();
					storage.addToExtraspace(new ArrayList<Resource>(Arrays.asList(dropped_resource)));
					break;
			}
		}

		PayMessage message = new PayMessage(storage);
		App.sendMessage(message);
		this.drag_and_drop_hashmap.clear();
		this.updateView();
	}

	/**
	 * Handle the drop on a Warehouse ImageView and send a StoreMessage to the Server
	 *
	 * @param event the DragEvent raised
	 * @param target the ImageView on which the Image of the Resource is dropped
	 */
	private void handleStoreInWarehouse(DragEvent event, ImageView target){
		handleDragDropped(event, target);

		Storage warehouse_storage = new Storage();

		try {
			for (ImageView key: this.drag_and_drop_hashmap.keySet()){
				ArrayList<Resource> resource_to_add = new ArrayList<Resource>();

				// get the name of the Resource from the userdata in the FXML
				resource_to_add.add(App.getTurn().getProducedResources().get(Integer.parseInt((String) key.getUserData())));

				// select which shelf of the Warehouse to put the Resources in
				switch (drag_and_drop_hashmap.get(key).getParent().getId()){
					case "warehouse_top":
						warehouse_storage.addToWarehouseTop(resource_to_add);
						break;
					case "warehouse_middle":
						warehouse_storage.addToWarehouseMid(resource_to_add);
						break;
					case "warehouse_bottom":
						warehouse_storage.addToWarehouseBot(resource_to_add);
						break;
					default:
						reverseDragAndDrop();
						return;
				}
			}
		} catch (NumberFormatException e){
			// something not allowed was trying to insert in the warehouse
			this.updateView();
			return;
		}

		StoreMessage message = new StoreMessage(warehouse_storage);
		App.sendMessage(message);
		this.drag_and_drop_hashmap.clear();
	}

	/**
	 * @return true if all the Imageview in the gain box are empty
	 */
	private boolean checkGainBox(){
		for (Node node : this.gain_box.getChildren()){
			if (((ImageView) node).getImage() != null){
				return false;
			} 
		}

		return true;
	}

	/**
	 * Handle the drop on a ExtraSpaceAbility ImageView and send a StoreMessage to the Server
	 *
	 * @param event the DragEvent raised
	 * @param target the ImageView on which the Image of the Resource is dropped
	 * @param index the index of the LeaderCard in the LeaderCard array of the Player
	 */
	private void handleStoreInExtraSpace(DragEvent event, ImageView target, int index){
		handleDragDropped(event, target);

		Storage extra_space_storage = new Storage();

		try {
			for (ImageView key: this.drag_and_drop_hashmap.keySet()){
				ArrayList<Resource> resources_to_add = new ArrayList<Resource>();

				// get the name of the Resource from the userdata in the FXML
				Resource new_resource = App.getTurn().getProducedResources().get(Integer.parseInt((String) key.getUserData()));

				resources_to_add.add(new_resource);
				extra_space_storage.addToExtraspace(resources_to_add);
			}
		} catch (NumberFormatException e){
			// something not allowed was trying to insert in the ExtraSpaceAbility
			this.updateView();
			return;
		}

		StoreMessage message = new StoreMessage(extra_space_storage);
		App.sendMessage(message);
		this.drag_and_drop_hashmap.clear();
	}

	/**
	 * Set the buttons that allow the Player to see other Players boards
	 *
	 * If it's not a SoloGame, don't show the SoloActionTokens
	 */
	private void initializeOtherPlayersButton() {
		ArrayList<SimplePlayer> players = App.getSimplePlayers();

		if (players.size() != 1){
			hideNode(last_token);
		} 

		ArrayList<SimplePlayer> other_players = getOtherPlayers(players);
		if (players.size() == 1){
			hideNode(view_player2_button);
		} else {
			view_player2_button.setOnMouseClicked(click -> changeSceneToOtherPlayer(other_players.get(0).getNickname()));
			view_player2_button.setText(other_players.get(0).getNickname());
		}

		if (players.size() <= 2){
			hideNode(view_player3_button);
		} else {
			view_player3_button.setOnMouseClicked(click -> changeSceneToOtherPlayer(other_players.get(1).getNickname()));
			view_player3_button.setText(other_players.get(1).getNickname());
		}

		if (players.size() <= 3){
			hideNode(view_player4_button);
		} else {
			view_player4_button.setOnMouseClicked(click -> changeSceneToOtherPlayer(other_players.get(2).getNickname()));
			view_player4_button.setText(other_players.get(2).getNickname());
		}
	}

	/**
	 * @return an array of all the other Players
	 */
	private ArrayList<SimplePlayer> getOtherPlayers(ArrayList<SimplePlayer> players) {
		ArrayList<SimplePlayer> other_players = new ArrayList<SimplePlayer>();
		for (SimplePlayer p: players) {
			if (!p.getNickname().equals(App.getMyPlayer().getNickname())) {
				other_players.add(p);
			}
		}
		return other_players;
	}

	/**
	 * VIEW SETTERS
	 *
	 * Set all the ImageViews and methods of the board, using the ViewUpdateObserver
	 *
	 * This method gets called once at the start and everytime there's a ViewUpdate (when the Model is changed)
	 */
	public void updateView(){
		SimpleWarehouse warehouse = App.getMyPlayer().getWarehouse();
		HashMap<Resource, Integer> strongbox = App.getMyPlayer().getStrongbox();
		SimpleDevelopmentCardSlot development_card_slot = App.getMyPlayer().getDevelopmentCardsSlots();
		ArrayList<LeaderCard> leader_cards = App.getMyPlayer().getLeaderCards();
		int marker_position = App.getMyPlayer().getMarker().getPosition();
		Tile[] tiles = App.getMyPlayer().getReports();
		ArrayList<Resource> to_pay = App.getTurn().getRequiredResources();
		ArrayList<Resource> to_store = App.getTurn().getProducedResources();
		boolean is_last_turn = App.getTurn().isFinal();

		resetFaithTrack();
		resetTurnResources();
		resetWarehouse();
		resetExtraSpace();

		// SOLOGAME
		if (App.isSoloGame()) {
			SimpleSoloGame game = (SimpleSoloGame) App.getSimpleGame();
			SimpleSoloPlayer player = (SimpleSoloPlayer) App.getMyPlayer();
			setLastToken(game.getLastToken());
			setBlackMarker(player.getBlackMarkerPosition());
		}

		// PLAYER
		setWarehouse(warehouse);
		setStrongbox(strongbox);
		setDevelopmentCardSlot(development_card_slot);
		setLeaderCards(leader_cards);
		setFaithMarker(marker_position);
		setTiles(tiles);

		// TURN
		setTurnResources(to_pay, to_store);
		setLastTurn(is_last_turn);
	}

	private void setWarehouse(SimpleWarehouse warehouse){
		ArrayList<Resource> top = warehouse.getTopResource();
		ArrayList<Resource> mid = warehouse.getMiddleResources();
		ArrayList<Resource> bot = warehouse.getBottomResources();

		for (Resource res : top){
			this.top1.setImage(getImageFromResource(res));
		}

		for (int i = 0; i < this.warehouse_middle.getChildren().size(); i++){
			ImageView middle_image = (ImageView) this.warehouse_middle.getChildren().get(i);
			try {
				middle_image.setImage(getImageFromResource(mid.get(i)));
			} catch (IndexOutOfBoundsException e) {
			}
		}

		for (int i = 0; i < this.warehouse_bottom.getChildren().size(); i++){
			ImageView bottom_image = (ImageView) this.warehouse_bottom.getChildren().get(i);
			try {
				bottom_image.setImage(getImageFromResource(bot.get(i)));
			} catch (IndexOutOfBoundsException e) {
			}
		}
	}

	private void setStrongbox(HashMap<Resource, Integer> strongbox){
		for (Resource res : strongbox.keySet()){
			switch (res){
				case COIN:
					coin_amount.setText("x" + String.valueOf(strongbox.get(Resource.COIN)));
					break;
				case SERVANT:
					servant_amount.setText("x" + String.valueOf(strongbox.get(Resource.SERVANT)));
					break;
				case SHIELD:
					shield_amount.setText("x" + String.valueOf(strongbox.get(Resource.SHIELD)));
					break;
				case STONE:
					stone_amount.setText("x" + String.valueOf(strongbox.get(Resource.STONE)));
					break;
				default:
					break;
			}
		}
	}

	private void setDevelopmentCardSlot(SimpleDevelopmentCardSlot development_card_slot){
		ArrayList<DevelopmentCard> first_column = development_card_slot.getFirstColumn();
		ArrayList<DevelopmentCard> second_column = development_card_slot.getSecondColumn();
		ArrayList<DevelopmentCard> third_column = development_card_slot.getThirdColumn();

		// first slot
		for (int i = 0; i < this.first_slot.getChildren().size(); i++) {
			int size = this.first_slot.getChildren().size() - 1;
			((ImageView) this.first_slot.getChildren().get(size - i)).setImage(getDevelopmentCardPath(first_column, i));
			this.first_slot.getChildren().get(size - i).setOnMouseClicked(null);
		}

		// second slot
		for (int i = 0; i < this.second_slot.getChildren().size(); i++) {
			int size = this.second_slot.getChildren().size() - 1;
			((ImageView) this.second_slot.getChildren().get(size - i)).setImage(getDevelopmentCardPath(second_column, i));
			this.second_slot.getChildren().get(size - i).setOnMouseClicked(null);
		}

		// third slot
		for (int i = 0; i < this.third_slot.getChildren().size(); i++) {
			int size = this.third_slot.getChildren().size() - 1;
			((ImageView) this.third_slot.getChildren().get(size - i)).setImage(getDevelopmentCardPath(third_column, i));
			this.third_slot.getChildren().get(size - i).setOnMouseClicked(null);
		}

		// set methods to activate the DevelopmentCards
		for (int i = 0; i < this.development_card_slot.getChildren().size(); i++) {
			List<Node> nodes = ((StackPane) this.development_card_slot.getChildren().get(i)).getChildren();

			final int slot = i;
			// the only activable DevelopmentCard is the one on top
			if (((ImageView) nodes.get(2)).getImage() != null) {
				nodes.get(2).setOnMouseClicked(click -> setDevelopmentCardProduction((ImageView) nodes.get(2), slot));
			} else if (((ImageView) nodes.get(1)).getImage() != null) {
				nodes.get(1).setOnMouseClicked(click -> setDevelopmentCardProduction((ImageView) nodes.get(1), slot));
			} else if (((ImageView) nodes.get(0)).getImage() != null) {
				nodes.get(0).setOnMouseClicked(click -> setDevelopmentCardProduction((ImageView) nodes.get(0), slot));
			}
		}
	}

	private void setLeaderCards(ArrayList<LeaderCard> leader_cards){
		int i = 0;
		for (Node node: this.leader_card_array.getChildren()) {
			if (i < leader_cards.size()) {
				VBox leader_card = (VBox) node;
				StackPane leader_card_stackpane = (StackPane) leader_card.getChildren().get(0);
				ImageView leader_card_image = (ImageView) leader_card_stackpane.getChildren().get(0);
				leader_card_image.setImage(new Image(leader_cards.get(i).getFrontPath()));

				setLeaderCardButtons(leader_card, leader_cards.get(i), i);
				setLeaderCardAbility(leader_card_stackpane, i);
				showNode(node);
				i++;
			} else {
				hideNode(node);
			}
		}
	}

	private void setLeaderCardButtons(VBox leader_card, LeaderCard player_leader_card, int index) {
		// set the activate and discard buttons under each card
		leader_card.getChildren().get(1).setOnMouseClicked(click -> handleActivateLeaderCard(index));
		leader_card.getChildren().get(2).setOnMouseClicked(click -> handleDiscardLeaderCard(index));

		// remove the Activate button if the card is activated
		if (player_leader_card.isActive()) {
			hideNode(leader_card.getChildren().get(1));
		}
	}

	private void setLeaderCardAbility(StackPane leader_card, int index) {
		LeaderCard player_leader_card = App.getMyPlayer().getLeaderCards().get(index);

		// if the LeaderCard has an ExtraSpaceAbility
		if (player_leader_card.isActive() && player_leader_card.getAbility().checkAbility(new ExtraSpaceAbility(null))) {
			// hide the ProductionAbility and WhiteMarblesAbility modifier
			hideNode(leader_card.getChildren().get(2));
			hideNode(leader_card.getChildren().get(3));

			setExtraSpaceAbility((HBox) leader_card.getChildren().get(1), player_leader_card);
		// if the LeaderCard has a ProductionAbility
		} else if (player_leader_card.isActive() && player_leader_card.getAbility().checkAbility(new ProductionAbility(null, null))) {
			// hide the ExtraSpaceAbility and WhiteMarblesAbility modifier
			hideNode(leader_card.getChildren().get(1));
			hideNode(leader_card.getChildren().get(3));

			setProductionAbility((ImageView) leader_card.getChildren().get(2), index);
		// if the LeaderCard has a WhiteMarblesAbility
		} else if (player_leader_card.isActive() && player_leader_card.getAbility().checkAbility(new WhiteMarblesAbility(null))) {
			// hide the ExtraSpaceAbility and ProductionAbility modifier
			hideNode(leader_card.getChildren().get(1));
			hideNode(leader_card.getChildren().get(2));

			setWhiteMarbleAbility((Text) leader_card.getChildren().get(3), index);
		} else {
			// hide every LeaderCard modifier
			hideNode(leader_card.getChildren().get(1));
			hideNode(leader_card.getChildren().get(2));
			hideNode(leader_card.getChildren().get(3));
		}
	}

	private void setExtraSpaceAbility(HBox extra_space, LeaderCard player_leader_card) {
		Resource extra_space_resource = ((ExtraSpaceAbility) player_leader_card.getAbility()).getResourceType();
		int number_of_resources = ((ExtraSpaceAbility) player_leader_card.getAbility()).peekResources();

		// set the images of the Resources contained in the LeaderCard
		for (int i = 0; i < number_of_resources; i++) {
			ImageView extra_space_image = (ImageView) extra_space.getChildren().get(i);
			extra_space_image.setImage(new Image(extra_space_resource.getPath()));

			showNode(extra_space);
		}
	}

	private void setProductionAbility(ImageView production_image, int index) {
		production_image.setOnMouseClicked(click -> chooseProductionAbilityResource(production_image, index));
		showNode(production_image);
	}

	private void setWhiteMarbleAbility(Text white_marbles_text, int index) {
		white_marbles_text.setOnMouseClicked(click -> chooseWhiteMarblesNumber(white_marbles_text, index));
		showNode(white_marbles_text);
	}

	private void setFaithMarker(int faith_marker){
		int i = 0;
		for (Node cell: faith_track.getChildren()) {
			if (i == faith_marker) {
				((ImageView) cell).setImage(new Image("/images/tokens/faithtrack/faith_marker.png"));
				break;
			}
			i++;
		}
	}

	private void setTiles(Tile[] tiles){
		if (tiles[0].isActive()) {
			this.tile1.setImage(new Image("/images/tokens/faithtrack/pope_favor1_front.png"));
		}

		if (tiles[1].isActive()) {
			this.tile2.setImage(new Image("/images/tokens/faithtrack/pope_favor2_front.png"));
		}

		if (tiles[2].isActive()) {
			this.tile3.setImage(new Image("/images/tokens/faithtrack/pope_favor3_front.png"));
		}
	}

	private void setBlackMarker(int black_marker){
		int i = 0;
		for (Node cell: faith_track.getChildren()) {
			if (i == black_marker) {
				((ImageView) cell).setImage(new Image("/images/tokens/sologame/black_marker.png"));
				break;
			}
			i++;
		}
	}

	private void setLastToken(SoloActionToken last_token){
		if (last_token != null) {
			this.last_token.setImage(new Image(last_token.getPath()));
		} else {
			this.last_token.setImage(new Image("/images/tokens/sologame/lorenzo.png"));
		}
	}

	private void setTurnResources(ArrayList<Resource> to_pay, ArrayList<Resource> to_store) {
		String active_player = App.getTurn().getNickname();

		if (!to_pay.isEmpty() && App.getMyPlayer().getNickname().equals(active_player)) {
			setPayResources(to_pay);
		} else if (!to_store.isEmpty() && App.getMyPlayer().getNickname().equals(active_player)) {
			setStoreResources(to_store);
		} else {
			hideNode(cost_resources_pane);
		}
	}

	private void setPayResources(ArrayList<Resource> to_pay) {
		showNode(cost_resources_pane);
		showNode(cost_box);
		hideNode(gain_box);

		pay_or_store_text.setText("Drop these resources here to pay the cost");

		List<Node> hbox_nodes = this.cost_box.getChildren();
		for (int i = 0; i < hbox_nodes.size(); i++){
			try{
				ImageView res_view = (ImageView) hbox_nodes.get(i);
				res_view.setImage(new Image(to_pay.get(i).getPath()));
			} catch (IndexOutOfBoundsException e){
				break;
			}
		}
	}

	private void setStoreResources(ArrayList<Resource> to_store) {
		showNode(cost_resources_pane);
		showNode(gain_box);
		hideNode(cost_box);

		pay_or_store_text.setText("Drag these resources away to store your gains");

		List<Node> hbox_nodes = this.gain_box.getChildren();
		for (int i = 0; i < hbox_nodes.size(); i++){
			try{
				ImageView res_view = (ImageView) hbox_nodes.get(i);
				res_view.setImage(new Image(to_store.get(i).getPath()));
			} catch (IndexOutOfBoundsException e){
				break;
			}
		}
	}

	private void setLastTurn(boolean is_last_turn){
		if (is_last_turn) {
			showNode(this.last_turn_text);
		} else {
			hideNode(this.last_turn_text);
		}
	}

	/**
	 * Set all the ImageViews of the FaithTrack as null
	 */
	private void resetFaithTrack() {
		for (Node cell: faith_track.getChildren()) {
			((ImageView) cell).setImage(null);
		}
	}

	/**
	 * Set all the ImageViews of the TurnResources as null
	 */
	private void resetTurnResources() {
		for (int i = 0; i < this.cost_box.getChildren().size(); i++) {
			((ImageView) this.cost_box.getChildren().get(i)).setImage(null);
		}

		for (int i = 0; i < this.gain_box.getChildren().size(); i++) {
			((ImageView) this.gain_box.getChildren().get(i)).setImage(null);
		}
	}

	/**
	 * Set all the ImageViews of the Warehouse as null
	 */
	private void resetWarehouse(){
		for (int i = 0; i < this.warehouse_top.getChildren().size(); i++){
			ImageView top_image = (ImageView) this.warehouse_top.getChildren().get(i);
			top_image.setImage(null);
		}

		for (int i = 0; i < this.warehouse_middle.getChildren().size(); i++){
			ImageView middle_image = (ImageView) this.warehouse_middle.getChildren().get(i);
			middle_image.setImage(null);
		}

		for (int i = 0; i < this.warehouse_bottom.getChildren().size(); i++){
			ImageView bottom_image = (ImageView) this.warehouse_bottom.getChildren().get(i);
			bottom_image.setImage(null);
		}
	}

	/**
	 * Set all the ImageViews of the ExtraSpaceAbilities as null
	 */
	private void resetExtraSpace(){
		for (Node node: this.extra_space_ability_1.getChildren()) {
			ImageView extra_space_image_1 = (ImageView) node;
			extra_space_image_1.setImage(null);
		}

		for (Node node: this.extra_space_ability_2.getChildren()) {
			ImageView extra_space_image_2 = (ImageView) node;
			extra_space_image_2.setImage(null);
		}
	}

	/**
	 * Get an Image with the index's element of the column array
	 *
	 * @param column the ArrayList of DevelopmentCard to choose from
	 * @param index the index of the DevelopmentCard to get the Image of
	 * @return the new Image, null if the index's element doesn't exist
	 */
	private Image getDevelopmentCardPath(ArrayList<DevelopmentCard> column, int index) {
		try {
			return new Image(column.get(index).getPath());
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * @param res the Resource to get the Image of
	 * @return the Image representing res, null if res was null
	 */
	private Image getImageFromResource(Resource res) {
		if (res == null) {
			return null;
		}
		
		return new Image(res.getPath());
	}

	/**
	 * Reset the View after if ErrorMessage is received
	 */
	public void receivedErrorMessage() {
		this.updateView();
	}

	/**
	 * BUTTON HANDLERS
	 */

	/**
	 * Show the GameScene
	 */
	public void changeSceneToGame(){
		// remove the Scene from the array of ViewUpdateObservers and ErrorMessageObserver to save memory
		App.removeViewUpdateObserver(this);
		App.removeErrorMessageObserver(this);

		// send the GameScene the array of Resources chosen by the WhiteMarblesAbilities
		new GameScene(createWhiteMarblesResources()).changeScene("/fxml/gamescene.fxml");
	}

	/**
	 * Create an array of Resources as chosen by the WhiteMarblesAbility
	 */
	private ArrayList<Resource> createWhiteMarblesResources() {
		ArrayList<Resource> white_marbles_resources = new ArrayList<Resource>();

		for (int i = 0; i < this.white_marbles_numbers.size(); i++) {
			// if the LeaderCard doesn't have a WhiteMarblesAbility or if it's not active
			if (this.white_marbles_numbers.get(i) == 0) {
				continue;
			}

			// add to the Resources array the ones chosen by the Player
			WhiteMarblesAbility white_marbles = (WhiteMarblesAbility) App.getMyPlayer().getLeaderCards().get(i).getAbility();
			for (int j = 0; j < this.white_marbles_numbers.get(i); j++) {
				white_marbles_resources.add(white_marbles.getResourceType());
			}
		}

		return white_marbles_resources;
	}

	/**
	 * Show the Board of the other Players
	 */
	public void changeSceneToOtherPlayer(String other_player_nickname){
		// remove the Scene from the array of ViewUpdateObservers and ErrorMessageObserver to save memory
		App.removeViewUpdateObserver(this);
		App.removeErrorMessageObserver(this);
		new OtherPlayerScene(other_player_nickname).changeScene("/fxml/otherplayerscene.fxml");
	}

	/**
	 * Select a DevelopmentCard as a Production
	 */
	public void setDevelopmentCardProduction(ImageView production, int slot){
		if (!development_card_productions.contains(slot)){
			development_card_productions.add(slot);
			production.setOpacity(0.50);
		} else {
			development_card_productions.remove(slot);
			production.setOpacity(1);
		}
	}

	/**
	 * Toggle the appeareance of the LeaderCard pane
	 */
	public void showOrHideLeaderCards(){
		if (!this.leaders_button.isSelected()){
			showNode(leader_card_pane);
			hideNode(development_card_slot);
		} else {
			hideNode(leader_card_pane);
			showNode(development_card_slot);
		}
	}

	/**
	 * Set the Resource images in the base Production, looping between all the possible choices
	 *
	 * @param source the ImageView of the Production
	 * @param index the index of the source in the set_resources array
	 */
	public void chooseBaseProductionResource(ImageView source, int index){
		// if there's no Resource set, use the first one
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			this.set_resources.set(index, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(this.set_resources.get(index));

			// if the Resource set is not the last one, use the consecutive one
			if (resource_pos != this.all_resources.size() - 1){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				this.set_resources.set(index, all_resources.get(resource_pos + 1));
			// if the Resource set is the last one, loop back to null
			} else {
				source.setImage(null);
				this.set_resources.set(index, null);
			}
		}
	}

	/**
	 * Set the Resource images in the ProductionAbility, looping between all the possible choices
	 *
	 * @param source the ImageView of the Production
	 * @param index the index of the LeaderCard in the leader_card_output array
	 */
	public void chooseProductionAbilityResource(ImageView source, int index){
		// if there's no Resource set, use the first one
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			this.leader_card_output.set(index, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(this.leader_card_output.get(index));

			// if the Resource set is not the last one, use the consecutive one
			if (resource_pos != all_resources.size() - 1){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				this.leader_card_output.set(index, all_resources.get(resource_pos + 1));
			// if the Resource set is the last one, loop back to null
			} else {
				source.setImage(null);
				this.leader_card_output.set(index, null);
			}
		}
	}

	/**
	 * Send a ProductionMessage with all the ProductionInterfaces selected by the Player
	 */
	public void activateProductions(){
		ArrayList<ProductionInterface> productions = new ArrayList<ProductionInterface>();

		ArrayList<Resource> input = new ArrayList<Resource>(Arrays.asList(set_resources.get(0), set_resources.get(1)));
		ArrayList<Resource> output = new ArrayList<Resource>(Arrays.asList(set_resources.get(2)));

		// if all the elements of the base production are set, activate it
		if (checkBaseProductionSet()){
			Production base_production = new Production(input, output);
			productions.add(base_production);
		} 

		// activate every DevelopmentCard selected
		for (Integer slot: this.development_card_productions){
			productions.add(this.getDevelopmentCard(slot));
		}

		// activate every ProductionAbility selected
		for (int i = 0; i < this.leader_card_output.size(); i++){
			if (leader_card_output.get(i) != null) {
				productions.add(createProductionAbility(i));
			}
		}

		ProductionMessage message = new ProductionMessage(productions);
		App.sendMessage(message);
	}

	/**
	 * @param index the index of the LeaderCard and of the Resource in the leader_card_output array
	 * @return the ProductionAbility of the LeaderCard with the Resource set
	 */
	private ProductionAbility createProductionAbility(int index) {
		// create an ArrayList from the chosen Resource
		Resource required_resource = this.leader_card_output.get(index);
		ArrayList<Resource> new_resource = new ArrayList<Resource>();
		new_resource.add(leader_card_output.get(index));

		// get the ProductionAbility of the LeaderCard and set the chosen Resource
		ProductionAbility production_ability = (ProductionAbility) App.getMyPlayer().getLeaderCards().get(index).getAbility();
		production_ability.setProducedResources(new_resource);
		return production_ability;
	}
	
	/**
	 * @param index the index of the slot selected for the Production
	 * @return the selected DevelopmentCard based on the slot index
	 */
	private ProductionInterface getDevelopmentCard(int index){
		return App.getMyPlayer().getDevelopmentCardsSlots().getTopCards().get(index);
	}

	/**
	 * @return true if the Production base is set
	 */
	private boolean checkBaseProductionSet(){
		return input1.getImage() != null && input2.getImage() != null && output.getImage() != null;
	}

	/**
	 * Set the number chosen in the WhiteMarblesAbility, looping between all the possible choices (0-4)
	 *
	 * @param source the text of the WhiteMarblesAbility
	 * @param index the index of the LeaderCard in the leader_card_output array
	 */
	public void chooseWhiteMarblesNumber(Text source, int index){
		int current_state = this.white_marbles_numbers.get(index);

		// loop for all the possible numbers
		if (current_state == 4){
			 current_state = 0;
		} else {
			 current_state = current_state + 1;
		}

		this.white_marbles_numbers.set(index, current_state);
		source.setText("+" + String.valueOf(current_state));
	}

	/**
	 * Send a DiscardResourcesMessage
	 */
	public void handleDiscardResources(){
		DiscardResourcesMessage message = new DiscardResourcesMessage();
		App.sendMessage(message);
	}

	/**
	 * Send an ActivateLeaderMessage
	 *
	 * @param index the index of the LeaderCard to activate
	 */
	public void handleActivateLeaderCard(int index){
		ArrayList<LeaderCard> player_leader_cards = App.getMyPlayer().getLeaderCards();
		ActivateLeaderMessage message = new ActivateLeaderMessage(player_leader_cards.get(index));
		App.sendMessage(message);
	}

	/**
	 * Send a DiscardLeaderMessage
	 *
	 * @param index the index of the LeaderCard to discard
	 */
	public void handleDiscardLeaderCard(int index){
		ArrayList<LeaderCard> player_leader_cards = App.getMyPlayer().getLeaderCards();
		DiscardLeaderMessage message = new DiscardLeaderMessage(player_leader_cards.get(index));
		App.sendMessage(message);
	}

	/**
	 * Send a RearrangeMessage to swap the first and the second rows of the Warehouse
	 */
	public void handleSwapFirstSecond(){
		RearrangeMessage message = new RearrangeMessage(1, 2);
		App.sendMessage(message);
	}

	/**
	 * Send a RearrangeMessage to swap the second and the third rows of the Warehouse
	 */
	public void handleSwapSecondThird(){
		RearrangeMessage message = new RearrangeMessage(2, 3);
		App.sendMessage(message);
	}

	/**
	 * Send a RearrangeMessage to swap the first and the third rows of the Warehouse
	 */
	public void handleSwapFirstThird(){
		RearrangeMessage message = new RearrangeMessage(1, 3);
		App.sendMessage(message);
	}

	/**
	 * Send a EndTurnMessage
	 */
	public void handleEndTurn(){
		EndTurnMessage message = new EndTurnMessage();
		App.sendMessage(message);
	}
}
