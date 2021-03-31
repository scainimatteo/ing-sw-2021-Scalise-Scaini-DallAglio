package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;

import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.resources.Resource;

public class ExtraSpaceAbilityTest {
	Resource wrong_resource = Resource.COIN;
	Resource test_except = Resource.FAITH;
	Resource test_type = Resource.SERVANT;
	Resource right_resource = Resource.SERVANT;
	ExtraSpaceAbility test_space; 
	
	@BeforeEach
	public void init (){
		test_space = new ExtraSpaceAbility (test_type);
	}

	/**
	* test per verificare il corretto inserimento nello spazio
	*/
	@Test
	public void testInsert(){
		test_space.putResource(right_resource);
		assertTrue(test_space.peekResources() == 1);
		test_space.putResource(right_resource);
		assertTrue(test_space.peekResources() == 2);
		assertThrows(IndexOutOfBoundsException.class, () -> {test_space.putResource(right_resource);})	;
		assertThrows(IllegalArgumentException.class, () -> {test_space.putResource(wrong_resource);});
	}

	/**
	*Test per verificare la correttezza dei getter
	*/
	@Test
	public void testGetter() {
		test_space.putResource(right_resource);
		Resource[] return_one = {Resource.SERVANT};
		assertArrayEquals(test_space.getResource(1), return_one);
		assertTrue (test_space.peekResources() == 0);
		test_space.putResource(right_resource);	
		test_space.putResource(right_resource);
		Resource[] return_two = {Resource.SERVANT, Resource.SERVANT};
		assertArrayEquals(test_space.getResource(2), return_two);
		test_space.putResource(right_resource);	
		test_space.putResource(right_resource);	
		assertArrayEquals(test_space.getResource(1), return_one);
		assertTrue (test_space.peekResources() == 1);	
		test_space.putResource(right_resource);
		assertThrows(IndexOutOfBoundsException.class, () -> {test_space.putResource(right_resource);})	;
				
	}
}
