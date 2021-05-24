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

	public void setPlayer(Player player){
		this.active_player = player;
	}

	public ArrayList<Resource> getRequiredResources() {
		return this.required_resources;
	}

	public void setRequiredResources(ArrayList<Resource> required) {
		this.required_resources = required;
	}

	public ArrayList<Resource> getProducedResources() {
		return this.produced_resources;
	}

	public void setProducedResources(ArrayList<Resource> produced) {
		this.produced_resources = produced;
	}

	public boolean hasDoneAction() {
		return this.action_done;
	}

	public void setDoneAction(boolean value) {
		this.action_done = value;
	}
	
	public boolean mustDiscard() {
		return this.must_discard;
	}

	public void setDiscard(boolean value) {
		this.action_done = value;
	}

	public boolean isFinal() {
		return this.final_turn;
	}

	public void setFinal(boolean value) {
		this.final_turn = value;
	}
}
