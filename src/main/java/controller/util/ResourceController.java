package it.polimi.ingsw.controller.util;

import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.HashMap;

import java.lang.IllegalArgumentException;

import it.polimi.ingsw.controller.util.ChoiceController;

import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Storage;

import it.polimi.ingsw.model.resources.Resource;

public class ResourceController {
	private Player player;
	protected ExtraSpaceAbility[] extra_space;
	private ChoiceController handler;
	
	public ResourceController(Player player, ChoiceController handler){
		this.player = player;
		this.handler = handler;
		checkExtraSpace();
	}

	/**
	 * Checks if the player has the proper resources
	 *
	 * @param card is the DevelopmentCard to be checked
	 * @return true if storage has enough resources to pay for the entirety of the cost 
	 */
	public boolean hasResources(Resource[] cost){
		boolean enough_resources = true;
		HashMap <Resource, Integer> total = player.totalResources();	
		for (ExtraSpaceAbility x : extra_space){
			if (x != null){
				total.put(x.getResourceType(), total.get(x.getResourceType()) + x.peekResources());
			}
		}
		for (Resource r: cost){
			total.put(r, total.get(r) - 1);
			if (total.get(r) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Adds all extra space from the player's LeaderCard deck to the turn's available extra space
	 */
	private void checkExtraSpace(){
		ExtraSpaceAbility test = new ExtraSpaceAbility(null);
		int index = 0;
		for (LeaderCard card: player.getDeck()){
			if (card.isActive() && card.getAbility().checkAbility(test)){
 				extra_space[index] = (ExtraSpaceAbility) card.getAbility();
				index ++;
			}
		}
	}

	/** 
	 * Removes resources equal to the cost from the player and allows them to choose where to get them out of
	 */
	public void payResources(Resource[] resources) {
		boolean has_decided;
		Storage storage;
		for (Resource x: resources) {
			has_decided = false;
			while (!has_decided){
				storage = (Storage) handler.pickBetween(player, "where do you want to get your resources?", new Storage[] {player.getPlayerWarehouse(), player.getPlayerStrongbox(), extra_space[0], extra_space[1]}, 1)[0];
				try {
					storage.getResource(x);
					has_decided = true;
				} catch (NoSuchElementException e){
					//TODO: alert the player the resource couldn't be stored}
				}
			}
		}
	}

	/**
 	 * Allows the player to position the resources gained from the productions
	 *  
	 * @param resources are the resources to be stored 
	 */
	public void storeFromProduction(Resource[] resources){
		boolean has_decided;
		Resource[] single_resource = new Resource[1];
		Storage storage;
		//TODO: print Warehouse
		for (Resource x : resources){
			has_decided = false;
			if (x != null) {
				while (!has_decided){
					storage = (Storage) handler.pickBetween(player, "where do you want to store your resource?", new Storage[] {player.getPlayerWarehouse(), player.getPlayerStrongbox(), extra_space[0], extra_space[1]}, 1)[0];
					try {
						storage.storeResource(x);
						has_decided = true;
					} catch (IllegalArgumentException e){
					//TODO: alert the player the resource couldn't be stored}
					}
				}
			}
		}
	}

	/**
 	 * Allows the player to position the resources gained from the market
	 * 
	 * @param resources are the resources to be inserted
	 * @return the number of discarded resources
	 */
	public int storeFromMarket(Resource[] resources){
		boolean has_decided;
		int discarded_resources = 0;
		Resource[] single_resource = new Resource[1];
		//TODO: print Warehouse
		for (Resource x : resources){
			has_decided = false;
			Storage storage;
			if (x != null) {
				while (!has_decided){
					while (handler.pickFlow(player, "Do you want to rearrange the warehouse?")){
						Object[] arg = handler.pickBetween (player, "Choose two rows to swap", new Integer[] {1, 2, 3}, 2);
						player.swapRows((Integer) arg[0], (Integer) arg[1]);
						//TODO: print
					}
					if (handler.pickFlow(player, "Do you want to store this resource?")){
						storage = (Storage) handler.pickBetween(player, "where do you want to store your resource?", new Storage[] {player.getPlayerWarehouse(), extra_space[0], extra_space[1]}, 1)[0];
						try {
							storage.storeResource(x);
							has_decided = true;
						} catch (IllegalArgumentException e){
						//TODO: alert the player the resource couldn't be stored}
						}
					}
					if (!has_decided && handler.pickFlow (player, "Do you want to discard the resource?")){
							discarded_resources++;
							has_decided = true;
					}
				}
			}
		}
		return discarded_resources;
	}

	/**
	 * @return the number of the resources stored
	 */
	public int howManyResources(){
		HashMap <Resource, Integer> total = player.totalResources();	
		ArrayList<Integer> storage_values = new ArrayList<Integer>(total.values());
		int to_return = 0;

		for (Integer num : storage_values){
			to_return += num;
		}

		return to_return;
	}

}
