
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
	/*
	* Test verificare il costruttore
	*/ 
	@Test
	public void testType(){
		assertEquals (type, wma.getResourceType());
	}

	/*
	* Test per verificare il lancio di eccezione
	*/
	@Test
	public void testException() {
		assertThrows(IllegalArgumentException.class, ()-> {wma = new WhiteMarblesAbility(except);});
	}
}