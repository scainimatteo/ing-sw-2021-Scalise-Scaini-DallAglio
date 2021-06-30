package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.ProductionAbility;

import it.polimi.ingsw.model.game.sologame.SoloActionToken;

import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.controller.message.ProductionMessage;
import it.polimi.ingsw.controller.message.DiscardResourcesMessage;

import it.polimi.ingsw.view.gui.scene.OtherPlayerScene;
import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;
import it.polimi.ingsw.view.simplemodel.SimpleSoloPlayer;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.view.simplemodel.SimpleSoloGame;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

import it.polimi.ingsw.util.observer.ViewUpdateObserver;

//TODO: remove useless imports
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.net.URL;

public class PlayerBoardScene extends SceneController implements ViewUpdateObserver, Initializable {
	ArrayList<Integer> development_card_productions;
	ArrayList<Resource> all_resources;
	ArrayList<Resource> set_resources; 
	ArrayList<Resource> leader_card_output;

	@FXML private GridPane faith_track;
	@FXML private HBox development_card_slot;
	@FXML private Pane cost_resources_pane;
	@FXML private Pane leader_card_pane;
	@FXML private HBox leader_card_array;

	@FXML private Button game_button;
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

	@FXML private ImageView prod_ability_1;
	@FXML private ImageView extra_space_11;
	@FXML private ImageView extra_space_12;
	@FXML private ImageView prod_ability_2;
	@FXML private ImageView extra_space_21;
	@FXML private ImageView extra_space_22;

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

		leaders_button.setOnMouseClicked(click -> handleToggleLeaderButton(leaders_button));

		initializeBaseProduction();

		// set the Drag and Drop
		initializeWarehouseDragAndDrop();
		initializeStrongBoxDragAndDrop();
		initializeCostBoxDragAndDrop();

		hideNode(leader_card_pane);
		//hideNode(cost_resources_pane);
		hideNode(last_turn_text);

