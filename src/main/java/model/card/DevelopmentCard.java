package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;

public class DevelopmentCard extends Card {
	private Production production;
	private Resource[] cost;
	private CardLevel level;

	public DevelopmentCard (int points, Production production, Resource [] cost, CardLevel level, int id) {
		this.victory_points = points;
		this.cost = cost;
		this.production = production;
		this.level = level;
		this.id = id;
	}	

	public CardLevel getCardLevel() {
		return this.level;
	}

	public Resource[] useCard(Resource[] resources_input) {
		return production.activateProduction(resources_input);
	}
}

