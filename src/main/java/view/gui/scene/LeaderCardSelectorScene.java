package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.gui.scene.SceneController;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.controller.message.DiscardLeaderMessage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.net.URL;

public class LeaderCardSelectorScene extends SceneController implements Initializable{
	ArrayList<LeaderCard> to_delete = new ArrayList<LeaderCard>();
	double opacity_percent = 0.5;
	SimplePlayer player;

	@FXML
	StackPane leader_card_scene;

	@FXML
	ImageView leader_card_1;
	@FXML
	ImageView leader_card_2;
	@FXML
	ImageView leader_card_3;
	@FXML
	ImageView leader_card_4;

	@FXML
	Image leader_card_image_1;
	@FXML
	Image leader_card_image_2;
	@FXML
	Image leader_card_image_3;
	@FXML
	Image leader_card_image_4;

	@Override
	public void initialize(URL location, ResourceBundle resources){
		this.player = App.getMyPlayer();

		leader_card_image_1 = new Image(player.getLeaderCards().get(0).getFrontPath());
		leader_card_image_2 = new Image(player.getLeaderCards().get(1).getFrontPath());
		leader_card_image_3 = new Image(player.getLeaderCards().get(2).getFrontPath());
		leader_card_image_4 = new Image(player.getLeaderCards().get(3).getFrontPath());

		leader_card_1.setImage(leader_card_image_1);
		leader_card_2.setImage(leader_card_image_2);
		leader_card_3.setImage(leader_card_image_3);
		leader_card_4.setImage(leader_card_image_4);

		hideNode(leader_card_scene);
	}

	public void leaderCard1Click(){
		to_delete.add(player.getLeaderCards().get(1));
		leader_card_1.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	public void leaderCard2Click(){
		to_delete.add(player.getLeaderCards().get(2));
		leader_card_2.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	public void leaderCard3Click(){
		to_delete.add(player.getLeaderCards().get(3));
		leader_card_3.setOpacity(opacity_percent);

		if (checkLeader()){
			endLeaderScene();
		} 
	}

	public void leaderCard4Click(){
		to_delete.add(player.getLeaderCards().get(4));
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
		
		hideNode(leader_card_scene);

		// resource_choice_scene.setVisible(true);
	}
}
