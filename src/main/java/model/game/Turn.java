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
	boolean initialized;
	boolean final_turn;
	private static final long serialVersionUID = 8008L;


	public Turn (Player player){
		this.active_player = player;
		this.required_resources = new ArrayList<Resource>();
		this.produced_resources = new ArrayList<Resource>();
		this.action_done = false;
		this.must_discard = false;
		this.final_turn = false;
	}

	public void init(Player player){
		active_player = player;
		required_resources.clear();
		produced_resources.clear();
		action_done = false;
		must_discard = false;
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

	public void addRequiredResources(ArrayList<Resource> required) {
		this.required_resources.addAll(required);
	}

	public void removeRequiredResources(ArrayList<Resource> required) {
		for (Resource x : required){
			required_resources.remove(x);
		}
	}

	public ArrayList<Resource> getProducedResources() {
		return this.produced_resources;
	}

	public void addProducedResources(ArrayList<Resource> produced) {
		this.produced_resources.addAll(produced);
	}

	public void removeProducedResources(ArrayList<Resource> required) {
		for (Resource x : required){
			produced_resources.remove(x);
		}
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

	public boolean isInitialized() {
		return this.initialized;
	}

	public void setInitialized(boolean value) {
		this.initialized = value;
	}

	public void setFinal(boolean value) {
		this.final_turn = value;
	}
}
