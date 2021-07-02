package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.util.ANSI;

public class FaithTrack {
	protected Cell[] track;
	protected Tile[] vatican_report_tiles;
	protected Cell faith_marker;

	public FaithTrack(Cell[] track, Tile[] vatican_report_tiles){
		this.track = track;
		this.vatican_report_tiles = vatican_report_tiles;
		this.faith_marker = this.track[0];
	}

	/**
	 * Persistence only - recreate a FaithTrack from the match saved in memory
	 */
	public FaithTrack(Cell[] track, Tile[] vatican_report_tiles, int marker_position) {
		this(track, vatican_report_tiles);
		for (Cell cell: track) {
			if (cell.getPosition() == marker_position) {
				this.faith_marker = cell;
				break;
			}
		}
	}

	/**
	 * @param number_of_times represent how many cells the marker has to move
	 * @return the vatican report activated if the marker reaches or overcomes a pope space
	 */
	public VaticanReports moveForward(int number_of_times) {
		// the new position can be max the last position on the FaithTrack
		int new_position = number_of_times + faith_marker.getPosition() < getLastPosition()? number_of_times + faith_marker.getPosition() : getLastPosition();
		this.faith_marker = track[new_position];

		for (int i = new_position; i > (new_position - number_of_times); i--){
			VaticanReports vatican_report = this.track[i].whichVaticanReport();
			if (this.track[i].isPopeSpace()){
				if (checkCell(vatican_report.getIndex())){
					this.vatican_report_tiles[vatican_report.getIndex()].activateVaticanReport();
					return vatican_report;
				}
			}
		}

		return null;
	}

	/**
	 * @param i is the index in the array of tiles
	 * @return true if the tile at the given index is not null and has not been activated yet
	 */
	protected boolean checkCell(int i){
		return this.vatican_report_tiles[i] != null && !(this.vatican_report_tiles[i].isActive());
	}

	/**
	 * @param vr_param is the vatican report to be activated
	 */
	public void activateVaticanReport(VaticanReports vr_param){
		if (this.checkCell(vr_param.getIndex())){
			if (faith_marker.whichVaticanReport() != null && faith_marker.whichVaticanReport().equals(vr_param)){
				this.vatican_report_tiles[vr_param.getIndex()].activateVaticanReport();
			} 
		} 

	}

	/**
	 * Set a VaticanReport null if the Player missed the opportunity to activate it
	 *
	 * @param vatican_report the VaticanReport to remove
	 */
	public void removeVaticanReport(VaticanReports vatican_report) {
		this.vatican_report_tiles[vatican_report.getIndex()] = null;
	}

	public Cell[] getCellTrack(){
		return this.track;
	}

	public Cell getMarker(){
		return this.faith_marker;
	}

	public Tile[] getTiles(){
		return this.vatican_report_tiles;
	}

	public int getMarkerPosition(){
		return this.faith_marker.getPosition();
	}

	public int getMarkerVictoryPoints() {
		return this.faith_marker.getVictoryPoints();
	}

	public int getLastPosition() {
		return this.track[this.track.length - 1].getPosition();
	}

	public boolean checkIfTileIsActive(int index){
		return this.vatican_report_tiles[index].isActive();
	}

	/**
	 * @return the total points made from the VaticanReports
	 */
	public int getVaticanReportsPoints() {
		int i = 0;

		for (Tile t: this.vatican_report_tiles) {
			if (t != null && t.isActive()) {
				i += t.getVictoryPoints();
			}
		}

		return i;
	}
}
