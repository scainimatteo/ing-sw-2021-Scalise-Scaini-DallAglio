package it.polimi.ingsw.model.player.track;

import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;

public class FaithTrackSinglePlayer extends FaithTrack {
	private Cell black_marker;

	public FaithTrackSinglePlayer(Cell[] track, Tile[] vatican_report_tiles){
		super(track, vatican_report_tiles);
		this.black_marker = track[0];
	}

	public void moveForwardBlackMarker(int number_of_times) {
		return;
	}
}
