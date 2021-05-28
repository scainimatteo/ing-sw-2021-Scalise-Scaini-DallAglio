package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.model.card.LeaderCard;

import java.io.Serializable;

public abstract class LeaderCardMessage extends Message implements Serializable {
	private static final long serialVersionUID = 33255L;
	public LeaderCard leader_card = null;
}
