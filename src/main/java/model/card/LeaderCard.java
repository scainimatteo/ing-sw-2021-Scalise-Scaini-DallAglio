package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

public abstract class LeaderCard extends Card implements Cloneable {
	protected LeaderAbility ability;
	protected boolean activated;

	public boolean isActive() {
		return this.activated;
	}
	
	public LeaderAbility getAbility(){
		return this.ability;
	}

	public void activateLeaderCard() {
		this.activated = true;
		return;	
	}

	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
}
