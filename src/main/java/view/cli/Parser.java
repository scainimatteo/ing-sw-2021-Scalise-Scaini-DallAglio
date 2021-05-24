package it.polimi.ingsw.view.cli;

import java.util.ArrayList;

import it.polimi.ingsw.controller.message.InitializingMessage;
import it.polimi.ingsw.controller.message.BuyCardMessage;
import it.polimi.ingsw.controller.message.MarketMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.resources.Resource;

public class Parser {
	/**
	 * Parse the input from the command line
	 *
	 * @param input the String form the CLI
	 * @return the Message created to send to the client
	 * @throws IllegalArgumentException if the input cannot be parsed
	 */
	public static Message parseInput(String input, boolean initializing) throws IllegalArgumentException {
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
			default:
				throw new IllegalArgumentException();
		}
	}

	private static Message parseBuyCardMessage(String[] inputs) throws IllegalArgumentException {
		try {
			int level = Integer.parseInt(inputs[1]);
			int color = DevelopmentCardsColor.valueOf(inputs[2]).getOrder();
			int slot = Integer.parseInt(inputs[3]);
			return new BuyCardMessage(level, color, slot);
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("buy level color slot");
		}
	}

	private static Message parseMarketMessage(String[] inputs) throws IllegalArgumentException {
		try {
			boolean row_or_column = inputs[1].equals("row") ? false : true;
			int index = Integer.parseInt(inputs[2]);
			if (inputs.length == 3) {
				return new MarketMessage(row_or_column, index);
			}

			ArrayList<Resource> white_marbles = new ArrayList<Resource>();
			for (int i = 3; i < 7; i++) {
				int num = Integer.parseInt(inputs[i]);
				Resource res = Resource.valueOf(inputs[i + 1]);
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
			throw new IllegalArgumentException("market row/column index <number color> <number color>");
		}
	}
}
