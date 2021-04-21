package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.card.LeaderAbility;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class ProductionAbilityTest {
	Resource test_resource_1 = Resource.COIN;
	Resource test_resource_2 = Resource.SHIELD;
	Resource test_resource_3 = Resource.STONE;
	Resource test_resource_4 = Resource.FAITH;
	Resource[] test_required = {test_resource_1, test_resource_2, test_resource_3, test_resource_4};
	Resource[] test_produced = {null, test_resource_4};
	ProductionAbility test_prod_ability = new ProductionAbility (test_required, test_produced);
	
	/*
	* test sul costruttore e accetazione dei parametri
	*/
	@Test
	public void ConstructorTest () {
		assertTrue (test_required.equals(test_prod_ability.getProduction().getRequiredResources()));
		assertTrue (test_produced.equals(test_prod_ability.getProduction().getProducedResources()));
	}	

	/*
	* test efficacia setter
	*/
	@Test
	public void SetterTest(){
		Resource test_resource_5 = Resource.STONE;
		test_prod_ability.setProducts(test_resource_5);
		Resource[] token_prod = test_prod_ability.getProduction().getProducedResources();
		assertEquals (token_prod[0], test_resource_5);
	}
}
