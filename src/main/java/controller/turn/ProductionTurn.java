package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.controller.util.FaithController;
import it.polimi.ingsw.controller.util.ChoiceHandler;

import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.ProductionAbility;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductionTurn extends Turn{
	private Production[] prod_ability;
	
	public ProductionTurn(Player player){
		this.player = player;
		this.handler = new ChoiceHandler();
		this.prod_ability = new Production[2];
	}

	/**
	 * @return the production chosen by the player
	 */
	private Production chooseProduction(){
		Production prod_base = new Production(null, null);
		ArrayList<Production> player_prods = new ArrayList<Production>();
		Production[] choice;
		Production to_return;

		player_prods.add(prod_base);

		DevelopmentCard[] tmp = player.getTopCards();
		for (DevelopmentCard card : tmp){
			if (card != null){
				player_prods.add(card.getProduction());
			} 
		}

		this.checkProductionAbility();
		player_prods.addAll(Arrays.asList(this.prod_ability));

		choice = player_prods.toArray(new Production[player_prods.size()]);

		to_return = (Production) handler.pickBetween(choice);

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
				this.prod_ability[index] = tmp.getProduction();
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
	 * @return the faithcontroller that contains the quantity of faith gained
	 * TODO: sceglie produzione
	 *		 controlla che qualche array/posizione non sia null
	 *		 se si, chiama metodo nullProd
	 *		 se no, chiama i metodi della production
	 *
	 *		 nullProd:
	 *		 controlla quali sono le posizioni nulle:
	 *		 se tutti e due gli array, chiede due input e un output e li genera
	 *		 se solo una posizione nell'output, chiede cosa mettere, lo inserisce in un array e controlla l'input
	 *
	 *		 devCardProd:
	 *		 chiede dove prendere i materiali e li mette in un array
	 *		 chiama activateProd
	 */
	@Override
	protected FaithController playAction(){
		Production choice = chooseProduction();
		boolean containsNullPosition = false;

		return new FaithController(this.player, 0, 0);
	}
}
