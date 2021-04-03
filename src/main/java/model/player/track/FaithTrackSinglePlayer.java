package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;

import java.io.*;

public class FaithTrackSinglePlayer extends FaithTrack {
	private Cell black_marker;

	public FaithTrackSinglePlayer(Cell[] track, Tile[] vatican_report_tiles){
		super(track, vatican_report_tiles);
		this.black_marker = track[0];
		System.out.println(vatican_report_tiles);
	}

	public VaticanReports moveForwardBlackMarker(int number_of_times) {
		int new_position = number_of_times + this.black_marker.getPosition();
		this.black_marker = super.track[new_position];

		for (int i = new_position; i > (new_position - number_of_times); i --){
			if(super.track[i].isPopeSpace()){
				if(super.checkCell(i)){
					super.activateVaticanReport(super.track[i].whichVaticanReport());
					return super.track[i].whichVaticanReport();
				}
			}
		}

		return null;
	}

	public int getBlackMarkerPosition(){
		return this.black_marker.getPosition();
	}
}
