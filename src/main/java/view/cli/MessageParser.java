package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
import java.util.HashSet;

import it.polimi.ingsw.controller.message.DiscardResourcesMessage;
import it.polimi.ingsw.controller.message.ActivateLeaderMessage;
import it.polimi.ingsw.controller.message.DiscardLeaderMessage;
import it.polimi.ingsw.controller.message.InitializingMessage;
import it.polimi.ingsw.controller.message.ProductionMessage;
import it.polimi.ingsw.controller.message.EndTurnMessage;
import it.polimi.ingsw.controller.message.BuyCardMessage;
import it.polimi.ingsw.controller.message.MarketMessage;
import it.polimi.ingsw.controller.message.StoreMessage;
import it.polimi.ingsw.controller.message.PayMessage;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.message.Storage;

import it.polimi.ingsw.model.resources.ProductionInterface;
import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.ProductionAbility;
import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.view.simplemodel.SimpleDevelopmentCardSlot;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;

public class MessageParser {
	/**
	 * Parse the input from the command line
	 *
	 * @param input the String form the CLI
	 * @return the Message created to send to the client
	 * @throws IllegalArgumentException if the input cannot be parsed
	 */
	public static Message parseInput(String input, boolean initializing, SimplePlayer player) throws IllegalArgumentException {
		if (initializing) {
			return new InitializingMessage(input);
		}

		String[] inputs = input.split(" ");
		switch(inputs[0].toUpperCase()) {
			case "BUYCARD":
			case "BUY":
			case "B":
				return parseBuyCardMessage(inputs);
			case "MARKET":
			case "M":
				return parseMarketMessage(inputs);
			case "PRODUCTION":
			case "PROD":
			case "PR":
				return parseProductionMessage(inputs, player);
			case "PAY":
			case "PA":
				return parsePayMessage(inputs);
			case "STORE":
			case "S":
				return parseStoreMessage(inputs);
			case "ENDTURN":
			case "END":
			case "E":
				return parseEndTurnMessage(inputs);
			case "DISCARD":
			case "D":
				return parseDiscardMessage(inputs, player);
			case "ACTIVATE":
			case "A":
				return parseActivateLeaderCardMessage(inputs, player);
			default:
				throw new IllegalArgumentException();
		}
	}

	private static Message parseBuyCardMessage(String[] inputs) throws IllegalArgumentException {
		try {
			int level = Integer.parseInt(inputs[1]);
			int color = DevelopmentCardsColor.valueOf(inputs[2].toUpperCase()).getOrder();
			int slot = Integer.parseInt(inputs[3]);
			return new BuyCardMessage(level, color, slot);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("buy level color slot");
		}
	}

	private static Message parseMarketMessage(String[] inputs) throws IllegalArgumentException {
		try {
			boolean row_or_column = inputs[1].toUpperCase().equals("row") ? false : true;
			int index = Integer.parseInt(inputs[2]);
			if (inputs.length == 3) {
				return new MarketMessage(row_or_column, index);
			}

			ArrayList<Resource> white_marbles = new ArrayList<Resource>();
			for (int i = 3; i < 7; i++) {
				int num = Integer.parseInt(inputs[i]);
				Resource res = Resource.valueOf(inputs[i + 1].toUpperCase());
				for (int j = 0; j < num; j++) {
					white_marbles.add(res);
				}
				if (inputs.length == (i + 2)) {
					break;
				} else {
					i++;
				}
			}
			return new MarketMessage(row_or_column, index, white_marbles);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("market row/column index <number resource> <number resource>");
		}
	}

	private static ProductionInterface parseBaseProduction(String[] inputs, int i, boolean production_base) throws IndexOutOfBoundsException {
		if (production_base) {
			throw new IllegalArgumentException();
		}

		ArrayList<Resource> required = new ArrayList<Resource>();
		required.add(Resource.valueOf(inputs[i + 1].toUpperCase()));
		required.add(Resource.valueOf(inputs[i + 2].toUpperCase()));
		ArrayList<Resource> produced = new ArrayList<Resource>();
		produced.add(Resource.valueOf(inputs[i + 3].toUpperCase()));
		return new Production(required, produced);
	}

	private static ProductionInterface parseDevelopmentCardProduction(String[] inputs, int i, SimplePlayer player, HashSet<Integer> slots_used) throws IndexOutOfBoundsException, IllegalArgumentException {
		int slot = Integer.parseInt(inputs[i + 1]);
		if (slot < 1 || slot > 3 || !slots_used.add(slot)) {
			throw new IllegalArgumentException();
		}

		SimpleDevelopmentCardSlot development_card_slots = player.getDevelopmentCardsSlots();
		if (slot == 1) {
			DevelopmentCard[] first_column = development_card_slots.getFirstColumn();
			return first_column[first_column.length - 1];
		} else if (slot == 2) {
			DevelopmentCard[] second_column = development_card_slots.getSecondColumn();
			return second_column[second_column.length - 1];
		} else {
			DevelopmentCard[] third_column = development_card_slots.getThirdColumn();
			return third_column[third_column.length - 1];
		}
	}

	private static ProductionInterface parseLeaderCardProduction(String[] inputs, int i, SimplePlayer player, HashSet<Integer> leader_used) throws IndexOutOfBoundsException, IllegalArgumentException {
		int index = Integer.parseInt(inputs[i + 1]);
		if (index < 1 || index > 2 || !leader_used.add(index)) {
			throw new IllegalArgumentException();
		}

		ArrayList<Resource> cost = new ArrayList<Resource>();
		cost.add(Resource.valueOf(inputs[i + 2].toUpperCase()));

		ArrayList<LeaderCard> player_deck = player.getDeck();
		LeaderCard leader_card = player_deck.get(index - 1);
		ProductionInterface production = (ProductionAbility) leader_card.getAbility();
		production.setProducedResources(cost);
		return production;
	}

