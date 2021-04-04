package it.polimi.ingsw.model.resources;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceTest{
	/**
	 * Test the creation of the resources
	 */
	@Test
	public void createResourceTest(){
		Resource test_resource = Resource.COIN;

		assertTrue(test_resource.name().equals("COIN"));
	}

	/**
	 * Test the method equals for the resources
	 */
	@Test
	public void testEqual(){
		Resource resource1 = Resource.COIN;
		Resource resource2 = Resource.COIN;

		assertTrue(resource1.equals(resource2));
	}
}
