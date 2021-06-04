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
import java.util.stream.Collectors;
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
	}

	public String getNickname(){
		return this.nickname;
	}

	public void notifyPlayer() {
		notify(new ViewUpdate(this.simplify()));
	}

	private SimplePlayer simplify() {
		DevelopmentCard[] first_column = new DevelopmentCard[3];
		Iterator<DevelopmentCard> iterator = this.development_card_slots.getDeck(0, 0).iterator();
		int i = 0;
		while (iterator.hasNext()){
			first_column[i] = iterator.next();
			i++;
		}

		DevelopmentCard[] second_column = new DevelopmentCard[3];
		iterator = this.development_card_slots.getDeck(0, 1).iterator();
		i = 0;
		while (iterator.hasNext()){
			second_column[i] = iterator.next();
			i++;
		}

		DevelopmentCard[] third_column = new DevelopmentCard[3];
		iterator = this.development_card_slots.getDeck(0, 2).iterator();
		i = 0;
		while (iterator.hasNext()){
			third_column[i] = iterator.next();
			i++;
		}

		return new SimplePlayer(this.nickname, this.track.getCellTrack(), this.track.getMarker(), this.track.getTiles(), new SimpleWarehouse(this.warehouse.getTopResource(), this.warehouse.getMiddleResources(), this.warehouse.getBottomResources()), this.strongbox.getStorage(), this.leader_cards_deck, new SimpleDevelopmentCardSlot(first_column, second_column, third_column));
	}

	/**
	 * LEADER CARD METHODS
	 */
	//TODO: change this name
	public ArrayList<LeaderCard> getDeck(){
		return this.leader_cards_deck;
	}

	public void setLeaderCards(ArrayList<LeaderCard> leader_cards) {
		this.leader_cards_deck = leader_cards;
		this.notifyPlayer();
	}

	public boolean isActivable(LeaderCard card){
		return card.isActivable(this);
	}

	public void activateLeader(LeaderCard card){
		for (LeaderCard x : leader_cards_deck){
			if (x.getId() == card.getId()){
				x.activateLeaderCard();
				this.notifyPlayer();
				return;
			}
		}
	}

	public void discardLeader(int leader_card_id){
		for (LeaderCard l: this.leader_cards_deck) {
			if (l.getId() == leader_card_id) {
				this.leader_cards_deck.remove(l);
				break;
			}
		}
		this.notifyPlayer();
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
		ArrayList<Resource> single_type;
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource r : check){
			single_type = (ArrayList<Resource>) res.stream().filter(x->x.equals(r)).collect(Collectors.toList());
			for (LeaderCard x : leader_cards_deck){
				ability = x.getAbility();
				if (ability.checkAbility(test)) {
					if (((ExtraSpaceAbility) ability).canBeStoredExtra(single_type)) {
						((ExtraSpaceAbility) ability).storeExtra(single_type);
					}
				}
			}
		}
	}

	public void getFromExtra(ArrayList<Resource> res){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		LeaderAbility ability;
		ArrayList<Resource> single_type;
		Resource[] check = {Resource.COIN, Resource.SHIELD, Resource.STONE, Resource.SERVANT};
		for (Resource r : check){
			single_type = (ArrayList<Resource>)res.stream().filter(x->x.equals(r)).collect(Collectors.toList());
			for (LeaderCard x : leader_cards_deck){
				ability = x.getAbility();
				if (ability.checkAbility(test)) {
					if (((ExtraSpaceAbility) ability).isContainedExtra(single_type)) {
						((ExtraSpaceAbility) ability).getFromExtra(single_type);
					}
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

	//TODO: change this in getSlot
	//TODO: why does this return an array?
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
