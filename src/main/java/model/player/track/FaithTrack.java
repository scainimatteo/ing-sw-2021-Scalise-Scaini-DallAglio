package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.util.Observer;

import java.io.*;

public class FaithTrack implements Observer<VaticanReports> {
	protected Cell[] track;
	protected Tile[] vatican_report_tiles;
	protected Cell faith_marker;

	public FaithTrack(Cell[] track, Tile[] vatican_report_tiles){
		this.track = track;
		this.vatican_report_tiles = vatican_report_tiles;
		this.faith_marker = this.track[0];
	}

	/**
	 * @param number_of_times represent how many cells the marker has to move
	 * @return the vatican report activated if the marker reaches or overcomes a pope space
	 */
	public VaticanReports moveForward(int number_of_times) {
		int new_position = number_of_times + faith_marker.getPosition();
		this.faith_marker = track[new_position];

		for (int i = new_position; i > (new_position - number_of_times); i --){
			if (track[i].isPopeSpace()){
				if (checkCell(this.track[i].whichVaticanReport().getIndex())){
					this.vatican_report_tiles[this.track[i].whichVaticanReport().getIndex()].activateVaticanReport();
					return this.track[i].whichVaticanReport();
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
	 *
	 * TODO: alla riga 51 il faith_marker si è spostato quindi il player attualmente non si trova su un vatican report, ma si può trovare oltre e quindi non venire considerato nell'attivazione
	 */
	public void activateVaticanReport(VaticanReports vr_param){
		if (this.checkCell(vr_param.getIndex())){
			if (faith_marker.whichVaticanReport() != null && faith_marker.whichVaticanReport().equals(vr_param)){
				this.vatican_report_tiles[vr_param.getIndex()].activateVaticanReport();
			} 
		} 

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
			if (t.isActive()) {
				i += t.getVictoryPoints();
			}
		}

		return i;
	}

	public void update(VaticanReports vr_param){
		this.activateVaticanReport(vr_param);
	}
}
