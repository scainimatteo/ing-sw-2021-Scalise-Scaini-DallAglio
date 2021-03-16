package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;

public class DevelopmentCard extends Card {
	private Production production;
	private Resource[] cost;
	private CardLevel level;

	public Resource[] useCard(Resource[] resources_input) {
		return null;
	}

	public boolean meetRequirements(CardLevel level_to_test) {
		return false;
	}
}
