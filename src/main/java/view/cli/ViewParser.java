package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.cli.Printer;

public class ViewParser {
	public static String parseInput(String input, SimpleGame game, SimplePlayer[] players, String nickname) throws IllegalArgumentException {
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
				default:
					throw new IllegalArgumentException();
			}
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("look <market | developmentcardsontable | track <nickname> | warehouse <nickname> | strongbox <nickname> | leadercards <nickname> | developmentcardsslots <nickname>>");
		}
	}

	/**
	 * @param players all the SimplePlayers in the game
	 * @param nickname the nickname of the Player to look for
	 * @return the SimplePlayer with that nickname
	 */
	public static SimplePlayer getPlayerFromNickname(SimplePlayer[] players, String nickname) throws IllegalArgumentException {
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

	public static String parseFaithTrack(String[] inputs, SimplePlayer[] players, String nickname) throws IllegalArgumentException {
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

	public static String parseWarehouse(String[] inputs, SimplePlayer[] players, String nickname) throws IllegalArgumentException {
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

	public static String parseStrongbox(String[] inputs, SimplePlayer[] players, String nickname) throws IllegalArgumentException {
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

	public static String parseLeaderCards(String[] inputs, SimplePlayer[] players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printLeaderCards(getPlayerFromNickname(players, nickname));
			} else {
				String player_nickname = inputs[2];
				return Printer.printLeaderCards(getPlayerFromNickname(players, player_nickname));
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look leadercards <nickname>");
		}
	}

	public static String parseDevelopmentCardsSlots(String[] inputs, SimplePlayer[] players, String nickname) throws IllegalArgumentException {
		try {
			if (inputs.length == 2) {
				return Printer.printLeaderCards(getPlayerFromNickname(players, nickname));
			} else {
				String player_nickname = inputs[2];
				return Printer.printLeaderCards(getPlayerFromNickname(players, player_nickname));
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("look developmentcardsslots <nickname>");
		}
	}
}