
package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class DiscountAbilityTest {
	
	Resource type = Resource.COIN;
	DiscountAbility da = new DiscountAbility(type);
	Resource except = Resource.FAITH;
	/*
	* Test verificare il costruttore
	*/ 
	@Test
	public void testType(){
		assertEquals (type, da.getDiscountedResource());
	}

	/*
	* Test per verificare il lancio di eccezione
	*/
	@Test
	public void testException() {
		assertThrows(IllegalArgumentException.class, ()-> {da = new DiscountAbility(except);});
	}

	@Test
	public void checkAbilityTest() {
		LeaderAbility test = new DiscountAbility(null);
		DiscountAbility test2 = new DiscountAbility(null);
		assertTrue(test.checkAbility(test2));
	}
}
