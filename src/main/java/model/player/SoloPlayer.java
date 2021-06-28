package it.polimi.ingsw.model.player;

import java.util.ArrayList;

import it.polimi.ingsw.controller.servermessage.ViewUpdate;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.player.track.SoloFaithTrack;
import it.polimi.ingsw.model.player.track.VaticanReports;

import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;
import it.polimi.ingsw.view.simplemodel.SimpleSoloPlayer;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;

public class SoloPlayer extends Player{
	public SoloPlayer(String nickname, SoloFaithTrack track){
		super(nickname, track);
	}

	/**
	 * Send a ViewUpdate with the current Player
	 */
	public void notifyPlayer() {
		notifyModel(new ViewUpdate(this.simplify()));
	}

	/**
	 * Simplify the Player to send it to the Client
	 *
	 * @return the SimpleSoloPlayer used to represent this Player
	 */
	protected SimpleSoloPlayer simplify() {
		ArrayList<DevelopmentCard> first_column = super.development_card_slots.getDeckAsArrayList(0, 0);
		ArrayList<DevelopmentCard> second_column = super.development_card_slots.getDeckAsArrayList(0, 1);
		ArrayList<DevelopmentCard> third_column = super.development_card_slots.getDeckAsArrayList(0, 2);

		return new SimpleSoloPlayer(super.nickname, super.track.getCellTrack(), super.track.getMarker(), super.track.getTiles(), new SimpleWarehouse(super.warehouse.getTopResource(), super.warehouse.getMiddleResources(), super.warehouse.getBottomResources()), super.strongbox.getStorage(), super.leader_cards_deck, new SimpleDevelopmentCardSlot(first_column, second_column, third_column), getBlackMarkerPosition());
	}

	/**
	 * SOLOFAITHTRACK METHODS
	 */

	/**
	 * Move the Black Cross marker forward, notifing the Client
	 *
	 * @param number_of_times how many Cells to go forward
	 */
	//TODO: is there a way to avoid this and the other casting?
	public VaticanReports moveForwardBlackMarker(int number_of_times){
		VaticanReports vatican_report = ((SoloFaithTrack) super.track).moveForwardBlackMarker(number_of_times);
		notifyPlayer();
		return vatican_report;
	}

	/**
	 * @return the position of the Cell the Black Cross is on
	 */
	public int getBlackMarkerPosition(){
		return ((SoloFaithTrack) super.track).getBlackMarkerPosition();
	}

	/**
	 * @return true if the Black Cross is at the end of the FaithTrack
	 */
	public boolean isBlackCrossAtTheEnd(){
		return ((SoloFaithTrack) super.track).getBlackMarkerPosition() >= super.track.getLastPosition();
	}
}
