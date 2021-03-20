package it.polimi.ingsw.model.game;

import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.Deck;

public class DevelopmentCardsOnTable {
	private Deck[][] development_card_decks;

	public DevelopmentCardsOnTable(Deck[][] development_card_decks) {
		this.development_card_decks = development_card_decks;
	}

	public DevelopmentCard[][] getTopCards() {
		DevelopmentCard[][] development_card_on_top = new DevelopmentCard[3][4];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				development_card_on_top[i][j] = (DevelopmentCard)development_card_decks[i][j].getTopCard();
			}
		}
		return development_card_on_top;
	}

	public void getFromDeck(DevelopmentCard chosen_card) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (development_card_decks[i][j].getTopCard().equals(chosen_card)) {
					development_card_decks[i][j].removeTopCard();
					break;
				}
			}
		}
	}
}
