package it.polimi.ingsw.view.cli;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.view.cli.Parser;
import it.polimi.ingsw.view.View;

public class CLI implements View {
	private boolean initializing = true;

	/**
	 * Start a thread parsing the strings passed from the command line
	 */
	public void startView(Client client) {
		Scanner stdin = new Scanner(System.in);
		new Thread(() -> {
			while (true) {
				System.out.print("> ");
				String inputLine = stdin.nextLine();
				try {
					Message parsed_message = Parser.parseInput(inputLine, this.initializing);
					client.sendMessage(parsed_message);
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
	 * Update the simple model after a ViewUpdate
	 */
	public void updateView() {
		return;
	}

	/**
	 * Print the error message
	 *
	 * @param error_message the ErrorMessage received from the Server
	 */
	public void handleError(ErrorMessage error_message) {
		return;
	}

	/**
	 * Print the message during the initalization phase
	 *
	 * @param error_message the ErrorMessage received from the Server
	 */
	public void handleInitializing(InitializingServerMessage initializing_message) {
		//TODO: temporary?
		System.out.print(initializing_message.message);
		if (initializing_message.message.equals("Start Match\n\n")) {
			System.out.print("> ");
			this.initializing = false;
		}
	}
}
