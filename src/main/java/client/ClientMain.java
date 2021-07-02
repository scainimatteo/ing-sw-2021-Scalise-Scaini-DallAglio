package it.polimi.ingsw.client;

import java.util.Arrays;
import java.util.List;

import java.io.IOException;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.View;

public class ClientMain {
	private static int port;
	private static String address;
	private static View view;
	private static final int default_port = 1234;
	private static final String default_address = "127.0.0.1";
	private static final View default_view = new GUI();

	public static void main(String[] args) {
		parseArguments(args);
		startClient();
	}

	/**
	 * Parse the arguments coming from command line
	 *
	 * @param args the arguments
	 */
	public static void parseArguments(String[] args) {
		port = default_port;
		address = default_address;
		view = default_view;

		if (args.length < 2) {
			return;
		}
		
		List<String> arglist = Arrays.asList(args);

		// PORT
		if (arglist.contains("-p") || arglist.contains("--port")) {
			port = parsePort(arglist);
		}

		// ADDRESS
		if (arglist.contains("-a") || arglist.contains("--address")) {
			address = parseAddress(arglist);
		}

		// VIEW
		if (arglist.contains("-v") || arglist.contains("--view")) {
			view = parseView(arglist);
		}
	}

	public static void startClient() {
		try {
			new Client(address, port, view).startClient();
		} catch (IOException e) {
			System.out.println("The connection was closed by the server");
			// the handleError will close the program
		}
	}

	/**
	 * @param arglist a List with all the command line arguments
	 * @return the port specified by command line
	 */
	private static int parsePort(List<String> arglist) {
		int index = arglist.indexOf("-p");
		if (index == -1) {
			index = arglist.indexOf("--port");
		}

		return Integer.parseInt(arglist.get(index + 1));
	}

	/**
	 * @param arglist a List with all the command line arguments
	 * @return the addres specified by command line
	 */
	private static String parseAddress(List<String> arglist) {
		int index = arglist.indexOf("-a");
		if (index == -1) {
			index = arglist.indexOf("--address");
		}

		return arglist.get(index + 1);
	}

	/**
	 * @param arglist a List with all the command line arguments
	 * @return the View specified by command line
	 */
	private static View parseView(List<String> arglist) {
		int index = arglist.indexOf("-v");
		if (index == -1) {
			index = arglist.indexOf("--view");
		}

		String view_string = arglist.get(index + 1);
		if (view_string.equals("cli")) {
			return new CLI();
		} else {
			return new GUI();
		}
	}
}
