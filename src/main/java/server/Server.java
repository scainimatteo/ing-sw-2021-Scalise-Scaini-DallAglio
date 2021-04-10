package it.polimi.ingsw.server;

import java.net.*;

import java.io.IOException;

import it.polimi.ingsw.server.ClientHandler;

public class Server {
	int port;
	ServerSocket serverSocket;
	ClientHandler[] players;
	int connected_players = 0;

	public Server(int port) throws IOException {
		this.port = port;
		this.serverSocket = new ServerSocket(this.port);
	}

	public void run() {
		// print the port in green -> TODO: make a static class with all the ANSI codes
		System.out.printf("Server starting on port \u001B[32m%d\u001B[0m\n\n", this.port);
		while (true) {
			try {
				Socket new_client = this.serverSocket.accept();
				ClientHandler new_client_handler = new ClientHandler(this, new_client);
				if (connected_players == 0) {
					manageFirstClient(new_client_handler);
				}
			} catch (IOException e) {
				System.out.println("Error while connecting to client");
			}
		}
	}

	private void manageFirstClient(ClientHandler first_client) {
		return;
	}
}
