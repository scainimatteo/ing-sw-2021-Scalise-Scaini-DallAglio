package it.polimi.ingsw.model.resources;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class ProductionTest {
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

		assertTrue(output_resources[0].equals(produced_resource[0]) && output_resources[1].equals(produced_resource[1]));
	}
}
