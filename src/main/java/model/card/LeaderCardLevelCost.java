package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;

public class LeaderCardLevelCost extends LeaderCard {
	private CardLevel[] requirements;
	
	public LeaderCardLevelCost (int points, LeaderAbility ability, CardLevel[] requirements, int id) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
	}

	public CardLevel[] getRequirements() {
		CardLevel[] to_return = this.requirements.clone();
		return to_return;
	}
}
