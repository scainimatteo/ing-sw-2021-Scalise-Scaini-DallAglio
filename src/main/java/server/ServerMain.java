package it.polimi.ingsw.server;

import java.util.Arrays;
import java.util.List;

import java.io.IOException;

import it.polimi.ingsw.server.Server;

public class ServerMain {
	static int port;
	static final int default_port = 1234;

	public static void main(String[] args) {
		parseArguments(args);
		startServer();
	}

	/**
	 * Parse the arguments coming from command line
	 *
	 * @param args the arguments
	 */
	public static void parseArguments(String[] args) {
		port = default_port;

		if (args.length < 2) {
			return;
		}

		List<String> arglist = Arrays.asList(args);

		// PORT
		if (arglist.contains("-p") || arglist.contains("--port")) {
			port = parsePort(arglist);
		}
	}

	public static void startServer() {
		try {
			new Server(port).run();
		} catch (IOException e) {
			System.out.printf("Can't run server. Try checking if the port %d is already in use\n", port);
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
}
