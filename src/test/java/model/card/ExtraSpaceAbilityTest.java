package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;
import java.util.NoSuchElementException;

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
	* Tests the methods to check which resources can be stored in the card and stores them
	*/
	@Test
	public void testStore(){
		ArrayList<Resource> test_res = new ArrayList<Resource>();
		assertTrue(test_space.canBeStoredExtra(test_res));
		test_space.storeExtra(test_res);
		test_res.add(right_resource);
		ArrayList<Resource> wrong_res = new ArrayList<Resource>();
		wrong_res.add(wrong_resource);

		assertTrue(test_space.canBeStoredExtra(test_res));
		test_space.storeExtra(test_res);
		assertTrue(test_space.peekResources() == 1);

		assertTrue(test_space.canBeStoredExtra(test_res));
		test_space.storeExtra(test_res);
		assertTrue(test_space.peekResources() == 2);

		assertFalse(test_space.canBeStoredExtra(wrong_res));
		assertThrows(IllegalArgumentException.class, () -> {test_space.storeExtra(wrong_res);});
		assertFalse(test_space.canBeStoredExtra(test_res));
		assertThrows(IllegalArgumentException.class, () -> {test_space.storeExtra(test_res);});
	}

	/**
	* Tests the methods to check which resources can be taken from the card and gets them
	*/
	@Test
	public void testGet() {
		ArrayList<Resource> test_res = new ArrayList<Resource>();
		assertTrue(test_space.isContainedExtra(test_res));
		test_res.add(right_resource);
		ArrayList<Resource> wrong_res = new ArrayList<Resource>();
		wrong_res.add(wrong_resource);
		test_space.storeExtra(test_res);
		test_space.storeExtra(test_res);
		
		assertTrue(test_space.isContainedExtra(test_res));
		test_space.getFromExtra(test_res);
		assertEquals(test_space.peekResources(), 1);
		assertTrue(test_space.isContainedExtra(test_res));
		test_space.getFromExtra(test_res);
		assertEquals(test_space.peekResources(), 0);

		test_res.add(right_resource);
		test_space.storeExtra(test_res);
		assertTrue(test_space.isContainedExtra(test_res));
		test_space.getFromExtra(test_res);
		assertEquals(test_space.peekResources(), 0);

		test_space.storeExtra(test_res);
		test_res.add(right_resource);
		assertFalse(test_space.isContainedExtra(wrong_res));
		assertThrows(IllegalArgumentException.class, () -> {test_space.getFromExtra(wrong_res);})	;
		assertFalse(test_space.isContainedExtra(test_res));
		assertThrows(NoSuchElementException.class, () -> {test_space.getFromExtra(test_res);})	;
	}

	@Test
	public void checkAbilityTest() {
		LeaderAbility test = new ExtraSpaceAbility(null);
		ExtraSpaceAbility test2 = new ExtraSpaceAbility(null);
		assertTrue(test.checkAbility(test2));
	}
}
