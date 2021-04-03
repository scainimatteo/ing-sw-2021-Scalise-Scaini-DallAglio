package it.polimi.ingsw.model.player.track;

public class Cell {
	private int position;
	private int victory_points;
	private VaticanReports report;
	private boolean pope_space;
	private boolean last_cell;

	public Cell(int position, int victory_points, VaticanReports report, boolean pope_space, boolean last_cell){
		this.position = position;
		this.victory_points = victory_points;
		this.report = report;
		this.pope_space = pope_space;
		this.last_cell = last_cell;
	}

	public boolean isPopeSpace(){
		return this.pope_space;
	}

	public boolean isLastCell(){
		return last_cell;
	}

	public VaticanReports whichVaticanReport(){
		return this.report;
	}

	public int getVictoryPoints(){
		return this.victory_points;
	}

	public int getPosition(){
		return this.position;
	}
}
