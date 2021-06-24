package it.polimi.ingsw.view.simplemodel;

import java.util.ArrayList;
import java.util.HashMap;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;

import java.io.Serializable;

public class SimpleSoloPlayer extends SimplePlayer {
	private static final long serialVersionUID = 418L;
	int black_marker_position;

	public SimpleSoloPlayer(String nickname, Cell[] cell_track, Cell faith_marker, Tile[] v_r_tiles, SimpleWarehouse warehouse, HashMap<Resource, Integer> strongbox, ArrayList<LeaderCard> leader_cards_deck, SimpleDevelopmentCardSlot slots, int black_marker_position){
		super(nickname, cell_track, faith_marker, v_r_tiles, warehouse, strongbox, leader_cards_deck, slots);
		this.black_marker_position = black_marker_position;
	}

	/**
	 * @return the position of the Cell the Black Cross is on
	 */
	public int getBlackMarkerPosition() {
		return this.black_marker_position;
	}
}
