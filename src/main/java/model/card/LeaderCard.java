package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.LeaderAbility;

public abstract class LeaderCard extends Card {
	private LeaderAbility ability;
	private boolean activated;

	public LeaderCard (LeaderAbility new_ability){
		this.ability = new_ability;
	}	

	public LeaderCard public boolean isActive() {
		return this.activated;
	}

	public void activateLeaderCard() {
		this.activated = TRUE;
		return;	
	}
//chiama toString dell'interfaccia affinché possa fare overriding
	public String cardType(){
		return ability.toString();
	}
}
