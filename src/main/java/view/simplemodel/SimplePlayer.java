package it.polimi.ingsw.view.simplemodel;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import java.util.ArrayList;
import java.util.HashMap;

import java.io.Serializable;

public class SimplePlayer implements Serializable {
	private static final long serialVersionUID = 7775L;
	protected String nickname;
	protected Cell[] track;
	protected Cell faith_marker;
	protected Tile[] vatican_reports;
	protected SimpleWarehouse warehouse;
	protected HashMap<Resource, Integer> strongbox;
	protected ArrayList<LeaderCard> leader_cards_deck;
	protected SimpleDevelopmentCardSlot development_card_slots;

	public SimplePlayer(String nickname, Cell[] cell_track, Cell faith_marker, Tile[] v_r_tiles, SimpleWarehouse warehouse, HashMap<Resource, Integer> strongbox, ArrayList<LeaderCard> leader_cards_deck, SimpleDevelopmentCardSlot slots){
		this.nickname = nickname;
		this.track = cell_track;
		this.faith_marker = faith_marker;
		this.vatican_reports = v_r_tiles;
		this.warehouse = warehouse;
		this.strongbox = strongbox;
		this.development_card_slots = slots; 
		this.leader_cards_deck = leader_cards_deck;
	}

	public String getNickname(){
		return this.nickname;
	}

	public Cell[] getFaithTrack() {
		return this.track;
	}

	public Cell getMarker() {
		return this.faith_marker;
	}
	
	public Tile[] getReports() {
		return this.vatican_reports;
	}

	public ArrayList<LeaderCard> getDeck(){
		return this.leader_cards_deck;
	}

	public SimpleWarehouse getWarehouse(){
		return this.warehouse;
	}

	public ArrayList<Resource> getTopResource(){
		return this.warehouse.getTopResource();
	}

	public ArrayList<Resource> getMiddleResources(){
		return this.warehouse.getMiddleResources();
	}

	public ArrayList<Resource> getBottomResources(){
		return this.warehouse.getBottomResources();
	}

	public HashMap<Resource, Integer> getStrongbox(){
		return this.strongbox;
	}

	public SimpleDevelopmentCardSlot getDevelopmentCardsSlots() {
		return this.development_card_slots;
	}
	
	public ArrayList<DevelopmentCard> getFirstColumn(){
		return this.development_card_slots.getFirstColumn();
	}

	public ArrayList<DevelopmentCard> getSecondColumn(){
		return this.development_card_slots.getSecondColumn();
	}

	public ArrayList<DevelopmentCard> getThirdColumn(){
		return this.development_card_slots.getThirdColumn();
	}

	public ArrayList<DevelopmentCard> getTopCards(){
		return this.development_card_slots.getTopCards();
	}
}
