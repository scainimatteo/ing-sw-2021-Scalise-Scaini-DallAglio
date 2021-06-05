package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

public abstract class LeaderCard extends Card implements Cloneable, Serializable {
	private static final long serialVersionUID = 883L;
	protected LeaderAbility ability;
	protected boolean activated;
	protected String front_path;

	public boolean isActive() {
		return this.activated;
	}
	
	public LeaderAbility getAbility(){
		return this.ability;
	}

	public String getFrontPath(){
		return this.front_path;
	}

	public void activateLeaderCard() {
		this.activated = true;
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

	/**
	 * @param player the Player that wants to activate the LeaderCard
	 * @return true if the LeaderCard is activable
	 */
	public abstract boolean isActivable(Player player);

	/**
	 * Print the cost of the LeaderCard, each type of LeaderCard has its own
	 */
	protected abstract String printTop();

	/**
	 * Print the bottom of the LeaderCard using the methods of the LeaderAbility
	 */
	protected String printBottom(){
		return ability.toString();
	}

	/**
	 * @return the String representation of the LeaderCard
	 */
	public String toString(){
		return "/-----------------\\\n" + printTop() + "|-------(" + String.valueOf(this.victory_points) + ")-------|\n" + printBottom() + "\\-----------------/\n";
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
