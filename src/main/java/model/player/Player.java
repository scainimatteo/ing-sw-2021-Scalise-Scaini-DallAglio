package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.LeaderCardResourcesCost;
import it.polimi.ingsw.model.card.LeaderCardLevelCost;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.util.Observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.IllegalArgumentException;

public class Player extends Observable {
	protected String nickname;
	protected FaithTrack track;
	protected Warehouse warehouse;
	protected StrongBox strongbox;
	protected DevelopmentCardsSlots development_card_slots;
	protected ArrayList<LeaderCard> leader_cards_deck;

	public Player(String nickname, Cell[] cell_track, Tile[] v_r_tiles){
		this.nickname = nickname;
		this.track = new FaithTrack(cell_track, v_r_tiles);
		this.warehouse = new Warehouse();
		this.strongbox = new StrongBox();
		this.development_card_slots = new DevelopmentCardsSlots();
		this.leader_cards_deck = new ArrayList<LeaderCard>();
	}

	public String getNickname(){
		return this.nickname;
	}

	public FaithTrack getFaithTrack() {
		return this.track;
	}
	
	public ArrayList<LeaderCard> getDeck(){
		return this.leader_cards_deck;
	}

	public void setLeaderCards(ArrayList<LeaderCard> leader_cards) {
		this.leader_cards_deck = leader_cards;
	}

	public boolean isActivable(LeaderCard card){
		if (card instanceof LeaderCardLevelCost){
			return this.isActivable((LeaderCardLevelCost) card);
		} else if (card instanceof LeaderCardResourcesCost){
			return this.isActivable((LeaderCardResourcesCost) card);
		}
		return false;
	}

	/**
	 * Return the total sum of resources available to the player
	 * 
	 * @return an hashmap with the sum of all the resources in the three available storages
	 */

	public HashMap <Resource, Integer> totalResources() {
		HashMap<Resource, Integer> total = new HashMap<Resource, Integer>();
		total.put(Resource.COIN, strongbox.get(Resource.COIN));
		total.put(Resource.SERVANT, strongbox.get(Resource.SERVANT));
		total.put(Resource.SHIELD, strongbox.get(Resource.SHIELD));
		total.put(Resource.STONE, strongbox.get(Resource.STONE));
		for (Resource x : getTopResource()){
			if (x != null) {
				total.put(x, total.get(x) + 1);
			}
		}
		for (Resource x : getMiddleResources()){
			if (x != null) {
				total.put(x, total.get(x) + 1);
			}
		}
		for (Resource x : getBottomResources()){
			if (x != null) {
				total.put(x, total.get(x) + 1);
			}
		}
        ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		ExtraSpaceAbility ability;
		for (LeaderCard x : leader_cards_deck){
			if (x.isActive() && x.getAbility().checkAbility(test)){
				ability = (ExtraSpaceAbility) x.getAbility();
				total.put(ability.getResourceType(), total.get(ability.getResourceType()) + ability.peekResources());
			}
		}
		return total;
	}

	/**
	 * @param card is the LeaderCard to be checked
	 * @return true if the card can be activated
	 */
	public boolean isActivable(LeaderCardLevelCost card){
		CardLevel[] req = card.getRequirements();
		Iterator<DevelopmentCard> iterator = this.development_card_slots.getIterator();
		boolean to_return = true;
		CardLevel tmp;

		while (iterator.hasNext()){
			tmp = iterator.next().getCardLevel();

			for (int i = 0; i < req.length; i ++){
				if (req[i].equals(tmp)){
					req[i] = null;
					break;
				} 
			}
		}

		for (int j = 0; j < req.length; j ++){
			if (req[j] != null){
				to_return = false;
				break;
			} 
		}

		return to_return;
	}

	/**
	 * @param card is the LeaderCard to be checked
	 * @return true if the card can be activated
	 */
	public boolean isActivable(LeaderCardResourcesCost card){
		ArrayList<Resource> to_check = card.getRequirements();
		Resource[] all_resources = {Resource.STONE, Resource.COIN, Resource.SERVANT, Resource.SHIELD};
		HashMap <Resource, Integer> total = totalResources();
		for (Resource x : all_resources){
			if (total.get(x) < (int) to_check.stream().filter(y->y.equals(x)).count()) {
				return false;
			}
		}
		return true;
	}

	// TODO: this method will change
	/**
	 * @param whichLeaderCard is an array of two boolean that represent which leader card has to be discarded
	 * @return a faithcontroller that contains the number of discarded leader cards in the faith gained
	 */
	//public FaithController discardLeaderCard(boolean[] whichLeaderCard){
	//	int to_return = 0;

	//	for (int i = 0; i < 2; i ++){
	//		if (whichLeaderCard[i]){
	//			this.leader_cards_deck[i] = null;
	//			to_return ++;
	//		} 
	//	}

	//	return new FaithController(this, to_return, 0);
	//}

	/**
	 * WAREHOUSE METHODS
	 */
	public Warehouse getWarehouse(){
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

	public void swapRows (int i, int j) {
		this.warehouse.swapRows(i, j);
	}

	public void storeTop(ArrayList<Resource> res){
		this.warehouse.storeTop(res);
	}

	public void storeMiddle(ArrayList<Resource> res){
		this.warehouse.storeMiddle(res);
	}

	public void storeBottom(ArrayList<Resource> res){
		this.warehouse.storeBottom(res);
	}

	public void getFromTop(ArrayList<Resource> res){
		this.warehouse.getFromTop(res);
	}

	public void getFromMiddle(ArrayList<Resource> res){
		this.warehouse.getFromMiddle(res);
	}

	public void getFromBottom(ArrayList<Resource> res){
		this.warehouse.getFromBottom(res);
	}

	public void clearWarehouse(){
		this.warehouse.clearWarehouse();
	}

		
	/**
	 * STRONGBOX METHODS
	 */
	public void insertResources(ArrayList<Resource> new_resources){
		this.strongbox.insertResources(new_resources);
	}

	public HashMap<Resource, Integer> getStorage(){
		return this.strongbox.getStorage();
	}

	public void removeResources(ArrayList<Resource> res){
		this.strongbox.removeResources(res);
	}

	public StrongBox getPlayerStrongBox(){
		return this.strongbox;
	}

	/**
	 * DEVCARDSSLOTS METHODS
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

	public Iterator<DevelopmentCard> getDevCardIterator(){
		return this.development_card_slots.getIterator();
	}

	public DevelopmentCardsSlots getDevelopmentCardsSlots() {
		return this.development_card_slots;
	}

	/**
	 * FAITHTRACK METHODS
	 */
	public int getMarkerPosition(){
		return this.track.getMarkerPosition();
	}
}
