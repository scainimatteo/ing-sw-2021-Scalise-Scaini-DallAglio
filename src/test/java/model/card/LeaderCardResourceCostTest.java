package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

import java.io.IOException;

import org.json.simple.parser.ParseException;

import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.game.Factory;

import it.polimi.ingsw.model.resources.Resource;

public class LeaderCardResourceCostTest {
	LeaderCardResourcesCost[] cards;
	Resource[] requirements_of_a_card;
	int chosen_card_index;

	@BeforeEach
	public void createCards() {
		try {
			this.cards = new LeaderCardResourcesCost[4];
			LeaderCard[] all_leader_cards = Factory.getIstance().getAllLeaderCards();
			List<LeaderCard> list = Arrays.asList(all_leader_cards);
			Collections.shuffle(list);
			list.toArray(all_leader_cards);

			int i = 0;
			for (LeaderCard card : all_leader_cards) {
				if (i < 4 && card instanceof LeaderCardResourcesCost) {
					this.cards[i] = (LeaderCardResourcesCost) card;
					i++;
				}
			}

			Random random = new Random();
			this.chosen_card_index = random.nextInt(4);
			this.requirements_of_a_card = cards[chosen_card_index].getRequirements();
		} catch (ParseException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Test
	public void activateLeaderCardTest() {
		for (LeaderCardResourcesCost card : this.cards) {
			card.activateLeaderCard();
			assertTrue(card.isActive());
		}
	}

	@Test
	public void checkRequirements() {
		for (int i = 0; i < 4; i++) {
			if (i == this.chosen_card_index) {
				assertArrayEquals(requirements_of_a_card, cards[i].getRequirements());
			} else {
				assertFalse(Arrays.equals(requirements_of_a_card, cards[i].getRequirements()));
			}
		}
	}
}
