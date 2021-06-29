package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.ProductionInterface;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.controller.message.ProductionMessage;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

import it.polimi.ingsw.util.observer.ViewUpdateObserver;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
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
import java.net.URL;

public class PlayerBoardScene extends SceneController implements ViewUpdateObserver, Initializable {
	ArrayList<Resource> all_resources = new ArrayList<Resource>(Arrays.asList(Resource.COIN, Resource.SERVANT, Resource.SHIELD, Resource.STONE));
	ArrayList<Resource> setted_resources = new ArrayList<Resource>(Arrays.asList(null, null, null));
	ArrayList<String> production_arraylist;
	ArrayList<String> order;
	SimplePlayer player;

	@FXML private GridPane strongbox;
	@FXML private HBox WarehouseTop;
	@FXML private HBox WarehouseMiddle;
	@FXML private HBox WarehouseBottom;
	@FXML private Pane leader_card_pane;
	@FXML private GridPane faith_track;
	@FXML private HBox development_card_slots;
	@FXML private Pane cost_resources_pane;

	@FXML private Button game_button;
	@FXML private Button view_player2_button;
	@FXML private Button view_player3_button;
	@FXML private Button view_player4_button;
	@FXML private ToggleButton leaders_button;

	@FXML private ImageView card11;
	@FXML private ImageView card21;
	@FXML private ImageView card31;

	@FXML private Text last_turn_text;
	@FXML private ImageView solo_game_token;

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


	@FXML private ImageView cost1;
	@FXML private ImageView cost2;
	@FXML private ImageView cost3;
	@FXML private ImageView cost4;
	@FXML private ImageView cost5;
	@FXML private ImageView cost6;
	@FXML private ImageView cost7;
	@FXML private ImageView cost8;
	@FXML private ImageView cost9;
	@FXML private ImageView cost10;

	/**
	 * TODO:
	 * set:
	 *	 - dev card slot
	 *	 - leader card
	 *	 - faith track (+ black marker)
	 *	 - token
	 *
	 * setOnMouseClicked:
	 *	 ~ other players (forse dovrei mettergli l'indice del giocatore)
	 *	 - [LEADERCARD] setOnMouseClicked con metodo che loopa nelle risorse
	 *
	 * showNode methods:
	 *	 - last_turn_text
	 *	 - cost_resources_pane
	 *
	 * drag:
	 *	 - extraspace
	 */

	@Override
	public void initialize(URL location, ResourceBundle resouces){
		this.player = App.getMyPlayer();
		this.order = App.getSimpleGame().getOrder();
		this.production_arraylist = new ArrayList<String>();
		App.setViewUpdateObserver(this);

		game_button.setOnMouseClicked(click -> handleChangeScene(new GameScene(), "/fxml/gamescene.fxml"));
		// view_player2_button.setOnMouseClicked(click -> handleChangeScene(new OtherPlayerScene(), "/fxml/otherplayerscene.fxml"));
		// view_player3_button.setOnMouseClicked(click -> handleChangeScene(new OtherPlayerScene(), "/fxml/otherplayerscene.fxml"));
		// view_player4_button.setOnMouseClicked(click -> handleChangeScene(new OtherPlayerScene(), "/fxml/otherplayerscene.fxml"));
		
		leaders_button.setOnMouseClicked(click -> handleToggleLeaderButton(leaders_button));

		card11.setOnMouseClicked(click -> handleProductionClick(card11));
		card21.setOnMouseClicked(click -> handleProductionClick(card21));
		card31.setOnMouseClicked(click -> handleProductionClick(card31));

		input1.setOnMouseClicked(click -> handleBaseProductionResource(input1, 0));
		input2.setOnMouseClicked(click -> handleBaseProductionResource(input2, 1));
		output1.setOnMouseClicked(click -> handleBaseProductionResource(output1, 2));

		top1.setOnDragDetected(detected -> handleDragDetected(detected, top1));
		middle1.setOnDragDetected(detected -> handleDragDetected(detected, middle1));
		middle2.setOnDragDetected(detected -> handleDragDetected(detected, middle2));
		bottom1.setOnDragDetected(detected -> handleDragDetected(detected, bottom1));
		bottom2.setOnDragDetected(detected -> handleDragDetected(detected, bottom2));
		bottom3.setOnDragDetected(detected -> handleDragDetected(detected, bottom3));

		coin_sprite.setOnDragDetected(detected -> handleDragDetected(detected, coin_sprite));
		shield_sprite.setOnDragDetected(detected -> handleDragDetected(detected, shield_sprite));
		servant_sprite.setOnDragDetected(detected -> handleDragDetected(detected, servant_sprite));
		stone_sprite.setOnDragDetected(detected -> handleDragDetected(detected, stone_sprite));

		top1.setOnDragDone(done -> handleDragDone(done, top1, true));
		middle1.setOnDragDone(done -> handleDragDone(done, middle1, true));
		middle2.setOnDragDone(done -> handleDragDone(done, middle2, true));
		bottom1.setOnDragDone(done -> handleDragDone(done, bottom1, true));
		bottom2.setOnDragDone(done -> handleDragDone(done, bottom2, true));
		bottom3.setOnDragDone(done -> handleDragDone(done, bottom3, true));

		coin_sprite.setOnDragDone(done -> handleDragDone(done, coin_sprite, false));
		shield_sprite.setOnDragDone(done -> handleDragDone(done, shield_sprite, false));
		servant_sprite.setOnDragDone(done -> handleDragDone(done, servant_sprite, false));
		stone_sprite.setOnDragDone(done -> handleDragDone(done, stone_sprite, false));

		cost1.setOnDragOver(over -> handleDragOver(over, cost1));
		cost2.setOnDragOver(over -> handleDragOver(over, cost2));
		cost3.setOnDragOver(over -> handleDragOver(over, cost3));
		cost4.setOnDragOver(over -> handleDragOver(over, cost4));
		cost5.setOnDragOver(over -> handleDragOver(over, cost5));
		cost6.setOnDragOver(over -> handleDragOver(over, cost6));
		cost7.setOnDragOver(over -> handleDragOver(over, cost7));
		cost8.setOnDragOver(over -> handleDragOver(over, cost8));
		cost9.setOnDragOver(over -> handleDragOver(over, cost9));
		cost10.setOnDragOver(over -> handleDragOver(over, cost10));

		cost1.setOnDragDropped(dropped -> handleDragDropped(dropped, cost1));
		cost2.setOnDragDropped(dropped -> handleDragDropped(dropped, cost2));
		cost3.setOnDragDropped(dropped -> handleDragDropped(dropped, cost3));
		cost4.setOnDragDropped(dropped -> handleDragDropped(dropped, cost4));
		cost5.setOnDragDropped(dropped -> handleDragDropped(dropped, cost5));
		cost6.setOnDragDropped(dropped -> handleDragDropped(dropped, cost6));
		cost7.setOnDragDropped(dropped -> handleDragDropped(dropped, cost7));
		cost8.setOnDragDropped(dropped -> handleDragDropped(dropped, cost8));
		cost9.setOnDragDropped(dropped -> handleDragDropped(dropped, cost9));
		cost10.setOnDragDropped(dropped -> handleDragDropped(dropped, cost10));

		hideNode(leader_card_pane);
		hideNode(cost_resources_pane);
		hideNode(last_turn_text);

		if (this.order.size() != 1){
			hideNode(solo_game_token);
		} 

		if (this.order.size() <= 3){
			hideNode(view_player4_button);
		} 
		if (this.order.size() <= 2){
			hideNode(view_player3_button);
		} 
		if (this.order.size() == 1){
			hideNode(view_player2_button);
		} 
	}