		initializeOtherPlayersButton();
	}

	/**
	 * Set the onMouseClicked methods for the BaseProduction
	 */
	private void initializeBaseProduction() {
		input1.setOnMouseClicked(click -> handleBaseProductionResource(input1, 0));
		input2.setOnMouseClicked(click -> handleBaseProductionResource(input2, 1));
		output1.setOnMouseClicked(click -> handleBaseProductionResource(output1, 2));
	}

	/**
	 * Set the methods to drag and drop the resources in and out of the Warehouse
	 */
	private void initializeWarehouseDragAndDrop() {
		// DRAG
		top1.setOnDragDetected(detected -> handleDragDetected(detected, top1));
		middle1.setOnDragDetected(detected -> handleDragDetected(detected, middle1));
		middle2.setOnDragDetected(detected -> handleDragDetected(detected, middle2));
		bottom1.setOnDragDetected(detected -> handleDragDetected(detected, bottom1));
		bottom2.setOnDragDetected(detected -> handleDragDetected(detected, bottom2));
		bottom3.setOnDragDetected(detected -> handleDragDetected(detected, bottom3));

		// DROP
		top1.setOnDragDone(done -> handleDragDone(done, top1, true));
		middle1.setOnDragDone(done -> handleDragDone(done, middle1, true));
		middle2.setOnDragDone(done -> handleDragDone(done, middle2, true));
		bottom1.setOnDragDone(done -> handleDragDone(done, bottom1, true));
		bottom2.setOnDragDone(done -> handleDragDone(done, bottom2, true));
		bottom3.setOnDragDone(done -> handleDragDone(done, bottom3, true));
	}

	/**
	 * Set the methods to drag and drop the resources in and out of the StrongBox
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
	private void initializeCostBoxDragAndDrop() {
		for (Node node: this.cost_box.getChildren()) {
			node.setOnDragOver(over -> handleDragOver(over, (ImageView) node));
			node.setOnDragDropped(dropped -> handleDragDropped(dropped, (ImageView) node));
		}
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
	 * Initialize the leader cards imageview
	 */
	private void initializeLeaderCards(){
		ArrayList<LeaderCard> player_leader_cards = App.getMyPlayer().getLeaderCards();
		int counter = 0;

		for (LeaderCard card : player_leader_cards){
			if (card.getAbility().checkAbility(new ExtraSpaceAbility(null))){
				if (counter == 0){
					prod_ability_1.setDisable(true);
				} else {
					prod_ability_2.setDisable(true);
				}
			} else if (card.getAbility().checkAbility(new ProductionAbility(null, null))){
				if (counter == 0){
					extra_space_11.setDisable(true);
					extra_space_12.setDisable(true);
					prod_ability_1.setOnMouseClicked(click -> handleLeaderCardProductionResource(prod_ability_1, 0));
				} else {
					extra_space_21.setDisable(true);
					extra_space_22.setDisable(true);
					prod_ability_2.setOnMouseClicked(click -> handleLeaderCardProductionResource(prod_ability_2, 1));
				}
			} else {
				if (counter == 0){
					prod_ability_1.setDisable(true);
					extra_space_11.setDisable(true);
					extra_space_12.setDisable(true);
				} else {
					prod_ability_2.setDisable(true);
					extra_space_21.setDisable(true);
					extra_space_22.setDisable(true);
				}
			}

			counter += 1;
		}
	}

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

		// PLAYER
		setWarehouse(warehouse);
		setStrongbox(strongbox);
		setDevelopmentCardSlot(development_card_slot);
		setLeaderCards(leader_cards);
		setFaithMarker(marker_position);
		setTiles(tiles);
		setTurnResources(to_pay, to_store);

		// SOLOGAME
		if (App.isSoloGame()) {
			SimpleSoloGame game = (SimpleSoloGame) App.getSimpleGame();
			SimpleSoloPlayer player = (SimpleSoloPlayer) App.getMyPlayer();
			setLastToken(game.getLastToken());
			setBlackMarker(player.getBlackMarkerPosition());
		}
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
					coin_amount.setText(String.valueOf(strongbox.get(Resource.COIN)));
					break;
				case SERVANT:
					servant_amount.setText(String.valueOf(strongbox.get(Resource.SERVANT)));
					break;
				case SHIELD:
					shield_amount.setText(String.valueOf(strongbox.get(Resource.SHIELD)));
					break;
				case STONE:
					stone_amount.setText(String.valueOf(strongbox.get(Resource.STONE)));
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
			i++;
		}
	}

	private void setTurnResources(ArrayList<Resource> to_pay, ArrayList<Resource> to_store){
		if (!to_pay.isEmpty()){
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
		new GameScene().changeScene("/fxml/gamescene.fxml");
	}

	public void changeSceneToOtherPlayer(SimplePlayer other_player){
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

	public void handleToggleLeaderButton(ToggleButton button){
		if (!button.isSelected()){
			showNode(leader_card_pane);
			hideNode(development_card_slot);
		} else {
			hideNode(leader_card_pane);
			showNode(development_card_slot);
		}
	}

	public void handleBaseProductionResource(ImageView source, int pos){
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			set_resources.set(pos, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(set_resources.get(pos));

			if (resource_pos != 3){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				set_resources.set(pos, all_resources.get(resource_pos + 1));
			} else {
				source.setImage(null);
				set_resources.set(pos, null);
			}
		}
	}

	public void handleLeaderCardProductionResource(ImageView source, int pos){
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			leader_card_output.set(pos, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(set_resources.get(pos));

			if (resource_pos != 3){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				leader_card_output.set(pos, all_resources.get(resource_pos + 1));
			} else {
				source.setImage(null);
				leader_card_output.set(pos, null);
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
	}
	
	private ProductionInterface getDevelopmentCard(int index){
		return App.getMyPlayer().getDevelopmentCardsSlots().getTopCards().get(index);
	}

	private boolean checkBaseProductionSet(){
		return input1.getImage() != null && input2.getImage() != null && output1.getImage() != null;
	}

	public void handleDiscardButton(){
		DiscardResourcesMessage message = new DiscardResourcesMessage();
		App.sendMessage(message);
	}
}
