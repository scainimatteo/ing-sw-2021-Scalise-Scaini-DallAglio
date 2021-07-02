package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.InitializingMessageType;
import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.message.InitializingMessage;
import it.polimi.ingsw.controller.SoloGameController;
import it.polimi.ingsw.controller.InitialController;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.GameController;

import it.polimi.ingsw.server.persistence.PersistenceParser;
import it.polimi.ingsw.server.persistence.PersistenceUtil;
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
	private final int match_name_len = 7;

	public Server(int port) throws IOException {
		this.port = port;
		this.server_socket = new ServerSocket(this.port);
		this.lobby = new HashMap<String, ArrayList<ClientHandler>>();
		this.num_players = new HashMap<String, Integer>();
		this.nicknames = new HashSet<String>();
	}

	/**
	 * Loops forever - accepts new players and inserts them in the lobby
	 */
	public void startServer() {
		System.out.println("Server starting on port " + ANSI.green(String.valueOf(this.port)));
		// create persistence directory using OS conventions
		PersistenceUtil.getPersistenceDirectory().toFile().mkdir();

		while (true) {
			try {
				// waits until a new client connects
				Socket new_client = this.server_socket.accept();
				ClientHandler new_client_handler = new ClientHandler(this, new_client);
				// start the ClientHandler
				new Thread(new_client_handler).start();

				// insert the clients in the lobby
				new Thread(() -> {
					try {
						new_client_handler.setController(new InitialController());
						insertIntoLobby(new_client_handler);
					} catch (InterruptedException e) {
						System.out.println("Miscomunication with the client");
						new_client_handler.close("Miscomunication with the server, you will be disconnected");
					} catch (NumberFormatException e) {
						new_client_handler.close("A match can only consist of 1 to 4 players");
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
	private void insertIntoLobby(ClientHandler client) throws IllegalAccessError, InterruptedException, InstantiationException, NumberFormatException {
		String match_name = manageClient(client);
		if (checkIfAllPlayersPresent(match_name)) {
			// the right amount of clients are connected, start the match
			for (ClientHandler ch : this.lobby.get(match_name)) {
				sendStringToClient(ch, "Start Match\n\n", InitializingMessageType.START_MATCH);
			}

			// create, initialize and start a new Game or SoloGame or recreate a persistent match
			GameController new_match = chooseGameController(match_name);

			for (ClientHandler c: this.lobby.get(match_name)) {
				c.setController(new_match);
			}
		}
	}

	/**
	 * Check if a match is a SoloGame or not
	 *
	 * @param match_name the name of the match to check
	 * @return true if in the match there's only one Client allowed
	 */
	private boolean isSoloGame(String match_name) {
		return this.lobby.get(match_name).size() == 1;
	}

	/**
	 * Choose the right GameController based on the match_name and setup it accordingly
	 *
	 * @param match_name the name of the match to create or recreate
	 * @return the appropriate GameController or SoloGameController
	 */
	private GameController chooseGameController(String match_name) throws InstantiationException {
		// if it's a match_name that's saved in memory
		if (PersistenceUtil.checkPersistence(match_name)) {
			if (isSoloGame(match_name)) {
				return new SoloGameController(this.lobby.get(match_name), match_name);
			} else {
				return new GameController(this.lobby.get(match_name), match_name);
			}
		} 
		
		GameController new_match;
		if (isSoloGame(match_name)) {
			new_match = new SoloGameController(this.lobby.get(match_name));
		} else {
			new_match = new GameController(this.lobby.get(match_name));
		}

		new_match.setMatchName(match_name);
		new_match.setupGame();
		return new_match;
	}

	/**
	 * @param client the client that just connected
	 * @return the name of match the client just connected to
	 */
	private String manageClient(ClientHandler client) throws IllegalAccessError, InterruptedException, NumberFormatException {
		sendStringToClient(client, "Choose a nickname: ", InitializingMessageType.NICKNAME);
		String nickname = receiveStringFromClient(client);

		synchronized (this.nicknames) {
			//TODO: refute null nicknames
			// try to put the username, throw exception if it's already in the Set
			if (!this.nicknames.add(nickname)) {
				client.close("Sorry but the nickname " + nickname + " is already taken");
				throw new IllegalAccessError();
			}
		}
		client.setNickname(nickname);

		sendStringToClient(client, "To join an already existing match, put the match name here, leave blank otherwise: ", InitializingMessageType.CHOOSE_MATCH_NAME);
		String match_name = receiveStringFromClient(client).toUpperCase();

		if (match_name.equals("")) {
			match_name = manageFirstClient(client);
			sendStringToClient(client, "Started match with match name " + match_name + "\n\nSend this match name to the other players to start playing\n\n", InitializingMessageType.STARTED_MATCH_NAME, match_name);
		} else if (PersistenceUtil.checkPersistence(match_name)) {
			// if the match_name put from the Client matches a saved match, create or join that one
			createOrJoinPersistenceGame(client, match_name, nickname);
		} else {
			manageOtherClient(client, match_name);
		}
		return match_name;
	}

	/**
	 * @param first_client the first client of a match
	 * @return the name of the newly created match
	 */
	private String manageFirstClient(ClientHandler first_client) throws InterruptedException, NumberFormatException {
		int num;

		sendStringToClient(first_client, "How many players will the match have? ", InitializingMessageType.NUM_PLAYERS);
		num = Integer.parseInt(receiveStringFromClient(first_client));
		if (num < 1 || num > 4) {
			throw new NumberFormatException();
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
				// if the match isn't already full, add it to the match
				if (this.lobby.get(match_name).size() < this.num_players.get(match_name)) {
					this.lobby.get(match_name).add(client);
					sendStringToClient(client, "Joined match with match name " + match_name + "\n\nSend this match name to the other players to start playing\n\n", InitializingMessageType.JOINED_MATCH);
				} else {
					client.close("Sorry but the match " + match_name + " is already at full capacity");
					throw new IllegalAccessError();
				}
			} else {
				client.close("Sorry but there is no current match named " + match_name);
				throw new IllegalAccessError();
			}
		}
	}

	/**
	 * If it's the first user, create a match using the parameters saved in memory
	 * If it's not the first user, join that match
	 *
	 * @param client the ClientHandler that wants to create or join the match
	 * @param match_name the identifier of the saved match
	 * @param nickname the nickname of the ClientHandler that wants to create or join (has to be the same of the saved match)
	 */
	private void createOrJoinPersistenceGame(ClientHandler client, String match_name, String nickname) throws IllegalAccessError, InterruptedException {
		try {
			ArrayList<String> match_nicknames = PersistenceParser.getAllClientsNickname(match_name);
			int match_clients_number = PersistenceParser.getClientsNumber(match_name);
			// check if nickname is correct
			if (!match_nicknames.contains(nickname)) {
				client.close("Sorry but you can't partecipate in the match " + match_name + " with the nickname " + nickname);
				throw new IllegalAccessError();
			}

			// if it's first create clients and add to lobby
			if (!this.lobby.containsKey(match_name)) {
				ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
				clients.add(client);
				synchronized(this.lobby) {
					this.lobby.put(match_name, clients);
					this.num_players.put(match_name, match_clients_number);
				}
			} else {
				// treat like any other ClientHandler
				manageOtherClient(client, match_name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InterruptedException();
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
		// if a file with that match_name exists, call the function again
		if (new File(PersistenceUtil.getPersistenceFileFromMatchName(match_name)).exists()) {
			match_name = newMatchName();
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
	 * @param type the InitializingMessageType of the message to send
	 */
	public void sendStringToClient(ClientHandler client, String string, InitializingMessageType type) {
		ServerMessage message = new InitializingServerMessage(string, type);
		client.sendToClient(message);
	}

	/**
	 * @param client the ClientHandler of the client
	 * @param string the message to send
	 * @param type the InitializingMessageType of the message to send
	 * @param match_name the name of the match that was just created
	 */
	public void sendStringToClient(ClientHandler client, String string, InitializingMessageType type, String match_name) {
		ServerMessage message = new InitializingServerMessage(string, type, match_name);
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