	public void updateView(){
		SimpleWarehouse warehouse = App.getMyPlayer().getWarehouse();
		HashMap<Resource, Integer> strongbox = App.getMyPlayer().getStrongbox();
		SimpleDevelopmentCardSlot dev_card_slot = App.getMyPlayer().getDevelopmentCardsSlots();
		ArrayList<LeaderCard> leader_card = App.getMyPlayer().getLeaderCards();
		int marker_position = App.getMyPlayer().getMarker().getPosition();

		setWarehouse(warehouse);
		setStrongbox(strongbox);
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

	public void handleChangeScene(SceneController controller, String fxml_path){
		Platform.runLater(() -> {
			controller.changeScene(fxml_path);
		});
	}

	public void handleProductionClick(ImageView production){
		String production_id = production.getId();

		if (!production_arraylist.contains(production_id)){
			production_arraylist.add(production_id);
			production.setOpacity(0.50);
		} else {
			production_arraylist.remove(production_id);
			production.setOpacity(1);
		}
	}

	public void handleToggleLeaderButton(ToggleButton button){
		if (!button.isSelected()){
			showNode(leader_card_pane);
			hideNode(development_card_slots);
		} else {
			hideNode(leader_card_pane);
			showNode(development_card_slots);
		}
	}

	public void handleBaseProductionResource(ImageView source, int pos){
		if (source.getImage() == null){
			source.setImage(new Image((all_resources.get(0)).getPath()));
			setted_resources.set(pos, all_resources.get(0));
		} else {
			int resource_pos = all_resources.indexOf(setted_resources.get(pos));

			if (resource_pos != 3){
				source.setImage(new Image((all_resources.get(resource_pos + 1)).getPath()));
				setted_resources.set(pos, all_resources.get(resource_pos + 1));
			} else {
				source.setImage(null);
				setted_resources.set(pos, null);
			}
		}
	}

	public void handleActivateProdButton(){
		ArrayList<ProductionInterface> production_interface_arraylist = new ArrayList<ProductionInterface>();
		ArrayList<Resource> input = new ArrayList<Resource>(Arrays.asList(setted_resources.get(0), setted_resources.get(1)));
		ArrayList<Resource> output = new ArrayList<Resource>(Arrays.asList(setted_resources.get(2)));

		if (checkProdBase()){
			Production production_base = new Production(input, output);
			production_interface_arraylist.add(production_base);
		} 

		for (String prod_string : production_arraylist){
			production_interface_arraylist.add(this.getDevelopmentCard(Character.getNumericValue(prod_string.charAt(5))));
		}

		ProductionMessage message = new ProductionMessage(production_interface_arraylist);
		App.sendMessage(message);
	}
	
	private ProductionInterface getDevelopmentCard(int index){
		return player.getDevelopmentCardsSlots().getTopCards().get(index);
	}

	private boolean checkProdBase(){
		return input1.getImage() != null && input2.getImage() != null && output1.getImage() != null;
	}
}
