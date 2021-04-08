package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import java.util.HashMap;

public class Player {
	private String nickname;
	private FaithTrack track;
	private Warehouse warehouse;
	private StrongBox strongbox;
	private DevelopmentCardsSlots development_card_slots;
	private LeaderCard[] leader_cards_deck;

	public Player(String nickname, Cell[] cell_track, Tile[] v_r_tiles){
		this.nickname = nickname;
		this.track = new FaithTrack(cell_track, v_r_tiles);
		this.warehouse = new Warehouse();
		this.strongbox = new StrongBox();
		this.development_card_slots = new DevelopmentCardsSlots();
		this.leader_cards_deck = new LeaderCard[4];
	}

	/**
	 * @param card is the DevelopmentCard to be checked
	 * @return true if the warehouse or the strongbox contains the resources requested for the buy
	 */
	public boolean isBuyable(DevelopmentCard card){
		Resource[] tmp = card.getCost();

		if(!(warehouse.areContainedInWarehouse(tmp) || strongbox.areContainedInStrongbox(tmp))){
			for (Resource res : tmp){
				if(res != null){
					return false;
				}
			}
		}

		return true;
	}

	public String getNickname(){
		return this.nickname;
	}

	/**
	 * WAREHOUSE METHODS
	 */
	public Resource getTopResource(){
		return this.warehouse.getTopResource();
	}

	public Resource[] getMiddleResources(){
		return this.warehouse.getMiddleResources();
	}

	public Resource[] getBottomResources(){
		return this.warehouse.getBottomResources();
	}

	public int tryToInsert(Resource[] new_resources){
		return this.warehouse.tryToInsert(new_resources);
	}

	public Resource[] getFromWarehouse(Resource resource_type, int quantity){
		return this.warehouse.getFromWarehouse(resource_type, quantity);
	}

	public void clearWarehouse(){
		this.warehouse.clearWarehouse();
	}

	/**
	 * STRONGBOX METHODS
	 */
	public void insertResources(Resource[] new_resources){
		this.strongbox.insertResources(new_resources);
	}

	public HashMap<Resource, Integer> getStrongBox(){
		return this.strongbox.getStrongBox();
	}

	public Resource[] removeResources(Resource resource_type, int quantity){
		return this.strongbox.removeResources(resource_type, quantity);
	}

	/**
	 * DEVCARDSSLOTS METHODS
	 * TODO: in buyCard after the call of the method remove the resources from the wh then from the sb
	 */
	public void buyCard(DevelopmentCard card, int position){
		this.development_card_slots.buyCard(card, position);
	}

	public DevelopmentCard[] getTopCards(){
		return this.development_card_slots.getTopCards();
	}

	public DevelopmentCard[] getCard(int position){
		return this.development_card_slots.getCard(position);
	}

	/**
	 * FAITHTRACK METHODS
	 */
	public VaticanReports moveForward(int number_of_times){
		return this.track.moveForward(number_of_times);
	}

	public int getMarkerPosition(){
		return this.track.getMarkerPosition();
	}
}
