package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.track.FaithTrack;

public class Player {
	private String nickname;
	private FaithTrack track;
	private Warehouse warehouse;
	private StrongBox strongbox;
	private DevelopmentCardsSlots development_card_slots;
	private LeaderCard[] leader_cards_deck;
}
