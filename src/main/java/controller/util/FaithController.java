package it.polimi.ingsw.controller.util;

public class FaithController {
	public final int faith_gained;
	public final int faith_to_redistribute;

	public FaithController(int gained, int to_redistribute){
		this.faith_gained = gained;
		this.faith_to_redistribute = to_redistribute;
	}
}
