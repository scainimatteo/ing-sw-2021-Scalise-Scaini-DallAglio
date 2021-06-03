package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
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
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.view.cli.MessageParser;
import it.polimi.ingsw.view.cli.ViewParser;
import it.polimi.ingsw.view.View;

public class CLI extends View {
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
}
