
package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Resource;

public class WhiteMarblesAbilityTest {
	
	Resource type = Resource.COIN;
	WhiteMarblesAbility wma = new WhiteMarblesAbility(type);
	Resource except = Resource.FAITH;
	/**
	* Constructor test
	* */ 
	@Test
	public void testType(){
		assertEquals (type, wma.getResourceType());
		assertThrows(IllegalArgumentException.class, ()-> {wma = new WhiteMarblesAbility(except);});
	}

	@Test
	public void checkAbilityTest() {
		LeaderAbility test = new WhiteMarblesAbility(null);
		WhiteMarblesAbility test2 = new WhiteMarblesAbility(null);
		assertTrue(test.checkAbility(test2));
	}
}
