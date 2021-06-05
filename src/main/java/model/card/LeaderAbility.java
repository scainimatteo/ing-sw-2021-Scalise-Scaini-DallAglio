package it.polimi.ingsw.model.card;

import java.io.Serializable;

import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.ProductionAbility;
import it.polimi.ingsw.model.card.DiscountAbility;

public abstract class LeaderAbility implements Serializable {
	private static final long serialVersionUID = 43723L;

	public boolean checkAbility(WhiteMarblesAbility target){
		return false;
	}

	public boolean checkAbility(ExtraSpaceAbility target){
		return false;
	}

	public boolean checkAbility(ProductionAbility target){
		return false;
	}

	public boolean checkAbility(DiscountAbility target){
		return false;
	}

	public abstract String toString();
}
