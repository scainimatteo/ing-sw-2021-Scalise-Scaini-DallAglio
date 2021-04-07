package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

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
}
