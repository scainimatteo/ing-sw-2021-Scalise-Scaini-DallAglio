package it.polimi.ingsw.view.gui.scene;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URL;

import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.view.gui.scene.PlayerBoardScene;
import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

import it.polimi.ingsw.util.observer.ViewUpdateObserver;

public class OtherPlayerScene extends SceneController implements ViewUpdateObserver, Initializable {
	String other_player_nickname;

	@FXML private GridPane faith_track;
	@FXML private HBox development_card_slot;
	@FXML private Pane leader_card_pane;
	@FXML private HBox leader_card_array;

	@FXML private ToggleButton leaders_button;

	@FXML private ImageView top1;
	@FXML private HBox warehouse_top;
	@FXML private HBox warehouse_middle;
	@FXML private HBox warehouse_bottom;

	@FXML private Text coin_amount;
	@FXML private Text shield_amount;
	@FXML private Text stone_amount;
	@FXML private Text servant_amount;

	@FXML private ImageView tile1;
	@FXML private ImageView tile2;
	@FXML private ImageView tile3;

	@FXML private StackPane first_slot;
	@FXML private StackPane second_slot;
	@FXML private StackPane third_slot;

	@FXML private Text nickname_text;

	public OtherPlayerScene(String other_player_nickname) {
		this.other_player_nickname = other_player_nickname;
	}

	/**
	 * INITIALIZATION
	 */

	@Override
	public void initialize(URL location, ResourceBundle resouces){
		this.updateView();

		// get updated everytime the View gets updated
		App.setViewUpdateObserver(this);

		hideNode(leader_card_pane);

		this.nickname_text.setText(this.other_player_nickname);
	}

	/**
	 * VIEW SETTERS
	 *
	 * Set all the ImageViews and methods of the board, using the ViewUpdateObserver
	 *
	 * This method gets called once at the start and everytime there's a ViewUpdate (when the Model is changed)
	 */
	public void updateView(){
		SimplePlayer other_player = getOtherPlayer();
		SimpleWarehouse warehouse = other_player.getWarehouse();
		HashMap<Resource, Integer> strongbox = other_player.getStrongbox();
		SimpleDevelopmentCardSlot development_card_slot = other_player.getDevelopmentCardsSlots();
		ArrayList<LeaderCard> leader_cards = other_player.getLeaderCards();
		int marker_position = other_player.getMarker().getPosition();
		Tile[] tiles = other_player.getReports();

		resetFaithTrack();
		resetWarehouse();

		// PLAYER
		setWarehouse(warehouse);
		setStrongbox(strongbox);
		setDevelopmentCardSlot(development_card_slot);
		setLeaderCards(leader_cards);
		setFaithMarker(marker_position);
		setTiles(tiles);
	}

	private SimplePlayer getOtherPlayer() {
		for (SimplePlayer p: App.getSimplePlayers()) {
			if (p.getNickname().equals(this.other_player_nickname)) {
				return p;
			}
		}

		return null;
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
		for (int i = this.first_slot.getChildren().size() - 1; i >= 0; i--) {
			((ImageView) this.first_slot.getChildren().get(i)).setImage(getDevelopmentCardPath(first_column, i));
		}

		// second slot
		for (int i = this.second_slot.getChildren().size() - 1; i >= 0; i--) {
			((ImageView) this.second_slot.getChildren().get(i)).setImage(getDevelopmentCardPath(second_column, i));
		}

		// third slot
		for (int i = this.third_slot.getChildren().size() - 1; i >= 0; i--) {
			((ImageView) this.third_slot.getChildren().get(i)).setImage(getDevelopmentCardPath(third_column, i));
		}
	}

	private void setLeaderCards(ArrayList<LeaderCard> leader_cards){
		int i = 0;
		for (Node node: this.leader_card_array.getChildren()) {
			if (i < leader_cards.size()) {
				StackPane leader_card_stackpane = (StackPane) node;
				ImageView leader_card_image = (ImageView) leader_card_stackpane.getChildren().get(0);
				// only show the active LeaderCards of the other Players
				if (leader_cards.get(i).isActive()) {
					leader_card_image.setImage(new Image(leader_cards.get(i).getFrontPath()));
			
					setLeaderCardAbility(leader_card_stackpane, leader_cards.get(i));
				}

				showNode(node);
				i++;
			} else {
				hideNode(node);
			}
		}
	}

	private void setLeaderCardAbility(StackPane leader_card, LeaderCard player_leader_card) {
		// if the LeaderCard has an ExtraSpaceAbility
		if (player_leader_card.getAbility().checkAbility(new ExtraSpaceAbility(null))) {
			setExtraSpaceAbility((HBox) leader_card.getChildren().get(1), player_leader_card);
		} else {
			// hide every LeaderCard modifier
			hideNode(leader_card.getChildren().get(1));
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
	 * Set all the ImageViews of the FaithTrack as null
	 */
	private void resetFaithTrack() {
		for (Node cell: faith_track.getChildren()) {
			((ImageView) cell).setImage(null);
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
	 * Show the PlayerBoardScene
	 */
	public void changeSceneToBoard(){
		// remove the Scene from the array of ViewUpdateObservers to save memory
		App.removeViewUpdateObserver(this);
		new PlayerBoardScene().changeScene("/fxml/playerboardscene.fxml");
	}

	public void showOrHideLeaderCards(){
		if (!this.leaders_button.isSelected()){
			showNode(leader_card_pane);
			hideNode(development_card_slot);
		} else {
			hideNode(leader_card_pane);
			showNode(development_card_slot);
		}
	}
}
