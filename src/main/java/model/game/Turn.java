package it.polimi.ingsw.model.game;

import java.io.Serializable;

import java.util.ArrayList;

import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.util.observer.ModelObservable;

public class Turn extends ModelObservable implements Serializable {
	private static final long serialVersionUID = 8008L;
	transient Player active_player;
	private ArrayList<Resource> required_resources;
	private ArrayList<Resource> produced_resources;
	private boolean action_done;
	private boolean must_discard;
	private boolean final_round;
	private boolean setup_done;
	private String nickname;

	public Turn(Player player){
		this.active_player = player;
		this.required_resources = new ArrayList<Resource>();
		this.produced_resources = new ArrayList<Resource>();
		this.action_done = false;
		this.must_discard = false;
		this.final_round = false;
		this.nickname = player.getNickname();
	}

	public void clearTurn(Player player) {
		this.active_player = player;
		this.required_resources.clear();
		this.produced_resources.clear();
		this.action_done = false;
		this.must_discard = false;
		this.nickname = player.getNickname();
		this.notifyTurn();
	}

	public Player getPlayer(){
		return this.active_player;
	}

	public String getNickname(){
		return this.nickname;
	}

	public void setPlayer(Player player){
		this.active_player = player;
		this.nickname = player.getNickname();
		this.notifyTurn();
	}

	public ArrayList<Resource> getRequiredResources() {
		return this.required_resources;
	}

	public void addRequiredResources(ArrayList<Resource> required) {
		this.required_resources.addAll(required);
		this.notifyTurn();
	}

	public void removeRequiredResources(ArrayList<Resource> required) {
		for (Resource x : required){
			required_resources.remove(x);
		}
		this.notifyTurn();
	}

	public void clearRequiredResources(){
		required_resources.clear();
		this.notifyTurn();
	}

	public ArrayList<Resource> getProducedResources() {
		return this.produced_resources;
	}

	public void addProducedResources(ArrayList<Resource> produced) {
		this.produced_resources.addAll(produced);
		this.notifyTurn();
	}

	public void removeProducedResources(ArrayList<Resource> required) {
		for (Resource x : required){
			produced_resources.remove(x);
		}
		this.notifyTurn();
	}
	
	public void clearProducedResources(){
		produced_resources.clear();
		this.notifyTurn();
	}

	public boolean hasDoneAction() {
		return this.action_done;
	}

	public void setDoneAction(boolean value) {
		this.action_done = value;
		this.notifyTurn();
	}
	
	public boolean mustDiscard() {
		return this.must_discard;
	}

	public void setDiscard(boolean value) {
		this.action_done = value;
		this.notifyTurn();
	}

	public boolean isFinal() {
		return this.final_round;
	}

	public boolean hasDoneSetup() {
		return this.setup_done;
	}

	public void setupDone(boolean value) {
		this.setup_done = value;
		this.notifyTurn();
	}

	public void setFinal(boolean value) {
		this.final_round = value;
		this.notifyTurn();
	}

	public void notifyTurn() {
		notifyModel(new ViewUpdate(this));
	}
}
