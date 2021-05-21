package it.polimi.ingsw.view.cli;

import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.BuyCardMessage;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import it.polimi.ingsw.view.View;

public class CLI implements View {

	public void startView(Client client) {
		Scanner stdin = new Scanner(System.in);
		new Thread(() -> {
			while (true) {
				String inputLine = stdin.nextLine();
				try {
					Message parsed_message = parseInput(inputLine);
					client.sendMessage(parsed_message);
				} catch (IllegalArgumentException e) {
					System.out.println("Sorry, the command was malformed");
					if (!e.getMessage().equals("")) {
						System.out.println("The command should be: " + e.getMessage());
					}
				}
			}
		}).start();
	}

	public void updateView() {
		return;
	}

	public void handleError(ErrorMessage error_message) {
		return;
	}

	public void handleInitializing(InitializingServerMessage initializing_message) {
		return;
	}

	private Message parseInput(String input) throws IllegalArgumentException {
		//TODO: Oh ma qui nessuno ha il player
		String[] inputs = input.split(" ");
		switch(inputs[0].toUpperCase()) {
			case "BUYCARD":
			case "BUY":
			case "B":
				return parseBuyCardMessage(inputs);
			default:
				throw new IllegalArgumentException();
		}
	}

	private Message parseBuyCardMessage(String[] inputs) throws IllegalArgumentException {
		//TODO: test this
		int i = inputs[1].equals("CARD") ? 2 : 1;

		try {
			int level = Integer.parseInt(inputs[i]);
			i++;
			int color = DevelopmentCardsColor.valueOf(inputs[i]).getOrder();
			i++;
			int slot = Integer.parseInt(inputs[i]);
			// TODO: discountability
			return new BuyCardMessage();
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("buy level color slot");
		}
	}
}
