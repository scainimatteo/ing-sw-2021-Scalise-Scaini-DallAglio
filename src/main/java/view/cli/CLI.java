package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.player.track.Cell;

import it.polimi.ingsw.util.ANSI;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.InitializingMessageType;
import it.polimi.ingsw.controller.servermessage.EndGameMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.view.cli.MessageParser;
import it.polimi.ingsw.view.cli.ViewParser;
import it.polimi.ingsw.view.View;

public class CLI extends View {
	private boolean nickname_flag = true;

	public CLI() {
		this.simple_players = new ArrayList<SimplePlayer>();
	}

	/**
	 * Start a thread parsing the strings passed from the command line
	 *
	 * @param client the Client using the View
	 */
	@Override
	public void startView(Client client) {
		Scanner stdin = new Scanner(System.in);
		new Thread(() -> {
			while (true) {
				System.out.print("> ");
				String inputLine = stdin.nextLine();
				try {
					if (this.nickname_flag) {
						this.nickname = inputLine;
						this.nickname_flag = false;
					}

					parseInput(client, inputLine);
				} catch (IllegalArgumentException e) {
					System.out.println("Sorry, the command was malformed");
					if (e.getMessage() != null) {
						System.out.println("The command should be: " + e.getMessage());
					}
				}
			}
		}).start();
	}

	/**
	 * Use the two Parsers to parse the input string
	 *
	 * @param client the Client using the View
	 * @param inputLine the String written by the Client
	 */
	private void parseInput(Client client, String inputLine) {
		String[] inputs = inputLine.split(" ");
		if (this.initialized && (inputs[0].toUpperCase().equals("LOOK") || inputs[0].toUpperCase().equals("L"))) {
			String to_view = ViewParser.parseInput(inputLine, this.simple_game, this.simple_players, this.turn, this.nickname);
			System.out.println(to_view);
		} else {
			Message parsed_message = MessageParser.parseInput(inputLine, this.initialized, this.getMyPlayer());
			client.sendMessage(parsed_message);
		}
	}

	@Override
	public void handleError(ErrorMessage error_message) {
		if (!this.initialized) {
			System.out.println(error_message.error_string);
			System.exit(1);
		} else if (error_message.nickname == null || error_message.nickname.equals(this.getMyPlayer().getNickname())) {
			System.out.println(error_message.error_string);
			System.out.print("> ");
		}
	}

	@Override
	public void handleInitializing(InitializingServerMessage initializing_message) {
		System.out.print(initializing_message.message);
		if (initializing_message.type == InitializingMessageType.START_MATCH) {
			System.out.print("> ");
			this.initialized = true;
		} else if (initializing_message.type == InitializingMessageType.NICKNAME) {
			this.nickname_flag = true;
		}
	}

	@Override
	public void handleEndGame(EndGameMessage end_game_message) {
		ArrayList<String> nicknames = end_game_message.nicknames;
		HashMap<String, Integer> rank = end_game_message.rank;
		if (nicknames.size() > 1) {
			// if there were more than 1 player
			handleEndMultipleGame(nicknames, rank);
		} else {
			// if it was a SoloGame
			handleEndSoloGame(rank.get(this.nickname));
		}
		System.out.println("Thanks for playing with us!");
		System.exit(0);
	}

	/**
	 * Print the rankings of the Players, with the points they scored
	 *
	 * @param rank an HashMap containing the nickname of the Players and their points
	 */
	private void handleEndMultipleGame(ArrayList<String> nicknames, HashMap<String, Integer> rank){
		System.out.println("The game has ended! These are the rankings:");
		for (String nick: nicknames) {
			System.out.println(nick + ": " + rank.get(nick) + " points");
		}
	}

	/**
	 * Print if the player has lost or, if they hasn't, print how many points they scored
	 *
	 * @param points the points of the Player
	 */
	private void handleEndSoloGame(int points){
		if (points == 0) {
			System.out.println("You lost");
		} else {
			System.out.println("You won with " + points + " points!");
		}
	}
}
