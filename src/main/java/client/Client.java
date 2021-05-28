package it.polimi.ingsw.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.servermessage.InitializingServerMessage;
import it.polimi.ingsw.controller.servermessage.ServerMessage;
import it.polimi.ingsw.controller.servermessage.ErrorMessage;
import it.polimi.ingsw.controller.servermessage.ViewUpdate;
import it.polimi.ingsw.controller.message.Message;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.view.View;

public class Client {
	private int port;
	private String address;
	private Socket server;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private View view;

	public Client(String address, int port, View view) throws IOException {
		this.port = port;
		this.address = address;
		this.view = view;
		this.server = new Socket(address, port);
		this.out = new ObjectOutputStream(server.getOutputStream()); 
		this.in = new ObjectInputStream(server.getInputStream()); 
	}

	/**
	 * Start the View thread and start receiving and sending messages
	 */
	public void startClient() throws IOException {
		System.out.printf("Client connected to server %s:%d\n", this.address, this.port);

		try {
			this.view.startView(this);
			while (true) {
				ServerMessage message = (ServerMessage) this.in.readObject();
				if (message.error) {
					this.view.handleError((ErrorMessage) message);
				} else if (message.initializing) {
					this.view.handleInitializing((InitializingServerMessage) message);
				} else {
					this.updateView((ViewUpdate) message);
				}
			}
		} catch(ClassNotFoundException e) {
			System.out.println("Connection closed from the client side");
		} finally {
			this.in.close();
			this.out.close();
			this.server.close();
		}
	}

	/**
	 * Update the View
	 *
	 * @param message the ViewUpdate that contains the information to update
	 */
	public void updateView(ViewUpdate message) {
		this.view.updateView(message);
	}

	/**
	 * Send a Message to the Server
	 *
	 * @param message the Message to send
	 */
	public synchronized void sendMessage(Message message) {
		try {
			this.out.reset();
			this.out.writeObject(message);
			this.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
