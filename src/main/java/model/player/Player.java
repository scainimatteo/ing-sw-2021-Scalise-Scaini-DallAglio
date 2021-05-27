package it.polimi.ingsw.model.player;

import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.LeaderCardResourcesCost;
import it.polimi.ingsw.model.card.LeaderCardLevelCost;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.CardLevel;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.util.Observable;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Iterator;

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
		this.notifyPlayer();
	}

	public String getNickname(){
		return this.nickname;
	}

	public void notifyPlayer() {
		notify(new ViewUpdate(this.simplify()));
	}

	private SimplePlayer simplify() {
		DevelopmentCard[] first_column = new DevelopmentCard[3];
		Iterator<DevelopmentCard> iterator = this.development_card_slots.getDeck(0, 1).iterator();
		int i = 0;
		while (iterator.hasNext()){
			first_column[2 - i] = iterator.next();
			i ++;
		}

		DevelopmentCard[] second_column = new DevelopmentCard[3];
		iterator = this.development_card_slots.getDeck(0, 1).iterator();
		i = 0;
		while (iterator.hasNext()){
			second_column[2 - i] = iterator.next();
			i ++;
		}

		DevelopmentCard[] third_column = new DevelopmentCard[3];
		iterator = this.development_card_slots.getDeck(0, 1).iterator();
		i = 0;
		while (iterator.hasNext()){
			third_column[2 - i] = iterator.next();
			i ++;
		}

		return new SimplePlayer(this.nickname, this.track.getCellTrack(), this.track.getMarker(), this.track.getTiles(), new SimpleWarehouse(this.warehouse.getTopResource(), this.warehouse.getMiddleResources(), this.warehouse.getBottomResources()), this.strongbox.getStorage(), this.leader_cards_deck, new SimpleDevelopmentCardSlot(first_column, second_column, third_column));
	}

	/**
	 * LEADER CARD METHODS
	 */
	
	public ArrayList<LeaderCard> getDeck(){
		return this.leader_cards_deck;
	}

	public void setLeaderCards(ArrayList<LeaderCard> leader_cards) {
		this.leader_cards_deck = leader_cards;
		this.notifyPlayer();
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

	public void activateLeader(LeaderCard card){
		for (LeaderCard x : leader_cards_deck){
			if (x.equals(card)){
				x.activateLeaderCard();
				this.notifyPlayer();
				return;
			}
		}
	}

	public void discardLeader(LeaderCard leader_card){
		this.leader_cards_deck.remove(leader_card);
		this.notifyPlayer();
	}

	/**
	 * Return the total sum of resources available to the player
	 * 
	 * @return an hashmap with the sum of all the resources in the three available storages
	 */

	private HashMap <Resource, Integer> totalResources() {
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

	public boolean hasEnoughResources(ArrayList<Resource> cost){
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		HashMap <Resource, Integer> storage = totalResources();
		for (Resource res : check){
			if(storage.get(res) < (int) cost.stream().filter(x->x.equals(res)).count()){
				return false;
			}
		}
		return true;
	}

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
		this.notifyPlayer();
	}

	public void storeTop(ArrayList<Resource> res){
		this.warehouse.storeTop(res);
		this.notifyPlayer();
	}

	public void storeMiddle(ArrayList<Resource> res){
		this.warehouse.storeMiddle(res);
		this.notifyPlayer();
	}

	public void storeBottom(ArrayList<Resource> res){
		this.warehouse.storeBottom(res);
		this.notifyPlayer();
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
		this.notifyPlayer();
	}

	public void storeExtra(ArrayList<Resource> res){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		LeaderAbility ability;
		for (LeaderCard x : leader_cards_deck){
			ability = x.getAbility();
			if (ability.checkAbility(test)) {
				if (((ExtraSpaceAbility) ability).canBeStoredExtra(res)) {
					((ExtraSpaceAbility) ability).storeExtra(res);
				}
			}
		}
	}

	public void getFromExtra(ArrayList<Resource> res){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		LeaderAbility ability;
		for (LeaderCard x : leader_cards_deck){
			ability = x.getAbility();
			if (ability.checkAbility(test)) {
				if (((ExtraSpaceAbility) ability).isContainedExtra(res)){
					((ExtraSpaceAbility) ability).getFromExtra(res);
				}
			}
		}
	}
		
	/**
	 * STRONGBOX METHODS
	 */
	public void insertResources(ArrayList<Resource> new_resources){
		this.strongbox.insertResources(new_resources);
		this.notifyPlayer();
	}

	public HashMap<Resource, Integer> getStorage(){
		return this.strongbox.getStorage();
	}

	public void removeResources(ArrayList<Resource> res){
		this.strongbox.removeResources(res);
		this.notifyPlayer();
	}

	public StrongBox getPlayerStrongBox(){
		return this.strongbox;
	}

	/**
	 * DEVCARDSSLOTS METHODS
	 */
	public boolean fitsInSlot(DevelopmentCard card, int pos){
		return this.development_card_slots.fitsInSlot(card, pos);
	}

	public void buyCard(DevelopmentCard card, int position){
		this.development_card_slots.buyCard(card, position);
		this.notifyPlayer();
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
	public FaithTrack getFaithTrack() {
		return this.track;
	}

	public int getMarkerPosition(){
		return this.track.getMarkerPosition();
	}

	public void moveForward(int steps){
		this.track.moveForward(steps);
		this.notifyPlayer();
	}

	public boolean endOfTrack(){
		return getMarkerPosition() >= track.getLastPosition();
	}
}
