package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.message.InitializingMessage;
import it.polimi.ingsw.controller.InitialController;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.GameController;

import it.polimi.ingsw.server.ClientHandler;

import it.polimi.ingsw.util.ANSI;

public class Server {
	private int port;
	private ServerSocket server_socket;
	// contains for all the matches all the players that want to play in that match
	private HashMap<String, ArrayList<ClientHandler>> lobby;
	// contains for all the matches the number of players
	private HashMap<String, Integer> num_players;
	// contains all the nicknames - there cannot be two that are the same
	private HashSet<String> nicknames;
	private ExecutorService executor;
	private final int match_name_len = 7;

	public Server(int port) throws IOException {
		this.port = port;
		this.server_socket = new ServerSocket(this.port);
		this.lobby = new HashMap<String, ArrayList<ClientHandler>>();
		this.num_players = new HashMap<String, Integer>();
		this.executor = Executors.newCachedThreadPool();
		this.nicknames = new HashSet<String>();
	}

	/**
	 * Loops forever - accepts new players and inserts them in the lobby
	 */
	public void startServer() {
		System.out.println("Server starting on port " + ANSI.green(String.valueOf(this.port)));
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
						new_client_handler.setController(new InitialController());
						insertIntoLobby(new_client_handler);
					} catch (InterruptedException e) {
						System.out.println("Miscomunication with the client");
						new_client_handler.close("Miscomunication with the server");
					} catch (IllegalAccessError e) {
						System.out.println("Client failed to put right match name or tried to use an already existing nickname");
					} catch (InstantiationException e) {
						System.out.println("A match could not start");
					}
				}).start();
			} catch (IOException e) {
				System.out.println("Error while connecting to client");
			}
		}
	}

	/**
	 * @param client the client that has to be inserted in the lobby
	 */
	private void insertIntoLobby(ClientHandler client) throws IllegalAccessError, InterruptedException, InstantiationException {
		String match_name = manageClient(client);
		// TODO: check for racing conditions
		if (checkIfAllPlayersPresent(match_name)) {
			// the right amount of clients are connected, start the match
			for (ClientHandler ch : this.lobby.get(match_name)) {
				sendStringToClient(ch, "Start Match\n\n");
			}

			//TODO: if only one player, new_match = new SoloMatch
			GameController new_match = new GameController(this.lobby.get(match_name));
			for (ClientHandler c: this.lobby.get(match_name)) {
				c.setController(new_match);
			}
			this.executor.execute(new_match);
		}
	}

	/**
	 * @param client the client that just connected
	 * @return the name of match the client just connected to
	 */
	private String manageClient(ClientHandler client) throws IllegalAccessError, InterruptedException {
		//TODO: put all strings in a separate class
		sendStringToClient(client, "Nickname? ");
		String nickname = receiveStringFromClient(client);

		synchronized (this.nicknames) {
			// try to put the username, throw exception if it's already in the Set
			if (!this.nicknames.add(nickname)) {
				client.close("Sorry but the nickname " + nickname + " is already taken");
				throw new IllegalAccessError();
			}
		}
		client.setNickname(nickname);

		sendStringToClient(client, "Match name? ");
		String match_name = receiveStringFromClient(client);

		if (match_name.equals("")) {
			match_name = manageFirstClient(client);
			sendStringToClient(client, "Started match with match name " + match_name + "\n\n");
		} else {
			manageOtherClient(client, match_name);
		}
		return match_name;
	}

	/**
	 * @param first_client the first client of a match
	 * @return the name of the newly created match
	 */
	private String manageFirstClient(ClientHandler first_client) throws InterruptedException {
		int num;

		//TODO: put all strings in a separate class
		sendStringToClient(first_client, "How many player in match? ");
		try {
			num = Integer.parseInt(receiveStringFromClient(first_client));
		} catch (NumberFormatException e) {
			throw new InterruptedException();
		}
		String match_name = newMatchName();

		ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
		clients.add(first_client);

		synchronized(this.lobby) {
			this.lobby.put(match_name, clients);
			this.num_players.put(match_name, num);
		}
		return match_name;
	}

	/**
	 * @param client the client that wants to enter an existing match
	 * @param match_name the match to connect to
	 */
	private void manageOtherClient(ClientHandler client, String match_name) throws IllegalAccessError {
		synchronized(this.lobby) {
			if (this.lobby.containsKey(match_name)) {
				this.lobby.get(match_name).add(client);
			} else {
				client.close("Sorry but there is no current match named " + match_name);
				throw new IllegalAccessError();
			}
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

	/**
	 * Remove a nickname from the HashSet
	 *
	 * @param nickname the nickname to remove
	 */
	public void removeNickname(String nickname) {
		synchronized(this.nicknames) {
			this.nicknames.remove(nickname);
		}
	}

	/**
	 * @param client the ClientHandler of the client
	 * @param string the message to send
	 */
	public void sendStringToClient(ClientHandler client, String string) {
		ServerMessage message = new InitializingServerMessage(string);
		client.sendToClient(message);
	}

	/**
	 * @param client the ClientHandler of the client
	 * @return a message received
	 */
	public String receiveStringFromClient(ClientHandler client) throws InterruptedException {
		InitialController controller = (InitialController) client.getController();
		while (true) {
			try {
				return controller.receiveMessage();
			} catch (InterruptedException e) {
				continue;
			}
		}
	}
}
