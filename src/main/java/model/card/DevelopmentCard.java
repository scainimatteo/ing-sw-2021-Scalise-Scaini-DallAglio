package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.ProductionInterface;

import it.polimi.ingsw.view.Viewable;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Set;

public class DevelopmentCard extends Card implements ProductionInterface, Viewable, Serializable {
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

	public HashMap<Resource, Integer> numOfCost(){
		HashMap<Resource, Integer> to_return = new HashMap<Resource, Integer>();

		for (Resource res : cost){
			if (to_return.containsKey(res)){
				to_return.put(res, to_return.get(res) + 1);
			} else {
				to_return.put(res, 1);
			}
		}

		return to_return;
	}

	public String printText(){
		String top = "-------------------";
		String mid = "|               |";

		/**
		 * level
		 */
		String spaces = "                 ";
		String tmp = "|  " + String.valueOf(level.getLevel()) + "  " + level.getColor().toString();
		String level = tmp + spaces.substring(tmp.length());
		level += " |";

		/**
		 * cost
		 */
		HashMap<Resource, Integer> cost_to_print = this.numOfCost();
		Set<Resource> cost_set = cost_to_print.keySet();
		Resource[] cost_array = cost_set.toArray(new Resource[cost_set.size()]);

		String cost_string = "| " + String.valueOf(cost_to_print.get(cost_array[0])) + cost_array[0].getAbbreviation();
		if (cost_array.length >= 2){
			cost_string += " " + String.valueOf(cost_to_print.get(cost_array[1])) + cost_array[1].getAbbreviation();
		} 
		if (cost_array.length >= 3){
			cost_string += " " + String.valueOf(cost_to_print.get(cost_array[2])) + cost_array[2].getAbbreviation();
		} 

		/**
		 * production
		 */
		String[] production_string = this.production.productionToText();
		String prod1 = "| " + production_string[0] + " |";
		String prod2 = "| " + production_string[1] + " |";
		String prod3 = "| " + production_string[2] + " |";
		String prod4 = "| " + production_string[3] + " |";
		String prod5 = "| " + production_string[4] + " |";

		String to_return = top + level + top + mid + cost_string + mid + top + mid + prod1 + prod2 +prod3 + prod4 + prod5 + mid + top;

		return to_return;
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
