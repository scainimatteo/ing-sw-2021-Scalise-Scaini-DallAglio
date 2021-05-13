package it.polimi.ingsw.model.player.track;

import java.io.Serializable;

public class Tile implements Cloneable, Serializable {
	private static final long serialVersionUID = 786L;
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

	public int getVictoryPoints() {
		return this.victory_points;
	}

	public Object clone() {
		Object clone = null;

		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return clone;
	}
}
