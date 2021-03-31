package it.polimi.ingsw.model.card;
 import org.junit.jupiter.api.*;
 import static org.junit.jupiter.api.Assertions.*;
 import java.util.Random; 
import it.polimi.ingsw.model.card.DevelopmentCardsColor;

public class CardLevelTest {
	int first_level = 0;
	int sec_level = 0;
	int i = 0;
	int j = 0;
	DevelopmentCardsColor[] rand_color = {DevelopmentCardsColor.GREEN, DevelopmentCardsColor.BLUE, DevelopmentCardsColor.YELLOW, DevelopmentCardsColor.PURPLE};
	/**
	* Testa diverse combinazioni randomiche di livelli e colori
	*/
	@RepeatedTest(value = 10)
	public void RandTestMethods (){
		Random random = new Random();
		first_level = random.nextInt(10);
		sec_level = random.nextInt(10);
		i = random.nextInt(4);
		j = random.nextInt(4);
		CardLevel first_card = new CardLevel (first_level, rand_color[i]);	
		CardLevel sec_card = new CardLevel (sec_level, rand_color[j]);
		if (j != i){
			assertFalse (first_card.compareColor(sec_card));
		}
		else {
			assertTrue (first_card.compareColor(sec_card));
		}
		assertTrue (first_level - sec_level == first_card.compareLevel(sec_card));	
		assertTrue (first_card.compareColor(sec_card) == sec_card.compareColor(first_card));
	}
}
