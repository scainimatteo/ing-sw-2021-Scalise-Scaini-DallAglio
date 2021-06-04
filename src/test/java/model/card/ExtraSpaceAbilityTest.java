package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;

import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;
import java.util.Arrays;

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
		ArrayList<Resource> test_res = new ArrayList<Resource>();
		test_res.add(right_resource);
		ArrayList<Resource> wrong_res = new ArrayList<Resource>();
		wrong_res.add(wrong_resource);
		test_space.storeExtra(test_res);
		assertTrue(test_space.peekResources() == 1);
		test_space.storeExtra(test_res);
		assertTrue(test_space.peekResources() == 2);
		assertThrows(IllegalArgumentException.class, () -> {test_space.storeExtra(wrong_res);});
	}

	/**
	*Test per verificare la correttezza dei getter
	*/
	@Test
	public void testGetter() {
		ArrayList<Resource> test_res = new ArrayList<Resource>();
		test_res.add(right_resource);
		ArrayList<Resource> wrong_res = new ArrayList<Resource>();
		wrong_res.add(wrong_resource);
		test_space.storeExtra(test_res);
		ArrayList<Resource> return_one = new ArrayList<Resource>(Arrays.asList(Resource.SERVANT));
		assertEquals(test_space.peekResources(), 1);
		test_space.storeExtra(test_res);
		ArrayList<Resource> return_two = new ArrayList<Resource>(Arrays.asList(Resource.SERVANT));
		assertEquals(test_space.peekResources(), 2);
		assertThrows(IllegalArgumentException.class, () -> {test_space.storeExtra(test_res);})	;
	}

	@Test
	public void checkAbilityTest() {
		LeaderAbility test = new ExtraSpaceAbility(null);
		ExtraSpaceAbility test2 = new ExtraSpaceAbility(null);
		assertTrue(test.checkAbility(test2));
	}
}