	private static Message parseProductionMessage(String[] inputs, SimplePlayer player) throws IllegalArgumentException {
		try {
			boolean production_base = false;
			HashSet<Integer> slots_used = new HashSet<Integer>();
			HashSet<Integer> leader_used = new HashSet<Integer>();
			ArrayList<ProductionInterface> productions = new ArrayList<ProductionInterface>();
			for(int i = 1; i < inputs.length;) {
				switch (inputs[i].toUpperCase()) {
					case "BASE":
					case "B":
						productions.add(parseBaseProduction(inputs, i, production_base));
						production_base = true;
						i += 4;
						break;
					case "DEV":
					case "D":
						productions.add(parseDevelopmentCardProduction(inputs, i, player, slots_used));
						i += 2;
						break;
					case "LEADER":
					case "L":
						productions.add(parseLeaderCardProduction(inputs, i, player, leader_used));
						i += 3;
						break;
					default:
						throw new IllegalArgumentException();
				}
			}
			return new ProductionMessage(productions);
		} catch (ClassCastException | IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("production <base res1 res2 res3 | dev slot | leader index res>+");
		}
	}

	private static Message parsePayMessage(String[] inputs) throws IllegalArgumentException {
		Storage storage = new Storage();

		try {
			for(int i = 1; i < inputs.length; i += 3) {
				int num = Integer.parseInt(inputs[i]);
				Resource res = Resource.valueOf(inputs[i + 1].toUpperCase());
				ArrayList<Resource> resources_to_add = new ArrayList<Resource>();
				for (int j = 0; j < num; j++) {
					resources_to_add.add(res);
				}
				addToStorage(inputs[i + 2], resources_to_add, storage);
			}
			return new PayMessage(storage);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("pay <number resource (wt|wm|wb|s|l)>+");
		}
	}

	private static Message parseStoreMessage(String[] inputs) throws IllegalArgumentException {
		Storage storage = new Storage();

		try {
			for(int i = 1; i < inputs.length; i += 3) {
				int num = Integer.parseInt(inputs[i]);
				Resource res = Resource.valueOf(inputs[i + 1].toUpperCase());
				ArrayList<Resource> resources_to_add = new ArrayList<Resource>();
				for (int j = 0; j < num; j++) {
					resources_to_add.add(res);
				}
				addToStorage(inputs[i + 2], resources_to_add, storage);
			}
			return new StoreMessage(storage);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("store <number resource (wt|wm|wb|s|l)>+");
		}
	}

	private static Message parseEndTurnMessage(String[] inputs) throws IllegalArgumentException {
		if (inputs.length > 1 && !inputs[1].equals("turn")) {
			throw new IllegalArgumentException("endturn/end turn");
		}

		return new EndTurnMessage();
	}

	private static Message parseDiscardMessage(String[] inputs, SimplePlayer player) throws IllegalArgumentException {
		try {
			String diff = inputs[1].toUpperCase();
			if (diff.equals("RESOURCES") || diff.equals("R")) {
				return parseDiscardResourcesMessage(inputs);
			} else if (diff.equals("LEADER") || diff.equals("L")) {
				return parseDiscardLeaderCardMessage(inputs, player);
			}

			throw new IllegalArgumentException();
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
			if (e.getMessage() != null) {
				throw new IllegalArgumentException(e.getMessage());
			}
			throw new IllegalArgumentException();
		}
	}

	private static Message parseDiscardResourcesMessage(String[] inputs) throws IllegalArgumentException {
		return new DiscardResourcesMessage();
	}

	private static Message parseDiscardLeaderCardMessage(String[] inputs, SimplePlayer player) throws IllegalArgumentException {
		try {
			int index = Integer.parseInt(inputs[2]);
			ArrayList<LeaderCard> player_deck = player.getDeck();
			LeaderCard leader_card = player_deck.get(index - 1);
			return new DiscardLeaderMessage(leader_card);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("discard leader index");
		}
	}

	private static Message parseActivateLeaderCardMessage(String[] inputs, SimplePlayer player) throws IllegalArgumentException {
		try {
			String diff = inputs[1].toUpperCase();
			if (!diff.equals("LEADER") && !diff.equals("L")) {
				throw new IllegalArgumentException();
			}
			int index = Integer.parseInt(inputs[2]);
			ArrayList<LeaderCard> player_deck = player.getDeck();
			LeaderCard leader_card = player_deck.get(index - 1);
			return new ActivateLeaderMessage(leader_card);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("activate leader index");
		}
	}

	/**
	 * Add an array of resources to the storage depending on the input string
	 *
	 * @param input the String representing what Storage of the player to use (Warehouse, Strongbox or Extraspace)
	 * @param resources_to_add an ArrayList containg the Resources to add to the Storage
	 * @param storage the Storage to add the Resources to
	 */
	private static void addToStorage(String input, ArrayList<Resource> resources_to_add, Storage storage) throws IllegalArgumentException {
		switch(input.toUpperCase()) {
			case "WT":
				storage.addToWarehouseTop(resources_to_add);
				break;
			case "WM":
				storage.addToWarehouseMid(resources_to_add);
				break;
			case "WB":
				storage.addToWarehouseBot(resources_to_add);
				break;
			case "S":
				storage.addToStrongbox(resources_to_add);
				break;
			case "L":
				storage.addToExtraspace(resources_to_add);
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
}
