package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*; 
import static org.junit.jupiter.api.Assertions.*;   

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.resources.Resource;  
import it.polimi.ingsw.model.resources.Production;

import java.util.Random;

public class DevelopmentCardTest{
	Resource [] randresource = {Resource.FAITH, Resource.STONE, Resource.SHIELD, Resource.COIN, Resource.SERVANT};
	Resource[] input = new Resource[2];
	Resource[] output = new Resource[2];
	Random random = new Random();
	DevelopmentCardsColor[] rand_color = {DevelopmentCardsColor.GREEN, DevelopmentCardsColor.BLUE, DevelopmentCardsColor.YELLOW, DevelopmentCardsColor.PURPLE};
	DevelopmentCard devcard;
	CardLevel level;	

	@BeforeEach
	public void init (){
		level = new CardLevel(random.nextInt(4), rand_color[random.nextInt(4)]);
		int x, y, z, t;
		x = random.nextInt(4);
		y = random.nextInt(4);
		z = random.nextInt(4);
		t = random.nextInt(4);
		input[0] = randresource[x];
		input[1] = randresource[y];
		output[0] = randresource[z];
		output[1] = randresource[t];
		devcard = new DevelopmentCard(random.nextInt(10), new Production(input, output), input, level, random.nextInt(3));
	}
	
	/**
	* tests getter and resource production
	*/
	@RepeatedTest(value = 10)
	public void fullTest(){
		assertEquals (level, devcard.getCardLevel());
		assertEquals (output, devcard.useCard(input));	
	}
}