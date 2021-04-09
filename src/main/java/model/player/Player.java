package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import java.util.HashMap;

import java.lang.IllegalArgumentException;

public class Player {
	protected String nickname;
	protected FaithTrack track;
	protected Warehouse warehouse;
	protected StrongBox strongbox;
	protected DevelopmentCardsSlots development_card_slots;
	protected LeaderCard[] leader_cards_deck;

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
	public boolean[] isBuyable(DevelopmentCard card){
		Resource[] tmp = card.getCost();
		boolean tmp_boolean = true;
		boolean[] to_return = {false, false, false};
		int card_level = card.getCardLevel().getLevel();

		if ( !(warehouse.areContainedInWarehouse(tmp) || strongbox.areContainedInStrongbox(tmp)) ){
			for (Resource res : tmp){
				if (res != null){
					tmp_boolean = false;
				}
			}
		}

		if (tmp_boolean){
			DevelopmentCard[] devcard = this.development_card_slots.getTopCards();

			if (devcard[0] != null){
				if (card_level - devcard[0].getCardLevel().getLevel() == 1){
					to_return[0] = true;
				}
			} else if (card_level == 1){ 
				to_return[0] = true;
			}

			if (devcard[1] != null){
				if (card_level - devcard[1].getCardLevel().getLevel() == 1){
					to_return[1] = true;
				}
			} else if (card_level == 1){
				to_return[1] = true;
			} 

			if (devcard[2] != null){
				if (card_level - devcard[2].getCardLevel().getLevel() == 1){
					to_return[2] = true;
				}
			} else if (card_level == 1){
				to_return[2] = true;
			}
		}

		return to_return;
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
	 */
	public void buyCard(DevelopmentCard card, int position, boolean warehouse_first){
		boolean[] returned = this.isBuyable(card);
		if (returned[position]){
			this.development_card_slots.buyCard(card, position);

			Resource[] tmp = card.getCost();
			if (warehouse_first){
				for (Resource res : tmp){
					try {
						this.warehouse.getFromWarehouse(res, 1);
					} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
						try {
							this.strongbox.removeResources(res, 1);
						} catch (IllegalArgumentException | IndexOutOfBoundsException x) {}
					}
				}
			} else {
				for (Resource res : tmp){
					try {
						this.strongbox.removeResources(res, 1);
					} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
						try {
							this.warehouse.getFromWarehouse(res, 1);
						} catch (IllegalArgumentException | IndexOutOfBoundsException x) {}
					}
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
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
