package it.polimi.ingsw.model.game;

import java.io.Serializable;

import java.util.ArrayList;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.resources.Resource;

public class Turn implements Serializable {
	Player active_player;
	ArrayList<Resource> required_resources;
	ArrayList<Resource> produced_resources;
	boolean action_done;
	boolean must_discard;
	boolean final_turn;
	private static final long serialVersionUID = 8008L;


	public Turn (Player player, boolean final_turn){
		this.active_player = player;
		this.required_resources = new ArrayList<Resource>();
		this.produced_resources = new ArrayList<Resource>();
		this.action_done = false;
		this.must_discard = false;
		this.final_turn = final_turn;
	}

    public Player getPlayer(){
		return this.active_player;
	}

	public ArrayList<Resource> getRequiredResources() {
		return this.required_resources;
	}

	public ArrayList<Resource> getProducedResources() {
		return this.produced_resources;
	}

	public boolean hasDoneAction() {
		return this.action_done;
	}
	
	public boolean mustDiscard() {
		return this.must_discard;
	}

	public boolean isFinal() {
		return this.final_turn;
	}
}
