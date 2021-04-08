package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.player.track.FaithTrackSinglePlayer;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

public class SinglePlayer extends Player{
	private FaithTrackSinglePlayer track_sp;

	public SinglePlayer(String nickname, Cell[] cell_track, Tile[] v_r_tiles){
		super(nickname, cell_track, v_r_tiles);
		this.track_sp = new FaithTrackSinglePlayer(cell_track, v_r_tiles);
	}

	/**
	 * FAITHTRACKSINGLEPLAYER METHODS
	 */
	public VaticanReports moveForwardBlackMarker(int number_of_times){
		return this.track_sp.moveForwardBlackMarker(number_of_times);
	}

	public int getBlackMarkerPosition(){
		return this.track_sp.getBlackMarkerPosition();
	}
}
