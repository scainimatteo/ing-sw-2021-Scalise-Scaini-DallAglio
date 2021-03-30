package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.track.VaticanReports;

public class FaithTrack {
	protected Cell[] track;
	protected Tile[] vatican_report_tiles;
	protected Cell faith_marker;

	public FaithTrack(Cell[] track, Tile[] vatican_report_tiles){
		this.track = track;
		this.vatican_report_tiles = vatican_report_tiles;
		this.faith_marker = this.track[0];
	}

	public VaticanReports moveForward(int number_of_times) {
		int new_position = number_of_times + faith_marker.getPosition();
		faith_marker = track[new_position];

		for (int i = new_position; i < (new_position - number_of_times); i --){
			if(track[i].isPopeSpace()){
				if(!(vatican_report_tiles[track[i].whichVaticanReport().getIndex()].isActive())){
					return track[i].whichVaticanReport();
				}
			}
		}

		return null;
	}
}
