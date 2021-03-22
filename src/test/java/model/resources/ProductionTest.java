package it.polimi.ingsw.model.resources;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class ProductionTest {
	
	/**
	 * Test that check if the methods of the class Production are working
	 */
	@Test
	public void firstProductionTest () {
		Resource test_resource_1 = Resource.COIN;
		Resource test_resource_2 = Resource.SHIELD;
		Resource test_resource_3 = Resource.STONE;
		Resource test_resource_4 = Resource.FAITH;

		Resource[] required_resource = {test_resource_1, test_resource_2};
		Resource[] produced_resource = {test_resource_3, test_resource_4};

		Production test_production = new Production (required_resource, produced_resource);

		// Resource[] to_print = test_production.getRequiredResources();
		// to_print = test_production.getProducedResources();

		Resource test_resource_5 = Resource.COIN;
		Resource test_resource_6 = Resource.SHIELD;
		Resource[] input_resource = {test_resource_6, test_resource_5};
		
		if (test_production.compareArrays(input_resource)) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
	}

	/**
	 * Test for the method activateProduction
	 */
	@Test
	public void activateProductionTest () {
		Resource test_resource_1 = Resource.COIN;
		Resource test_resource_2 = Resource.SHIELD;
		Resource test_resource_3 = Resource.STONE;
		Resource test_resource_4 = Resource.FAITH;

		Resource[] required_resource = {test_resource_1, test_resource_2};
		Resource[] produced_resource = {test_resource_3, test_resource_4};

		Production test_production = new Production (required_resource, produced_resource);

		Resource[] output_resources = test_production.activateProduction(required_resource);

		if (output_resources[0].equals(produced_resource[0]) && output_resources[1].equals(produced_resource[1])) {
			assertTrue(true);
		}else {
			assertTrue(false);
		}
	}
}
