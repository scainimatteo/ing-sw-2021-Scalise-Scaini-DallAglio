package it.polimi.ingsw.model.card;

import it.polimi.ingsw.view.Viewable;

import java.io.Serializable;

public abstract class LeaderAbility implements Serializable {
	private static final long serialVersionUID = 5L;

	public boolean checkAbility (LeaderAbility target){
		return false;
	}

    public String printText(){
		return "\n qualcosa non ha funzionato\n";
	}
}
