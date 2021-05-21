package it.polimi.ingsw.model.card;

// import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Player;
import java.util.ArrayList;

public class LeaderCardResourcesCost extends LeaderCard {
	private ArrayList<Resource> requirements;

	public LeaderCardResourcesCost (int points, LeaderAbility ability, ArrayList<Resource> requirements, int id) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
	}

	public ArrayList<Resource> getRequirements() {
		return requirements;
	}

	public String printText(){
		return null;
	}

	public String printText(int index){
		return null;
	}
}
