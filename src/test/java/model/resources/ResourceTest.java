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
			assertFalse(true);
		}else{
			assertFalse(false);
		}
	}
}
