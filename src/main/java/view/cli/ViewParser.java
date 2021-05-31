package it.polimi.ingsw.view.cli;

import java.util.ArrayList;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.cli.Printer;

public class ViewParser {
	public static String parseInput(String input, SimpleGame game, ArrayList<SimplePlayer> players, Turn turn, String nickname) throws IllegalArgumentException {
		try {
			String[] inputs = input.split(" ");
			switch(inputs[1].toUpperCase()) {
				case "MARKET":
				case "M":
					return parseMarket(game);
				case "DEVELOPMENTCARDSONTABLE":
				case "DCOT":
					return parseDevelopmentCardsOnTable(game);
				case "FAITHTRACK":
				case "TRACK":
				case "T":
					return parseFaithTrack(inputs, players, nickname);
				case "WAREHOUSE":
				case "W":
					return parseWarehouse(inputs, players, nickname);
				case "STRONGBOX":
				case "S":
					return parseStrongbox(inputs, players, nickname);
				case "LEADERCARDS":
				case "L":
					return parseLeaderCards(inputs, players, nickname);
				case "DEVELOPMENTCARDSSLOTS":
				case "DCS":
					return parseDevelopmentCardsSlots(inputs, players, nickname);
				case "TURN":
				case "TU":
					return parseTurn(inputs, turn);
				default:
					throw new IllegalArgumentException();
			}
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("look market | developmentcardsontable | track <nickname> | warehouse <nickname> | strongbox <nickname> | leadercards <nickname> | developmentcardsslots <slot> <nickname> | turn");
		}
	}

	/**
	 * @param players all the SimplePlayers in the game
	 * @param nickname the nickname of the Player to look for
	 * @return the SimplePlayer with that nickname
	 */
	public static SimplePlayer getPlayerFromNickname(ArrayList<SimplePlayer> players, String nickname) throws IllegalArgumentException {
		for (SimplePlayer s: players) {
			if (s.getNickname().equals(nickname)) {
				return s;
			}
		}
		throw new IllegalArgumentException();
	}

	public static String parseMarket(SimpleGame game) throws IllegalArgumentException {
		return Printer.printMarket(game);
	}

	public static String parseDevelopmentCardsOnTable(SimpleGame game) throws IllegalArgumentException {
		return Printer.printDevelopmentCardsOnTable(game);
	}

	public static String parseFaithTrack(String[] inputs, ArrayList<SimplePlayer> players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printTrack(getPlayerFromNickname(players, nickname));
			} else {
				String player_nickname = inputs[2];
				return Printer.printTrack(getPlayerFromNickname(players, player_nickname));
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look track <nickname>");
		}
	}

	public static String parseWarehouse(String[] inputs, ArrayList<SimplePlayer> players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printWarehouse(getPlayerFromNickname(players, nickname));
			} else {
				String player_nickname = inputs[2];
				return Printer.printWarehouse(getPlayerFromNickname(players, player_nickname));
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look warehouse <nickname>");
		}
	}

	public static String parseStrongbox(String[] inputs, ArrayList<SimplePlayer> players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printStrongbox(getPlayerFromNickname(players, nickname));
			} else {
				String player_nickname = inputs[2];
				return Printer.printStrongbox(getPlayerFromNickname(players, player_nickname));
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look strongbox <nickname>");
		}

	}

	public static String parseLeaderCards(String[] inputs, ArrayList<SimplePlayer> players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printLeaderCards(getPlayerFromNickname(players, nickname), true);
			} else {
				String player_nickname = inputs[2];
				return Printer.printLeaderCards(getPlayerFromNickname(players, player_nickname), false);
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look leadercards <nickname>");
		}
	}

	public static String parseDevelopmentCardsSlots(String[] inputs, ArrayList<SimplePlayer> players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printDevelopmentCardsSlots(getPlayerFromNickname(players, nickname));
			} else {
				try {
					int slot = Integer.parseInt(inputs[2]);
					if (inputs.length == 3) {
						return Printer.printDevelopmentCardsSlots(getPlayerFromNickname(players, nickname), slot);
					} else {
						String player_nickname = inputs[3];
						return Printer.printDevelopmentCardsSlots(getPlayerFromNickname(players, player_nickname), slot);
					}
				} catch (NumberFormatException e) {
					String player_nickname = inputs[2];
					return Printer.printDevelopmentCardsSlots(getPlayerFromNickname(players, player_nickname));
				}
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look developmentcardsslots <slot> <nickname>");
		}
	}

	public static String parseTurn(String[] inputs, Turn turn) throws IllegalArgumentException {
		return Printer.printTurn(turn);
	}
}
