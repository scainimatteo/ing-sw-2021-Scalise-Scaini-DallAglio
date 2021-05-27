package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.model.resources.Resource;
import it.polimi.ingsw.model.player.track.Cell;

import it.polimi.ingsw.util.ANSI;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;
import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.view.cli.MessageParser;
import it.polimi.ingsw.view.cli.ViewParser;
import it.polimi.ingsw.view.View;

public class CLI extends View {
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
		if (!this.initializing && (inputs[0].toUpperCase().equals("LOOK") || inputs[0].toUpperCase().equals("L"))) {
			String to_view = ViewParser.parseInput(inputLine, this.simple_game, this.simple_players, this.nickname);
		} else {
			Message parsed_message = MessageParser.parseInput(inputLine, this.initializing, this.getMyPlayer());
			client.sendMessage(parsed_message);
		}
	}

	@Override
	public void handleError(ErrorMessage error_message) {
		if (error_message.nickname == null) {
			System.out.println(error_message.error_string);
		} else {
			if (error_message.nickname == this.getMyPlayer().getNickname()) {
				System.out.println(error_message.error_string);
			}
		}
	}

	@Override
	public void handleInitializing(InitializingServerMessage initializing_message) {
		//TODO: temporary?
		System.out.print(initializing_message.message);
		if (initializing_message.message.equals("Start Match\n\n")) {
			System.out.print("> ");
			this.initializing = false;
		} else if (initializing_message.message.equals("Nickname?")) {
			System.out.print("> ");
			this.nickname_flag = true;
		}
	}
}
