package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.LeaderAbility;

public abstract class LeaderCard extends Card {
	protected LeaderAbility ability;
	protected boolean activated;

	public boolean isActive() {
		return this.activated;
	}

	public void activateLeaderCard() {
		this.activated = true;
		return;	
	}
//chiama metodo dell'interfaccia affinché possa fare overriding e printare il tipo di abilità
	public String cardType(){
		return ability.toString();
	}
}
