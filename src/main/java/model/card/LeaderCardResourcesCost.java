package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.resources.Resource;

public class LeaderCardResourcesCost extends LeaderCard {
	private Resource[] requirements;

	public LeaderCardResourcesCost (int points, LeaderAbility ability, Resource[] requirements, int id) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
	}

	public Resource[] getRequirements() {
		Resource[] to_return = this.requirements.clone();
		return to_return;
	}
}
