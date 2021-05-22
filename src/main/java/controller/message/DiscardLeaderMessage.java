package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.controller.Controller;

public class DiscardLeaderMessage extends LeaderCardMessage {
	public DiscardLeaderMessage(LeaderCard leader_card){
		this.leader_card = leader_card;
	}

	public void useMessage(Controller controller) {
		controller.handleDiscardLeader(this.player, this.leader_card);
	}
}
