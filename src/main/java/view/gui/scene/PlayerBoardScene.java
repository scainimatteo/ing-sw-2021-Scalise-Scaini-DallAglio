package it.polimi.ingsw.view.gui.scene;

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
import it.polimi.ingsw.controller.message.EndTurnMessage;
import it.polimi.ingsw.controller.message.StoreMessage;
import it.polimi.ingsw.controller.message.Storage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.view.gui.scene.OtherPlayerScene;
import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.scene.GameScene;
import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.*;

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

/**
 * TODO:
 * + paymessage
 * + 
 */
public class PlayerBoardScene extends SceneController implements ViewUpdateObserver, Initializable {
	ArrayList<Resource> all_resources;
	// contains the list of all the DevelopmentCardSlots activated
	ArrayList<Integer> development_card_productions;
	//TODO: rethink this array
	ArrayList<Resource> set_resources; 
	// contains the resources decided by the ProductionAbilities of the LeaderCards
	ArrayList<Resource> leader_card_output;
	Storage warehouse_storage;

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
	@FXML private ImageView output1;

	@FXML private Button activate_prod_button;

	@FXML private ImageView top1;
	@FXML private ImageView middle1;
	@FXML private ImageView middle2;
	@FXML private ImageView bottom1;
	@FXML private ImageView bottom2;
	@FXML private ImageView bottom3;

