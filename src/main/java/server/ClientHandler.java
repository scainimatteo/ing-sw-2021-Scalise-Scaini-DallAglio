package it.polimi.ingsw.server;

import java.io.*;

import java.net.*;

import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.message.Message;
import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.server.Server;


public class ClientHandler implements Runnable {
	private Server server;
	private Socket client;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private String nickname;
	private Controller controller;

	public ClientHandler(Server server, Socket client) throws IOException {
		this.server = server;
		this.client = client;
		this.out = new ObjectOutputStream(client.getOutputStream()); 
		this.in = new ObjectInputStream(client.getInputStream()); 
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void sendToClient(ServerMessage message) {
		try {
			while (true) {
				out.reset();
				out.writeObject(message);
				out.flush();
			}
		} catch (Exception e) {
			// TODO: better exception handling
			e.printStackTrace();
		}
	}

	/**
	 * Safely close the connection with the client
	 */
	public void close(String error_message) {
		try {
			//TODO
			//asyncSendToClient(error_message);
			this.server.removeNickname(this.nickname);
			this.client.close();
		} catch (IOException e) {
			System.out.println("Connection closed");
		}
	}

	public void run() {
		while (true) {
			// TODO: better exception handling
			try {
				Message message = (Message) in.readObject();
				this.controller.handleMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
