package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.player.track.SoloFaithTrack;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

public class SoloPlayer extends Player{
	public SoloPlayer(String nickname, SoloFaithTrack track){
		super(nickname, track);
	}

	/**
	 * SOLOFAITHTRACK METHODS
	 */
	//TODO: find another way, avoinding casting
	public VaticanReports moveForwardBlackMarker(int number_of_times){
		return ((SoloFaithTrack) super.track).moveForwardBlackMarker(number_of_times);
	}

	public int getBlackMarkerPosition(){
		return ((SoloFaithTrack) super.track).getBlackMarkerPosition();
	}
}