	@FXML private ImageView coin_sprite;
	@FXML private ImageView shield_sprite;
	@FXML private ImageView servant_sprite;
	@FXML private ImageView stone_sprite;
	@FXML private Text coin_amount;
	@FXML private Text shield_amount;
	@FXML private Text stone_amount;
	@FXML private Text servant_amount;

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
		this.set_resources = new ArrayList<Resource>(Arrays.asList(null, null, null, null));
		this.leader_card_output = new ArrayList<Resource>(Arrays.asList(null, null));
	}

	/**
	 * INITIALIZATION
	 */

	@Override
	public void initialize(URL location, ResourceBundle resouces){
		this.updateView();

		// get updated everytime the View gets updated
		App.setViewUpdateObserver(this);

		initializeBaseProduction();

		// set the Drag and Drop
		initializeWarehouseDragAndDrop();
		initializeStrongBoxDragAndDrop();
		initializeCostBoxDragAndDrop();
		initializeGainBoxDragAndDrop();

		hideNode(leader_card_pane);
		hideNode(cost_resources_pane);
		hideNode(last_turn_text);

		initializeOtherPlayersButton();
	}

	/**
	 * Set the onMouseClicked methods for the BaseProduction
	 */
	private void initializeBaseProduction() {
		input1.setOnMouseClicked(click -> chooseBaseProductionResource(input1, 0));
		input2.setOnMouseClicked(click -> chooseBaseProductionResource(input2, 1));
		output1.setOnMouseClicked(click -> chooseBaseProductionResource(output1, 2));
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

		top1.setOnDragDone(done -> handleDragDone(done, top1, true));
		middle1.setOnDragDone(done -> handleDragDone(done, middle1, true));
		middle2.setOnDragDone(done -> handleDragDone(done, middle2, true));
		bottom1.setOnDragDone(done -> handleDragDone(done, bottom1, true));
		bottom2.setOnDragDone(done -> handleDragDone(done, bottom2, true));
		bottom3.setOnDragDone(done -> handleDragDone(done, bottom3, true));

		// DROP
		top1.setOnDragOver(over -> handleDragOver(over, top1));
		middle1.setOnDragOver(over -> handleDragOver(over, middle1));
		middle2.setOnDragOver(over -> handleDragOver(over, middle2));
		bottom1.setOnDragOver(over -> handleDragOver(over, bottom1));
		bottom2.setOnDragOver(over -> handleDragOver(over, bottom2));
		bottom3.setOnDragOver(over -> handleDragOver(over, bottom3));

		top1.setOnDragDropped(dropped -> handleWarehouseDragDropped(dropped, top1));
		middle1.setOnDragDropped(dropped -> handleWarehouseDragDropped(dropped, middle1));
		middle2.setOnDragDropped(dropped -> handleWarehouseDragDropped(dropped, middle2));
		bottom1.setOnDragDropped(dropped -> handleWarehouseDragDropped(dropped, bottom1));
		bottom2.setOnDragDropped(dropped -> handleWarehouseDragDropped(dropped, bottom2));
		bottom3.setOnDragDropped(dropped -> handleWarehouseDragDropped(dropped, bottom3));
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
	 * Set the methods to drag and drop the resources in and out of the cost box
	 */
	// TODO: does this need the drag?
	private void initializeCostBoxDragAndDrop() {
		for (Node node: this.cost_box.getChildren()) {
			node.setOnDragOver(over -> handleDragOver(over, (ImageView) node));
			node.setOnDragDropped(dropped -> handleDragDropped(dropped, (ImageView) node));
		}
	}

	/**
	 * Set the methods to drag and drop the resources in and out of the gain box
	 */
	// TODO: does this need the drop?
	private void initializeGainBoxDragAndDrop(){
		for (Node node : this.gain_box.getChildren()){
			node.setOnDragDetected(detected -> handleDragDetected(detected, (ImageView) node));
			node.setOnDragDone(done -> handleDragDone(done, (ImageView) node, true));
		}
	}

	private void handleWarehouseDragDropped(DragEvent event, ImageView target){
		handleDragDropped(event, target);

		if (warehouse_storage == null){
			warehouse_storage = new Storage();
		} 

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
			}
		}

		if (checkGainBox()){
			StoreMessage message = new StoreMessage(warehouse_storage);
			App.sendMessage(message);
			warehouse_storage = null;
		} 
	}

	private boolean checkGainBox(){
		for (Node node : this.gain_box.getChildren()){
			if (((ImageView) node).getImage() != null){
				return false;
			} 
		}

		return true;
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
			view_player2_button.setOnMouseClicked(click -> changeSceneToOtherPlayer(other_players.get(0)));
			view_player2_button.setText(other_players.get(0).getNickname());
		}

		if (players.size() <= 2){
			hideNode(view_player3_button);
		} else {
			view_player3_button.setOnMouseClicked(click -> changeSceneToOtherPlayer(other_players.get(1)));
			view_player3_button.setText(other_players.get(1).getNickname());
		}

		if (players.size() <= 3){
			hideNode(view_player4_button);
		} else {
			view_player4_button.setOnMouseClicked(click -> changeSceneToOtherPlayer(other_players.get(2)));
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

		// PLAYER
		setWarehouse(warehouse);
		setStrongbox(strongbox);
		setDevelopmentCardSlot(development_card_slot);
		setLeaderCards(leader_cards);
		setFaithMarker(marker_position);
		setTiles(tiles);
		if (App.getTurn().getPlayer() != null){
			setTurnResources(to_pay, to_store);
		} 

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
		int counter = 0;

		for (Resource res : top){
			if (res != null){
				top1.setImage(new Image(res.getPath()));
			} else {
				top1.setImage(null);
			}
		}

		for (Resource res : mid){
			if (res != null){
				if (counter == 0){
					middle1.setImage(new Image(res.getPath()));
				} else if (counter == 1){
					middle2.setImage(new Image(res.getPath()));
				}
			} else {
				if (counter == 0){
					middle1.setImage(null);
				} else if (counter == 1){
					middle2.setImage(null);
				}
			}
			counter += 1;
		}

		for (Resource res : bot){
			if (res != null){
				if (counter == 0){
					bottom1.setImage(new Image(res.getPath()));
				} else if (counter == 1){
					bottom2.setImage(new Image(res.getPath()));
				} else if (counter == 2){
					bottom3.setImage(new Image(res.getPath()));
				}
			} else {
				if (counter == 0){
					bottom1.setImage(null);
				} else if (counter == 1){
					bottom2.setImage(null);
				} else if (counter == 2){
					bottom3.setImage(null);
				}
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
			((ImageView) this.first_slot.getChildren().get(i)).setImage(getDevelopmentCardPath(first_column, i));
			this.first_slot.getChildren().get(i).setOnMouseClicked(null);
		}

		// second slot
		for (int i = 0; i < this.second_slot.getChildren().size(); i++) {
			((ImageView) this.second_slot.getChildren().get(i)).setImage(getDevelopmentCardPath(second_column, i));
			this.second_slot.getChildren().get(i).setOnMouseClicked(null);
		}

		// third slot
		for (int i = 0; i < this.third_slot.getChildren().size(); i++) {
			((ImageView) this.third_slot.getChildren().get(i)).setImage(getDevelopmentCardPath(third_column, i));
			this.third_slot.getChildren().get(i).setOnMouseClicked(null);
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
			VBox leader_card = (VBox) node;
			ImageView leader_card_image = (ImageView) leader_card.getChildren().get(0);
			leader_card_image.setImage(new Image(leader_cards.get(i).getFrontPath()));

			setLeaderCardButtons(leader_card, leader_cards.get(i), i);
			setLeaderCardAbility(leader_card, i);
			i++;
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

	private void setLeaderCardAbility(VBox leader_card, int index) {
		LeaderCard player_leader_card = App.getMyPlayer().getLeaderCards().get(index);

		// if the LeaderCard has an ExtraSpaceAbility
		if (player_leader_card.isActive() && player_leader_card.getAbility().checkAbility(new ExtraSpaceAbility(null))) {
			setExtraSpaceAbility(leader_card, player_leader_card);
		// if the LeaderCard has a ProductionAbility
		} else if (player_leader_card.isActive() && player_leader_card.getAbility().checkAbility(new ProductionAbility(null, null))) {
			setProductionAbility(leader_card, player_leader_card, index);
		} else {
			// disable every LeaderCard modifier
			leader_card.getChildren().get(3).setDisable(true);
			leader_card.getChildren().get(4).setDisable(true);
			leader_card.getChildren().get(5).setDisable(true);
		}
	}

	private void setExtraSpaceAbility(VBox leader_card, LeaderCard player_leader_card) {
		Resource extra_space_resource = ((ExtraSpaceAbility) player_leader_card.getAbility()).getResourceType();
		int number_of_resources = ((ExtraSpaceAbility) player_leader_card.getAbility()).peekResources();

		// set the images of the Resources contained in the LeaderCard
		int j = 3;
		for (int i = 0; i < number_of_resources; i++) {
			ImageView leader_card_image = (ImageView) leader_card.getChildren().get(j);
			leader_card_image.setImage(new Image(extra_space_resource.getPath()));
			j++;
		}

		// disable the ProductionAbility modifier
		leader_card.getChildren().get(5).setDisable(true);
	}

	private void setProductionAbility(VBox leader_card, LeaderCard player_leader_card, int index) {
		ImageView production_resource_image = (ImageView) leader_card.getChildren().get(5);
		production_resource_image.setOnMouseClicked(click -> chooseProductionAbilityResource(production_resource_image, index));

		// disable the ExtraSpaceAbility modifiers
		leader_card.getChildren().get(3).setDisable(true);
		leader_card.getChildren().get(4).setDisable(true);
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

	private void setTurnResources(ArrayList<Resource> to_pay, ArrayList<Resource> to_store){
		String active_player = App.getTurn().getNickname();

		if (!to_pay.isEmpty() && !App.getMyPlayer().getNickname().equals(active_player)){
			showNode(cost_resources_pane);
			showNode(cost_box);
			hideNode(gain_box);
			pay_or_store_text.setText("Drop these resources here to pay the cost");
			List<Node> hbox_nodes = this.cost_box.getChildren();
			ImageView res_view;
			for (int i = 0; i < 10; i++){
				try{
					res_view = (ImageView) hbox_nodes.get(i);
					res_view.setImage(new Image(to_pay.get(i).getPath()));
				} catch (IndexOutOfBoundsException e){
					break;
				}
			}
		} else if (!to_store.isEmpty()) {
			showNode(cost_resources_pane);
			showNode(gain_box);
			hideNode(cost_box);
			pay_or_store_text.setText("Drag these resources away to store your gains");
			List<Node> hbox_nodes = this.gain_box.getChildren();
			ImageView res_view;
			for (int i = 0; i < 10; i++){
				try{
					res_view = (ImageView) hbox_nodes.get(i);
					res_view.setImage(new Image(to_store.get(i).getPath()));
				} catch (IndexOutOfBoundsException e){
					break;
				}
			}
		} else {
			hideNode(cost_resources_pane);
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
	 * BUTTON HANDLERS
	 */

	/**
	 * Show the GameScene
	 */
	public void changeSceneToGame(){
		// remove the Scene from the array of ViewUpdateObservers to save memory
		App.removeViewUpdateObserver(this);
		new GameScene().changeScene("/fxml/gamescene.fxml");
	}

	/**
	 * Show the Board of the other Players
	 */
	public void changeSceneToOtherPlayer(SimplePlayer other_player){
		// remove the Scene from the array of ViewUpdateObservers to save memory
		App.removeViewUpdateObserver(this);
		new OtherPlayerScene(other_player).changeScene("/fxml/otherplayerscene.fxml");
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
	 * Set the Resource images in the base Production
	 */
	public void chooseBaseProductionResource(ImageView source, int index){
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			set_resources.set(index, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(set_resources.get(index));

			if (resource_pos != 3){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				set_resources.set(index, all_resources.get(resource_pos + 1));
			} else {
				source.setImage(null);
				set_resources.set(index, null);
			}
		}
	}

	/**
	 * Set the Resource images in the ProductionAbility
	 */
	public void chooseProductionAbilityResource(ImageView source, int index){
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			leader_card_output.set(index, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(leader_card_output.get(index));

			if (resource_pos != all_resources.size() - 1){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				leader_card_output.set(index, all_resources.get(resource_pos + 1));
			} else {
				source.setImage(null);
				leader_card_output.set(index, null);
			}
		}
	}

	/**
	 * Send a ProductionMessage with all the ProductionInterfaces selected
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
		for (Integer slot: development_card_productions){
			productions.add(this.getDevelopmentCard(slot));
		}

		// TODO: LeaderCard Productions

		ProductionMessage message = new ProductionMessage(productions);
		App.sendMessage(message);
		updateView();
	}
	
	private ProductionInterface getDevelopmentCard(int index){
		return App.getMyPlayer().getDevelopmentCardsSlots().getTopCards().get(index);
	}

	private boolean checkBaseProductionSet(){
		return input1.getImage() != null && input2.getImage() != null && output1.getImage() != null;
	}

	public void handleDiscardResources(){
		DiscardResourcesMessage message = new DiscardResourcesMessage();
		App.sendMessage(message);
		//TODO: updateView();
	}

	public void handleActivateLeaderCard(int index){
		ArrayList<LeaderCard> player_leader_cards = App.getMyPlayer().getLeaderCards();
		ActivateLeaderMessage message = new ActivateLeaderMessage(player_leader_cards.get(index));
		App.sendMessage(message);
		//TODO: updateView();
	}

	public void handleDiscardLeaderCard(int index){
		ArrayList<LeaderCard> player_leader_cards = App.getMyPlayer().getLeaderCards();
		DiscardLeaderMessage message = new DiscardLeaderMessage(player_leader_cards.get(index));
		App.sendMessage(message);
		//TODO: updateView();
	}

	/**
	 * Send a EndTurnMessage
	 */
	public void handleEndTurn(){
		EndTurnMessage message = new EndTurnMessage();
		App.sendMessage(message);
	}
}
