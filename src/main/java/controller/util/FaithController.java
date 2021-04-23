package it.polimi.ingsw.controller.util;

import it.polimi.ingsw.model.player.Player;

public class FaithController {
	private Player player;
	private int faith_gained;
	private int faith_to_redistribute;

	public FaithController(Player player, int gained, int to_redistribute){
		this.player = player;
		this.faith_gained = gained;
		this.faith_to_redistribute = to_redistribute;
	}

	public int getGainedFaith(){
		return this.faith_gained;
	}

	public int getToRedistributeFaith(){
		return this.faith_to_redistribute;
	}

	public boolean isSamePlayer(Player to_check){
		return this.player.equals(to_check);
	}
}
