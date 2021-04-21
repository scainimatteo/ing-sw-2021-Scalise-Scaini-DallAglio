package it.polimi.ingsw.client;

import it.polimi.ingsw.client.Client;

import java.io.IOException;

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
		//TODO: We can do better that this
		port = default_port;
		address = default_address;

		if (args.length < 2) {
			return;
		}

		if (args[0].equals("-p")) {
			port = Integer.parseInt(args[1]);
		}
	}

	public static void startClient() {
		try {
			new Client(address, port).run();
		} catch (IOException e) {
			System.out.printf("Can't run client on address %s and port %d\n", address, port);
		}
	}
}
