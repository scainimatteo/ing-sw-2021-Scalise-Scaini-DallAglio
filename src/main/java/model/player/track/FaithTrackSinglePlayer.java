package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;

public class FaithTrackSinglePlayer extends FaithTrack {
	private Cell black_marker;

	public FaithTrackSinglePlayer(Cell[] track, Tile[] vatican_report_tiles){
		super(track, vatican_report_tiles);
		this.black_marker = track[0];
	}

	public VaticanReports moveForwardBlackMarker(int number_of_times) {
		int new_position = number_of_times + black_marker.getPosition();
		this.black_marker = track[new_position];

		for (int i = 0; i > (new_position - number_of_times); i --){
			if(track[i].isPopeSpace()){
				if(super.checkCell(i)){
					return track[i].whichVaticanReport();
				}
			}
		}

		return null;
	}
}
