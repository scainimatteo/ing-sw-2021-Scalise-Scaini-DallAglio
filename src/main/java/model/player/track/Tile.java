package it.polimi.ingsw.model.player.track;

public class Tile implements Cloneable {
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
