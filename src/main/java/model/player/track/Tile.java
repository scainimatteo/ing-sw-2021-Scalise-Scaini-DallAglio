package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.model.player.track.VaticanReports;

public class Tile {
	private VaticanReports report;
	private int victory_points;
	private boolean active;

	public Tile(VaticanReports report, int victory_points){
		this.report = report;
		this.victory_points = victory_points;
		this.active = false;
	}

	public void activateVaticanReport(){
		this.active = true;
	}

	public boolean isActive(){
		return this.active;
	}
}
