package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.card.LeaderAbility;

public class LeaderCard extends Card {
	private LeaderAbility ability;
	private CardLevel[] requirements;
	private boolean activated;

	public boolean isActive() {
		return false;
	}

	public void activateLeaderCard() {
		return;
	}
}
