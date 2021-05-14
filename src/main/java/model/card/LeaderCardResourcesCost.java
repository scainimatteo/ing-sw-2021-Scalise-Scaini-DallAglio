package it.polimi.ingsw.model.card;

// import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Player;

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

	public boolean isActivable(Player player){
		Warehouse warehouse = player.getWarehouse();
		StrongBox strongbox = player.getPlayerStrongBox();
		Resource[] tmp = this.getRequirements();
		boolean to_return = true;

		if ( !(warehouse.areContainedInWarehouse(tmp) || strongbox.areContainedInStrongbox(tmp)) ){
			for (Resource res : tmp){
				if (res != null){
					to_return = false;
				}
			}
		}

		return to_return;
		
	}
}
