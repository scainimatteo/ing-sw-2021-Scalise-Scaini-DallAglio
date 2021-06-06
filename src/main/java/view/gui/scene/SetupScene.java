package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.controller.message.DiscardLeaderMessage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.TransferMode;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.net.URL;

public class SetupScene extends SceneController implements Initializable{
	ArrayList<LeaderCard> to_delete = new ArrayList<LeaderCard>();
	double opacity_percent = 0.5;
	SimplePlayer player;

	@FXML private Pane select_card_pane;
	@FXML private Pane select_resource_pane;
	@FXML private Pane waiting_pane;

	@FXML private ImageView leader_card_1;
	@FXML private ImageView leader_card_2;
	@FXML private ImageView leader_card_3;
	@FXML private ImageView leader_card_4;

	@FXML private Image leader_card_image_1;
	@FXML private Image leader_card_image_2;
	@FXML private Image leader_card_image_3;
	@FXML private Image leader_card_image_4;

	@Override
	public void initialize(URL location, ResourceBundle resources){
		this.player = App.getMyPlayer();

		leader_card_image_1 = new Image(getClass().getResource(player.getLeaderCards().get(0).getFrontPath()).toString());
		leader_card_image_2 = new Image(getClass().getResource(player.getLeaderCards().get(1).getFrontPath()).toString());
		leader_card_image_3 = new Image(getClass().getResource(player.getLeaderCards().get(2).getFrontPath()).toString());
		leader_card_image_4 = new Image(getClass().getResource(player.getLeaderCards().get(3).getFrontPath()).toString());

		leader_card_1.setImage(leader_card_image_1);
		leader_card_2.setImage(leader_card_image_2);
		leader_card_3.setImage(leader_card_image_3);
		leader_card_4.setImage(leader_card_image_4);

		showNode(select_card_pane);
		hideNode(select_resource_pane);
		hideNode(waiting_pane);
	}

	@FXML
	public void leaderCard1Click(){
		to_delete.add(player.getLeaderCards().get(0));
		leader_card_1.setDisable(true);
		leader_card_1.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	@FXML
	public void leaderCard2Click(){
		to_delete.add(player.getLeaderCards().get(1));
		leader_card_2.setDisable(true);
		leader_card_2.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	@FXML
	public void leaderCard3Click(){
		to_delete.add(player.getLeaderCards().get(2));
		leader_card_3.setDisable(true);
		leader_card_3.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	@FXML
	public void leaderCard4Click(){
		to_delete.add(player.getLeaderCards().get(3));
		leader_card_4.setDisable(true);
		leader_card_4.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	private boolean checkLeader(){
		return to_delete.size() == 2;
	}

	private void endLeaderScene(){
		DiscardLeaderMessage message;

		message = new DiscardLeaderMessage(to_delete.get(0));
		App.sendMessage(message);

		message = new DiscardLeaderMessage(to_delete.get(1));
		App.sendMessage(message);
		
		hideNode(select_card_pane);
		showNode(select_resource_pane);
	}

	public void handleDragDetected(MouseEvent event) {
		/* drag was detected, start a drag-and-drop gesture*/
        /* allow any transfer mode */
		ImageView source = (ImageView) event.getSource();
        Dragboard db = source.startDragAndDrop(TransferMode.ANY);
        
        /* Put a string on a dragboard */
        ClipboardContent content = new ClipboardContent();
        content.putImage(source.getImage());
        db.setContent(content);
        
        event.consume();
	}
}
