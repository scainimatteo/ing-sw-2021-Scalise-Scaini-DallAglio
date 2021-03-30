package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;

public class DevelopmentCard extends Card {
	private Production production;
	private Resource[] cost;
	private CardLevel level;

	public DevelopmentCard (int points, Production production, Resource [] cost, CardLevel level) {
		this.victory_points = points;
		this.cost = cost;
		this.production = production;
		this.level = level;
	}	

	public Resource[] useCard(Resource[] resources_input) {
		return production.activateProduction(resources_input);
	}
}

