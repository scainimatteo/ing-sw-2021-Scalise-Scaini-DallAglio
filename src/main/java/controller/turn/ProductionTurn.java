package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.controller.util.ResourceController;
import it.polimi.ingsw.controller.util.ChoiceController;
import it.polimi.ingsw.controller.util.FaithController;

import it.polimi.ingsw.model.card.ProductionAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.util.NotExecutableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class ProductionTurn extends Turn{
	private ProductionAbility[] prod_ability;
	
	public ProductionTurn(Player player, ChoiceController handler){
		this.player = player;
		this.handler = handler;
		this.res_controller = new ResourceController(player, handler);
		this.prod_ability = new ProductionAbility[2];
	}

	/**
	 * @return the production chosen by the player
	 */
	private ProductionInterface chooseProduction() throws NotExecutableException{
		ArrayList<ProductionInterface> player_prods = new ArrayList<ProductionInterface>();
		Production prod_base = new Production(null, null);
		ProductionInterface to_return = null;
		ProductionInterface[] choice;

		player_prods.add(prod_base);

		DevelopmentCard[] tmp = player.getTopCards();
		for (DevelopmentCard card : tmp){
			if (card != null){
				player_prods.add(card);
			} 
		}

		this.checkProductionAbility();
		player_prods.addAll(Arrays.asList(this.prod_ability));

		choice = player_prods.toArray(new Production[player_prods.size()]);

		while( !canBeActivated(to_return) ){
			to_return = (ProductionInterface) handler.pickBetween(player, "Which production do you want to use?", choice, 1)[0];
		}
		 
		return to_return;
	}

	/**
	 * Adds all production ability from the player's LeaderCard deck to the turn's available production
	 */
	private void checkProductionAbility(){
		ProductionAbility test = new ProductionAbility(null, null);
		ProductionAbility tmp = null;
		int index = 0;

		for (LeaderCard card : player.getDeck()){
			if (card != null && card.isActive() && card.getAbility().checkAbility(test)){
				tmp = (ProductionAbility) card.getAbility();
				this.prod_ability[index] = tmp;
				index ++;
			} 
		}
	}

	protected boolean checkProduction(Production prod){
		boolean to_return = false;

		if (prod.getRequiredResources() == null){
			to_return = true;
		} 

		for (Resource res : prod.getProducedResources()){
			if (res == null){
				to_return = true;
			} 
		}

		return to_return;
	}

	/**
	 * @param prod is the production choosen by the player
	 * @return true if the player has the resources requested by the production
	 * @throws NotExecutableException if the player has 1 or 0 resources
	 */
	protected boolean canBeActivated(ProductionInterface prod) throws NotExecutableException {
		int	num_of_resources = 0;

		if (prod == null){
			return false;
		} 

		if (res_controller.howManyResources() < 2){
			throw new NotExecutableException();
		} 

		/**
		 * TODO: check the presence of the resources requested from the production
		 */

		return true;
	}

	/**
	 * @param prod is the production choosen by the player
	 * @return true if the production requires the unset after the activation
	 */
	private boolean setResources(ProductionInterface choice){
		Resource[] all_resources_array = {Resource.COIN, Resource.STONE, Resource.SERVANT, Resource.SHIELD};
		Resource[] set_required_array = new Resource[2];
		Resource[] set_produced_array = new Resource[1];
		Resource returned;
		boolean to_return = false;

		if (choice.getRequiredResources() == null){
			for (int i = 0; i < 2; i ++){
				returned = (Resource) handler.pickBetween(this.player, "Choose a resource to insert as a request", all_resources_array, 1)[0];
				set_required_array[i] = returned;
			}

			choice.setRequiredResources(set_required_array);
			to_return = true;
		} 

		if (choice.getProducedResources() == null){
			set_produced_array[0] = (Resource) handler.pickBetween(this.player, "Choose a resource to insert as a request", all_resources_array, 1)[0];
			choice.setProducedResources(set_produced_array);
			to_return = true;
		} 

		if (choice.getProducedResources()[0] == null){
			set_produced_array[0] = (Resource) handler.pickBetween(this.player, "Choose a resource to insert as a request", all_resources_array, 1)[0];
			choice.setProducedResources(set_produced_array);
			to_return = true;
		} 

		return to_return;
	}

	/**
	 * @param choice is the production choosen by the player
	 */
	private void unsetResources(ProductionInterface choice){
		Resource[] unset_array = {null};
		choice.setProducedResources(unset_array);
	}

	/**
	 * @return the faithcontroller that contains the quantity of faith gained
	 */
	@Override
	protected FaithController playAction() throws NotExecutableException{
		boolean containsNullPosition = false;
		int gained_faith = 0;

		ProductionInterface choice = chooseProduction();

		containsNullPosition = setResources(choice);

		// pay
		res_controller.payResources(choice.getRequiredResources());

		Resource[] produced = choice.activateProduction();

		if (containsNullPosition){
			unsetResources(choice);
		} 

		// store
		res_controller.storeFromProduction(produced);

		return new FaithController(this.player, gained_faith, 0);
	}
}
