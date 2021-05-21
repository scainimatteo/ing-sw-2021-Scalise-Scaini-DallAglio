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
		ArrayList<Resource> to_return = (ArrayList<Resource>)this.cost.clone();
		return to_return;
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

	// TODO: move into view
//	private HashMap<Resource, Integer> numOfCost(){
//		HashMap<Resource, Integer> to_return = new HashMap<Resource, Integer>();
//
//		for (Resource res : cost){
//			if (to_return.containsKey(res)){
//				to_return.put(res, to_return.get(res) + 1);
//			} else {
//				to_return.put(res, 1);
//			}
//		}
//
//		return to_return;
//	}
//
//	public String printText(){
//		String top = "|-----------------|\n";
//		String mid = "|                 |\n";
//
//		/**
//		 * level
//		 */
//		String spaces = "                 ";
//		String tmp = "|  " + String.valueOf(level.getLevel()) + "  " + level.getColor().toString();
//		String level = tmp + spaces.substring(tmp.length());
//		level += " |\n";
//
//		/**
//		 * cost
//		 */
//		HashMap<Resource, Integer> cost_to_print = this.numOfCost();
//		Set<Resource> cost_set = cost_to_print.keySet();
//		ArrayList<Resource> cost_array = cost_set.toArray(new Resource[cost_set.size()]);
//
//		String cost_string = "| " + String.valueOf(cost_to_print.get(cost_array[0])) + " " + cost_array[0].getAbbreviation();
//		if (cost_array.length >= 2){
//			cost_string += " " + String.valueOf(cost_to_print.get(cost_array[1])) + " " + cost_array[1].getAbbreviation();
//			if (cost_array.length >= 3){
//				cost_string += " " + String.valueOf(cost_to_print.get(cost_array[2])) + " " + cost_array[2].getAbbreviation();
//			} else {
//				cost_string += "     ";
//			}
//		} else {
//			cost_string += "          ";
//		}
//		cost_string += "  |\n";
//
//		/**
//		 * pv
//		 */
//		String pv_string = "";
//		if (getPoints() < 10){
//			pv_string = "|-------(" + String.valueOf(this.getPoints()) + ")-------|\n";
//		} else {
//			String vp = String.valueOf(this.getPoints());
//			pv_string = "|------(" + vp.substring(0,1) + " " + vp.substring(1) + ")------|\n";
//		}
//
//		/**
//		 * production
//		 */
//		String[] production_string = this.production.productionToText();
//		String prod1 = "| " + production_string[0] + "  |\n";
//		String prod2 = "| " + production_string[1] + "  |\n";
//		String prod3 = "| " + production_string[2] + "  |\n";
//		String prod4 = "| " + production_string[3] + "  |\n";
//		String prod5 = "| " + production_string[4] + "  |\n";
//
//		String to_return = top + level + top + mid + cost_string + mid + pv_string + mid + prod1 + prod2 + prod3 + prod4 + prod5 + mid + top;
//
//		return to_return;
//	}
//
//	public String printText(int index){
//		return null;
//	}
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
