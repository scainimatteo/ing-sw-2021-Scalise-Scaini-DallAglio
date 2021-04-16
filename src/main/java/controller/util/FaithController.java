package it.polimi.ingsw.controller.util;

import it.polimi.ingsw.model.player.Player;

public class FaithController {
	public final Player player;
	public final int faith_gained;
	public final int faith_to_redistribute;

	public FaithController(Player player, int gained, int to_redistribute){
		this.player = player;
		this.faith_gained = gained;
		this.faith_to_redistribute = to_redistribute;
	}
}
