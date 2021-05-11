package it.polimi.ingsw.client;

import java.util.Arrays;
import java.util.List;

import java.io.IOException;

import it.polimi.ingsw.client.Client;

import it.polimi.ingsw.view.cli.NetworkManagerCLI;
import it.polimi.ingsw.view.View;

public class ClientMain {
	private static int port;
	private static String address;
	private static final int default_port = 1234;
	private static final String default_address = "127.0.0.1";

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
	}

	public static void startClient() {
		try {
			View view = new NetworkManagerCLI();
			new Client(address, port, view).run();
		} catch (IOException e) {
			System.out.printf("Can't run client on address %s and port %d\n", address, port);
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
}
