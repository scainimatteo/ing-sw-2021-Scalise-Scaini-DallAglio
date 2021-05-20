package it.polimi.ingsw.view.cli;

import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import it.polimi.ingsw.controller.util.ArrayChooser;
import it.polimi.ingsw.controller.util.TurnSelector;
import it.polimi.ingsw.controller.util.ViewMessage;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;
import it.polimi.ingsw.controller.util.Choice;

import it.polimi.ingsw.util.ANSI;

import it.polimi.ingsw.view.Viewable;
import it.polimi.ingsw.view.ViewType;
import it.polimi.ingsw.view.View;

public class NetworkManagerCLI implements View {
	private Message message_to_parse;

	/**
	 * @return true if the message_to_parse is parsed
	 */
	public boolean isMessageParsed() {
		return this.message_to_parse.isParsed();
	}

	public void setMessageToParse(Message message_to_parse) {
		this.message_to_parse = message_to_parse;
	}

	// RECEIVE

	/**
	 * Print the String received
	 *
	 * @param s the String received
	 */
	public void handleString(String s) {
		System.out.print(s);
	}

	/**
	 * Print the elements of the Array
	 *
	 * @param array_chooser the ArrayChooser received
	 */
	public void handleArray(ArrayChooser array_chooser) {
		System.out.println(array_chooser.getMessage());
		Object[] array = array_chooser.getArray();
		for (int i = 1; i < array.length + 1; i++) {
			System.out.printf("%d. %s\n", i, array[i - 1]);
		}
		System.out.printf("Put the index of the element chosen: ");
	}

	/**
	 * Print the message of the Choice received
	 *
	 * @param c the Choice received
	 */
	public void handleChoice(Choice c) {
		System.out.println(c.getMessage() + " [y/n] ");
	}

	/**
	 * Print the message of the TurnSelector received
	 *
	 * @param t the TurnSelector received
	 */
	public void handleTurn(TurnSelector t) {
		System.out.println(t.getMessage());
		System.out.printf("Put the index of the turn chosen: ");
	}

	/**
	 * Print the ranking of the Players
	 *
	 * @param rank the HashMap representing the rank received
	 */
	public void handleRank(HashMap<String, Integer> rank) {
		String[] sorted_players = sortPlayers(rank);

		String rank_string = "Final scores:\n\n";
		for (int i = 0; i < sorted_players.length; i++) {
			rank_string += (i + 1) + ". " + sorted_players[i] + ": " + rank.get(sorted_players[i]) + " points\n";
		}

		System.out.println(rank);
	}

	/**
	 * Sort players based on the number of victory points they got
	 *
	 * @return an array with the nicknames representing the Players in ascending order
	 */
	private String[] sortPlayers(HashMap<String, Integer> rank) {
		String[] order = new String[rank.keySet().size()];
		ArrayList<Integer> values = new ArrayList<Integer>(rank.values());
		Collections.sort(values);

		for (String s: rank.keySet()) {
			int index = values.indexOf(rank.get(s));
			order[index] = s;
			values.set(index, values.get(index) + 1);
		}
		return order;
	}

	/**
	 * Print the Viewable requested
	 *
	 * @param view_message the ViewMessage received
	 */
	public void handleViewReply(ViewMessage view_message) {
		Viewable viewable = view_message.getReply();
		String nickname = view_message.getNickname();
		if (nickname != null) {
			System.out.println(ANSI.underline(nickname) + "\n\n");
		}
		System.out.println(viewable.printText() + "\n");
	}


	// SEND
	
	/**
	 * Get the input by reading stdin
	 *
	 * @param message_to_parse the message that is being parsed
	 */
	public Message getInput(Message message_to_parse) {
		this.message_to_parse = message_to_parse;
		Scanner stdin = new Scanner(System.in);
		String inputLine = stdin.nextLine();
		return parseSend(inputLine);
	}

