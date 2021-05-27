package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.ProductionInterface;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class DevelopmentCard extends Card implements ProductionInterface, Serializable {
	private static final long serialVersionUID = 8L;
	private Production production;
	private ArrayList<Resource> cost;
	private CardLevel level;

	public DevelopmentCard (int points, Production production, ArrayList<Resource> cost, CardLevel level, int id) {
		this.victory_points = points;
		this.cost = cost;
		this.production = production;
		this.level = level;
		this.id = id;
	}

	public CardLevel getCardLevel() {
		return this.level;
	}

	public ArrayList<Resource> getCost(){
		return (ArrayList<Resource>)this.cost.clone();
	}

	public Production getProduction(){
		return this.production;
	}
	
	public ArrayList<Resource> getRequiredResources(){
		return this.production.getRequiredResources();
	}

	public ArrayList<Resource> getProducedResources(){
		return this.production.getProducedResources();
	}

	public void setRequiredResources(ArrayList<Resource> cost){
		return;
	}
	
	public void setProducedResources(ArrayList<Resource> new_resource) {
		return;
	}

	/**
	* applies a discount to the given card 
	*
	* @param discount is the discount to be applied
	* @return a new card with discount applied
	*/ 
	public DevelopmentCard applyDiscount (ArrayList<Resource> discount){
		ArrayList<Resource> temp = (ArrayList<Resource>) cost.clone();
		for (Resource x : discount){
			temp.remove(x);
		}
		return new DevelopmentCard (victory_points, production, temp, level, id);
	}

	public ArrayList<Resource> activateProduction(){
		return this.production.activateProduction();
	}
}

/**
 * -------------------	X
 * |                 |	X
 * -------------------	X
 * |                 |	X
 * | 0 sp 0 sp 0 sp  |  X
 * |                 |	X
 * -------------------	X
 * |                 |	X
 * |                 |	X
 * |                 |	X
 * |                 |	X
 * |                 |	X
 * |                 |	X
 * |                 |	X
 * -------------------	X
 */
