package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.game.Factory;

public class LeaderCardLevelCostTest {
	LeaderCardLevelCost[] cards;
	CardLevel[] cardlevels;

	@BeforeEach
	public void createCards() {
		try {
			this.cards = new LeaderCardLevelCost[4];
			this.cardlevels = new CardLevel[4];
			LeaderCard[] all_leader_cards = Factory.getIstance().getAllLeaderCards();
			List<LeaderCard> list = Arrays.asList(all_leader_cards);
			Collections.shuffle(list);
			list.toArray(all_leader_cards);

			int i = 0;
			for (LeaderCard card: all_leader_cards) {
				if (i < 4 && card instanceof LeaderCardLevelCost) {
					this.cards[i] = (LeaderCardLevelCost) card;
					int level = this.cards[i].getRequirements().get(0).getLevel() - 1;
					DevelopmentCardsColor color = this.cards[i].getRequirements().get(0).getColor();
					this.cardlevels[i] = new CardLevel(level, color);
					i++;
				}
			}
		} catch (ParseException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Test
	public void activateLeaderCardTest() {
		for (LeaderCardLevelCost card : this.cards) {
			card.activateLeaderCard();
			assertTrue(card.isActive());
		}
	}

	@Test
	public void checkRequirements() {
		for (int i = 0; i < this.cards.length; i++) {
			CardLevel leader_card_level = this.cards[i].getRequirements().get(0);
			assertTrue(leader_card_level.equals(this.cardlevels[i]));
		}
	}
}
