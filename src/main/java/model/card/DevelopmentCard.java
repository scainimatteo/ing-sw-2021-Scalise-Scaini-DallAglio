package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.ProductionInterface;

public class DevelopmentCard extends Card implements ProductionInterface {
	private static final long serialVersionUID = 8L;
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

	public Resource[] getCost(){
		Resource[] to_return = this.cost.clone();
		return to_return;
	}

	public Production getProduction(){
		return this.production;
	}
	
	public Resource[] getRequiredResources(){
		return this.production.getRequiredResources();
	}

	public Resource[] getProducedResources(){
		return this.production.getProducedResources();
	}

	public void setRequiredResources(Resource[] cost){
		return;
	}
	
	public void setProducedResources(Resource[] new_resource) {
		return;
	}

	/**
	* applies a discount to the given card 
	*
	* @param discount is the discount to be applied
	* @return a new card with discount applied
	*/ 
	public DevelopmentCard applyDiscount (Resource[] discount){
		Resource[] temp = cost.clone();
		for (int i = 0; i < temp.length; i++){
			for(int j = 0; j < discount.length; j++){
				if (temp[i].equals(discount[j])) {
					temp[i] = null;
					discount[j] = null;
				}
			}
		}
		return new DevelopmentCard (victory_points, production, temp, level, id);
	}

	public Resource[] activateProduction(){
		return this.production.activateProduction();
	}
}
