package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.match.Match;

import it.polimi.ingsw.server.ClientHandler;

public class Server {
	private int port;
	private ServerSocket server_socket;
	// contains for all the matches all the players that want to play in that match
	private HashMap<String, ArrayList<ClientHandler>> lobby;
	// contains for all the matches the number of players
	private HashMap<String, Integer> num_players;
	private ExecutorService executor;
	private final int match_name_len = 7;

	public Server(int port) throws IOException {
		this.port = port;
		this.server_socket = new ServerSocket(this.port);
		this.lobby = new HashMap<String, ArrayList<ClientHandler>>();
		this.num_players = new HashMap<String, Integer>();
		this.executor = Executors.newCachedThreadPool();
	}

	/**
	 * Loops forever - accepts new players and inserts them in the lobby
	 */
	public void run() {
		// print the port in green -> TODO: make a static class with all the ANSI codes
		System.out.printf("Server starting on port \u001B[32m%d\u001B[0m\n\n", this.port);
		while (true) {
			try {
				// waits until a new client connects
				Socket new_client = this.server_socket.accept();
				ClientHandler new_client_handler = new ClientHandler(this, new_client);
				// start the ClientHandler
				this.executor.execute(new_client_handler);

				// insert the clients in the lobby
				new Thread(() -> {
					try {
						insertIntoLobby(new_client_handler);
					} catch (InterruptedException e) {
						System.out.println("Miscomunication with the client");
						new_client_handler.close();
					}
				}).start();

			} catch (IOException e) {
				System.out.println("Error while connecting to client");
			} catch (IllegalAccessError e) {
				System.out.println("Client failed to put right match name");
			}
		}
	}

	/**
	 * @param client the client that has to be inserted in the lobby
	 */
	private synchronized void insertIntoLobby(ClientHandler client) throws IllegalAccessError, InterruptedException {
		String match_name = manageClient(client);
		if (checkIfAllPlayersPresent(match_name)) {
			// the right amount of clients are connected, start the match
			//TODO: start match
			for (ClientHandler ch : this.lobby.get(match_name)) {
				ch.asyncSendToClient("Start Match");
			}
			//TODO: if only one player, new_match = new SoloMatch
			Runnable new_match = new Match(this.lobby.get(match_name));
			this.executor.execute(new_match);
		}
	}

	/**
	 * @param client the client that just connected
	 * @return the name of match the client just connected to
	 */
	private synchronized String manageClient(ClientHandler client) throws IllegalAccessError, InterruptedException {
		//TODO: put all strings in a separate class
		client.asyncSendToClient("Nickname? ");
		String nickname = (String) client.asyncReceiveFromClient();
		client.setNickname(nickname);

		client.asyncSendToClient("Match name? ");
		String match_name = (String) client.asyncReceiveFromClient();

		if (match_name.equals("")) {
			match_name = manageFirstClient(client);
			client.asyncSendToClient("Started match with match name " + match_name);
		} else {
			manageOtherClient(client, match_name);
		}
		return match_name;
	}

	/**
	 * @param first_client the first client of a match
	 * @return the name of the newly created match
	 */
	private synchronized String manageFirstClient(ClientHandler first_client) throws InterruptedException {
		Integer num;

		//TODO: put all strings in a separate class
		first_client.asyncSendToClient("How many player in match? ");
		try {
			num = (Integer) first_client.asyncReceiveFromClient();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		}
		String match_name = newMatchName();

		ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
		clients.add(first_client);

		this.lobby.put(match_name, clients);
		this.num_players.put(match_name, num);
		return match_name;
	}

	/**
	 * @param client the client that wants to enter an existing match
	 * @param match_name the match to connect to
	 */
	private synchronized void manageOtherClient(ClientHandler client, String match_name) throws IllegalAccessError {
		if (this.lobby.containsKey(match_name)) {
			this.lobby.get(match_name).add(client);
		} else {
			client.asyncSendToClient("Sorry but there is no current match named " + match_name);
			client.close();
			throw new IllegalAccessError();
		}
	}

	/**
	 * Choose a random string of uppercase letter to use as a match name
	 *
	 * @return a randomized name for the match
	 */
	private synchronized String newMatchName() {
		Random random = new Random();
		String match_name = "";
		for (int i = 0; i < this.match_name_len; i++) {
			// random integer that represents a ascii uppercase letter
			int randint = random.nextInt(26) + 65;
			match_name = match_name + Character.toString((char) randint);
		}
		return match_name;
	}

	/**
	 * @param match_name the match to check
	 * @return if all the client have connected to the match
	 */
	private synchronized boolean checkIfAllPlayersPresent(String match_name) {
		return this.lobby.get(match_name).size() == this.num_players.get(match_name);
	}
}
