package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

import java.util.Iterator;

public class LeaderCardLevelCost extends LeaderCard {
	private CardLevel[] requirements;
	
	public LeaderCardLevelCost (int points, LeaderAbility ability, CardLevel[] requirements, int id) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
	}

	public CardLevel[] getRequirements() {
		CardLevel[] to_return = this.requirements.clone();
		return to_return;
	}

	public boolean isActivable(Player player){
		CardLevel[] req = this.getRequirements();
		Iterator<DevelopmentCard> iterator = player.getDevCardIterator();
		boolean to_return = true;
		CardLevel tmp;

		while (iterator.hasNext()){
			tmp = iterator.next().getCardLevel();

			for (int i = 0; i < req.length; i ++){
				if (req[i].equals(tmp)){
					req[i] = null;
					break;
				} 
			}
		}

		for (int j = 0; j < req.length; j ++){
			if (req[j] != null){
				to_return = false;
				break;
			} 
		}

		return to_return;
	}
	
}

/*
/-----------------\
|                 |
|                 |
|                 |
|                 |
|                 |
|-------(X)-------|
|                 |
|                 |
|                 |
|                 |
|                 |
|                 |
|                 |
\-----------------/
*/
