package it.polimi.ingsw.server;

import java.io.*;

import java.net.*;

import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.server.Server;

public class ClientHandler implements Runnable {
	private Server server;
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String nickname;
	private Controller controller;
	private Player player;

	public ClientHandler(Server server, Socket client) throws IOException {
		this.server = server;
		this.client = client;
		this.out = new ObjectOutputStream(client.getOutputStream()); 
		this.in = new ObjectInputStream(client.getInputStream()); 
	}

	public Controller getController() {
		return this.controller;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * @param message the ServerMessage to send to the Client
	 */
	public void sendToClient(ServerMessage message) {
		try {
			out.reset();
			out.writeObject(message);
			out.flush();
		} catch (IOException e) {
			System.out.println("Connection closed");
		}
	}

	/**
	 * Safely close the connection with the client
	 */
	public void close(String error_message) {
		try {
			this.sendToClient(new ErrorMessage(error_message));
			this.server.removeNickname(this.nickname);
			this.client.close();
		} catch (IOException e) {
			System.out.println("Connection closed");
		}
	}

	/**
	 * Receive messages from the Client and use the Controller to handle them
	 */
	public void run() {
		while (true) {
			try {
				Message message = (Message) in.readObject();
				message.setPlayer(this.player);
				this.controller.handleMessage(message);
			} catch (IOException e) {
				this.close("Connection closed");
				break;
			} catch (ClassNotFoundException e) {
				System.out.println("The client sent something not understandable");
			}
		}
	}
}
