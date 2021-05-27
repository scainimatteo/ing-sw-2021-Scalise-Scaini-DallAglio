package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serializable;

public class ActivateLeaderMessage extends LeaderCardMessage implements Serializable {
	private static final long serialVersionUID = 4353L;

	public ActivateLeaderMessage(LeaderCard leader_card){
		this.leader_card = leader_card;
	}

	public void useMessage(Controller controller) {
		controller.handleActivateLeader(this.player, this.leader_card);
	}
}
