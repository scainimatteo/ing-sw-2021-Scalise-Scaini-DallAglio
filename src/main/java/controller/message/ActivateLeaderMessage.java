package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.card.LeaderCard;

public class ActivateLeaderMessage implements LeaderCardMessage {
	public ActivateLeaderMessage(LeaderCard leader_card){
		this.leader_card = leader_card;
	}

	public void useMessage(Controller controller) {
		controller.handleActivateLeader(this.player, this.leader_card);
	}
}
