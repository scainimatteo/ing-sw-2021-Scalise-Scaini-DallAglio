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
	SimplePlayer other_player;

	@FXML private GridPane faith_track;
	@FXML private HBox development_card_slot;
	@FXML private Pane leader_card_pane;
	@FXML private HBox leader_card_array;

	@FXML private ToggleButton leaders_button;

	@FXML private ImageView top1;
	@FXML private ImageView middle1;
	@FXML private ImageView middle2;
	@FXML private ImageView bottom1;
	@FXML private ImageView bottom2;
	@FXML private ImageView bottom3;

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

	public OtherPlayerScene(SimplePlayer other_player) {
		this.other_player = other_player;
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

		this.nickname_text.setText(this.other_player.getNickname());

		hideNode(leader_card_pane);
	}

	/**
	 * VIEW SETTERS
	 *
	 * Set all the ImageViews and methods of the board, using the ViewUpdateObserver
	 *
	 * This method gets called once at the start and everytime there's a ViewUpdate (when the Model is changed)
	 */
	public void updateView(){
		SimpleWarehouse warehouse = this.other_player.getWarehouse();
		HashMap<Resource, Integer> strongbox = this.other_player.getStrongbox();
		SimpleDevelopmentCardSlot development_card_slot = this.other_player.getDevelopmentCardsSlots();
		ArrayList<LeaderCard> leader_cards = this.other_player.getLeaderCards();
		int marker_position = this.other_player.getMarker().getPosition();
		Tile[] tiles = this.other_player.getReports();

		// PLAYER
		setWarehouse(warehouse);
		setStrongbox(strongbox);
		setDevelopmentCardSlot(development_card_slot);
		setLeaderCards(leader_cards);
		setFaithMarker(marker_position);
		setTiles(tiles);
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
	}

	private void setLeaderCards(ArrayList<LeaderCard> leader_cards){
		int i = 0;
		for (Node node: this.leader_card_array.getChildren()) {
			if (i < leader_cards.size()) {
				VBox leader_card = (VBox) node;
				ImageView leader_card_image = (ImageView) leader_card.getChildren().get(0);
				// only show the active LeaderCards of the other Players
				if (leader_cards.get(i).isActive()) {
					leader_card_image.setImage(new Image(leader_cards.get(i).getFrontPath()));
			
					setLeaderCardAbility(leader_card, leader_cards.get(i));
				}

				showNode(node);
				i++;
			} else {
				hideNode(node);
			}
		}
	}

	private void setLeaderCardAbility(VBox leader_card, LeaderCard player_leader_card) {
		// if the LeaderCard has an ExtraSpaceAbility
		if (player_leader_card.getAbility().checkAbility(new ExtraSpaceAbility(null))) {
			setExtraSpaceAbility(leader_card, player_leader_card);
		} else {
			// hide every LeaderCard modifier
			hideNode(leader_card.getChildren().get(1));
			hideNode(leader_card.getChildren().get(2));
		}
	}

	private void setExtraSpaceAbility(VBox leader_card, LeaderCard player_leader_card) {
		Resource extra_space_resource = ((ExtraSpaceAbility) player_leader_card.getAbility()).getResourceType();
		int number_of_resources = ((ExtraSpaceAbility) player_leader_card.getAbility()).peekResources();

		// set the images of the Resources contained in the LeaderCard
		int j = 1;
		for (int i = 0; i < number_of_resources; i++) {
			ImageView leader_card_image = (ImageView) leader_card.getChildren().get(j);
			leader_card_image.setImage(new Image(extra_space_resource.getPath()));
			showNode(leader_card_image);
			j++;
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

	public void handleToggleLeaderButton(ToggleButton button){
		if (!button.isSelected()){
			showNode(leader_card_pane);
			hideNode(development_card_slot);
		} else {
			hideNode(leader_card_pane);
			showNode(development_card_slot);
		}
	}
}
