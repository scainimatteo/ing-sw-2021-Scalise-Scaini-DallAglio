package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.App;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;

import it.polimi.ingsw.model.card.LeaderCard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.net.URL;

public class LeaderCardSelector implements Initializable{
	ArrayList<LeaderCard> to_delete = new ArrayList<LeaderCard>();
	SimplePlayer player;

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
		// this.player = App.getMyPlayer();
	}

	public void leaderCard1Click(){
		to_delete.add(player.getLeaderCards().get(1));
		// leader_card_1.setOpacity(num)

		if (checkLeader()){
			// finisci e manda arraylist
		} 
	}

	public void leaderCard2Click(){
		to_delete.add(player.getLeaderCards().get(2));
		// leader_card_2.setOpacity(num)

		if (checkLeader()){
			// finisci e manda arraylist
		} 
	}

	public void leaderCard3Click(){
		to_delete.add(player.getLeaderCards().get(3));
		// leader_card_3.setOpacity(num)

		if (checkLeader()){
			// finisci e manda arraylist
		} 
	}

	public void leaderCard4Click(){
		to_delete.add(player.getLeaderCards().get(4));
		// leader_card_4.setOpacity(num)

		if (checkLeader()){
			// finisci e manda arraylist
		} 
	}

	private boolean checkLeader(){
		return to_delete.size() == 2;
	}
}
