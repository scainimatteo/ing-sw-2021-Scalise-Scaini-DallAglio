package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.DevelopmentCardsSlots;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.StrongBox;

import it.polimi.ingsw.model.resources.ProductionBase;

public class Player {
	private String nickname;
	private FaithTrack track;
	private Warehouse warehouse;
	private StrongBox strongbox;
	private DevelopmentCardsSlots development_card_slots;
	private ProductionBase base_production;
	private LeaderCard[] leader_cards_deck;
}
