package it.polimi.ingsw.model.card;

// import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;

import java.io.Serializable;

public class LeaderCardResourcesCost extends LeaderCard implements Serializable {
	private static final long serialVersionUID = 83762L;
	private ArrayList<Resource> requirements;

	public LeaderCardResourcesCost (int points, LeaderAbility ability, ArrayList<Resource> requirements, int id, String front_path) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
		this.front_path = front_path;
	}

	public ArrayList<Resource> getRequirements() {
		return requirements;
	}

	private HashMap<Resource, Integer> numOfCost(){
		HashMap<Resource, Integer> to_return = new HashMap<Resource, Integer>();

		for (Resource res : requirements){
			if (to_return.containsKey(res)){
				to_return.put(res, to_return.get(res) + 1);
			} else {
				to_return.put(res, 1);
			}
		}

		return to_return;
	}

	@Override
	protected String printTop(){
		String top = "|                 |\n|                 |\n";
		HashMap<Resource, Integer> cost_to_print = this.numOfCost();
		Set<Resource> cost_set = cost_to_print.keySet();
		Resource[] cost_array = cost_set.toArray(new Resource[cost_set.size()]);

		String cost_string = "| " + String.valueOf(cost_to_print.get(cost_array[0])) + " " + cost_array[0].getAbbreviation();
		if (cost_array.length >= 2){
			cost_string += " " + String.valueOf(cost_to_print.get(cost_array[1])) + " " + cost_array[1].getAbbreviation();
			if (cost_array.length >= 3){
				cost_string += " " + String.valueOf(cost_to_print.get(cost_array[2])) + " " + cost_array[2].getAbbreviation();
			} else {
				cost_string += "     ";
			}
		} else {
			cost_string += "          ";
		}
		cost_string += "  |\n";

		return top + cost_string + "|                 |\n|                 |\n";
	}
}
