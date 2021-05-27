package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serializable;

public class DiscardLeaderMessage extends LeaderCardMessage implements Serializable {
	private static final long serialVersionUID = 33563L;

	public DiscardLeaderMessage(LeaderCard leader_card){
		this.leader_card = leader_card;
	}

	public void useMessage(Controller controller) {
		controller.handleDiscardLeader(this.player, this.leader_card);
	}
}
