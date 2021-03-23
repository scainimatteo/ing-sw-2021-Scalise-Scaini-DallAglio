package it.polimi.ingsw.model.resources;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

public class ResourceTest{
	
	/**
	 * First enum test
	 */
	@Test
	public void firstResourceTest(){
		Resource test_resource = Resource.COIN;

		if(test_resource.name().equals("COIN")){
			assertTrue(true);
		}else{
			assertTrue(false);
		}
	}

	/**
	 * Test the method equals for the resources
	 */
	@Test
	public void testEqual () {
		Resource resource1 = Resource.COIN;
		Resource resource2 = Resource.COIN;

		if (resource1.equals(resource2)) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
	}
}
