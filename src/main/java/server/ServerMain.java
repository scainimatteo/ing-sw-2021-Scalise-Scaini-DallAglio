package it.polimi.ingsw.server;

import it.polimi.ingsw.server.Server;

import java.io.IOException;

public class ServerMain {
	static int port;
	static final int default_port = 1234;

	public static void main(String[] args) {
		parseArguments(args);
		startServer();
	}

	//TODO: We can do better that this
	public static void parseArguments(String[] args) {
		port = default_port;

		if (args.length < 2) {
			return;
		}

		if (args[0].equals("-p")) {
			port = Integer.parseInt(args[1]);
		}
	}

	public static void startServer() {
		try {
			new Server(port).run();
		} catch (IOException e) {
			System.out.printf("Can't run server. Try checking if the port %d is already in use\n", port);
		}
	}
}