	/**
	 * Parse the string to send so that you can send objects
	 *
	 * @param input the string written on the console
	 * @return the Message that the string represents
	 */
	private Message parseSend(String input) {
		Message message = null;

		if (this.message_to_parse == null) {
			message = new Message(MessageType.VIEWREQUEST, sendViewRequest(input));
			this.message_to_parse = message;
			message.setParsed();
		} else {
			switch(this.message_to_parse.getMessageType()) {
				case ARRAYCHOOSER:
					message = new Message(MessageType.ARRAYCHOOSER, sendArray(input));
					break;
				case CHOICE:
					message = new Message(MessageType.CHOICE, sendChoice(input));
					break;
				case TURNSELECTOR:
					message = new Message(MessageType.TURNSELECTOR, sendTurn(input));
					break;
			}
		}

		if (message == null) {
			try {
				// INTEGER
				message = new Message(MessageType.INTEGER, Integer.parseInt(input));
				this.message_to_parse.setParsed();
			} catch (NumberFormatException e) {
				// FALLBACK
				message = new Message(MessageType.STRING, input);
				this.message_to_parse.setParsed();
			}
		}

		return message;
	}

	/**
	 * Parse the input as an index of the Array and send the ArrayChooser if it's complete
	 *
	 * @param input a String representing an index of the Array
	 * @return the complete ArrayChooser or null if it's not complete (the parsing will continue)
	 */
	private ArrayChooser sendArray(String input) {
		try {
			int chosen = Integer.parseInt(input);
			ArrayChooser array_chooser = (ArrayChooser) this.message_to_parse.getMessage();

			if (array_chooser.setChosen(chosen)) {
				if (array_chooser.isComplete()) {
					this.message_to_parse.setParsed();
					return array_chooser;
				}
			}
		} catch (NumberFormatException e) {
			//TODO: print an error
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse the input as a Choice and send the Choice
	 *
	 * @param input the String rapresenting the choice made
	 * @return the Choice representing the choice made
	 */
	private Choice sendChoice(String input){
		Choice c = new Choice("");
		if (input.equals("y")) {
			c.setResponse(true);
		} else {
			c.setResponse(false);
		}
		this.message_to_parse.setParsed();
		return c;
	}

	/**
	 * Parse the input as a TurnSelector and send the TurnSelector
	 *
	 * @param input the String rapresenting the turn chosen
	 * @return the TurnSelector representing the turn chosen
	 */
	private TurnSelector sendTurn(String input) {
		try {
			int chosen = Integer.parseInt(input);
			TurnSelector turn = (TurnSelector) this.message_to_parse.getMessage();

			if (turn.setChosen(chosen)) {
				this.message_to_parse.setParsed();
				return turn;
			}
		} catch (NumberFormatException e) {
			//TODO: print an error
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse the input as a ViewMessage
	 *
	 * @param input the String rapresenting the Viewable chosen
	 * @return the ViewMessage representing the Viewable chosen
	 */
	private ViewMessage sendViewRequest(String input) {
		ViewMessage request = null;
		String[] inputs = input.split(" ");
		switch(inputs[0].toUpperCase()) {
			case "FAITHTRACK":
			case "TRACK":
				if (inputs.length == 1) {
					// the user requested his own FaithTrack
					request = new ViewMessage(ViewType.FAITHTRACK);
				} else {
					request = new ViewMessage(ViewType.FAITHTRACK, inputs[1]);
				}
				break;
			case "MARKET":
				request = new ViewMessage(ViewType.MARKET);
				break;
			case "DEVELOPMENTCARDSONTABLE":
			case "DCOT":
				request = new ViewMessage(ViewType.DEVELOPMENTCARDSONTABLE);
				break;
			case "STRONGBOX":
			case "SB":
				if (inputs.length == 1) {
					// the user requested his own StrongBox
					request = new ViewMessage(ViewType.STRONGBOX);
				} else {
					request = new ViewMessage(ViewType.STRONGBOX, inputs[1]);
				}
				break;
			case "WAREHOUSE":
			case "WH":
				if (inputs.length == 1) {
					// the user requested his own Warehouse
					request = new ViewMessage(ViewType.WAREHOUSE);
				} else {
					request = new ViewMessage(ViewType.WAREHOUSE, inputs[1]);
				}
				break;
			case "DEVELOPMENTCARDSSLOTS":
			case "DSC":
				if (inputs.length == 1) {
					// the user requested his own DevelopmentCardsSlots
					request = new ViewMessage(ViewType.DEVELOPMENTCARDSSLOTS);
				} else {
					request = new ViewMessage(ViewType.DEVELOPMENTCARDSSLOTS, inputs[1]);
				}
				break;
		}

		return request;
	}
}
