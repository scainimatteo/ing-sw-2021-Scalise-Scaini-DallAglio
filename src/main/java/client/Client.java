package it.polimi.ingsw.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.util.ArrayChooser;
import it.polimi.ingsw.controller.util.TurnSelector;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;
import it.polimi.ingsw.controller.util.Choice;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.view.View;

public class Client {
	private int port;
	private String address;
	private Socket server;
	private ObjectInputStream din;
	private ObjectOutputStream dout;
	private Message message_to_parse;
	private View view;
	private boolean parsed = true;
	private ArrayBlockingQueue<Message> messages;

	public Client(String address, int port, View view) throws IOException {
		this.port = port;
		this.address = address;
		// connect to the server
		this.server = new Socket(address, port);
		this.dout = new ObjectOutputStream(server.getOutputStream()); 
		this.din = new ObjectInputStream(server.getInputStream()); 
		this.view = view;
		this.messages = new ArrayBlockingQueue<Message>(10);
	}

	public void run() throws IOException {
		System.out.printf("Client connected to server %s:%d\n", this.address, this.port);

		try{
			// indefinitely read to and write from the server
			Thread t0 = asyncReadFromSocket();
			Thread t1 = asyncWriteToSocket();
			Thread t2 = processData();
			t0.join();
			t1.join();
			t2.join();
		} catch(InterruptedException e){
			System.out.println("Connection closed from the client side");
		} finally {
			this.din.close();
			this.dout.close();
			this.server.close();
		}
	}

	/**
	 * Read from the server indefinitely, put the Messages read in a queue
	 *
	 * @return Thread the thread used to read
	 */
	public Thread asyncReadFromSocket() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					Message input_message = (Message) this.din.readObject();
					System.out.println(input_message.getMessageType());
					this.messages.put(input_message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	/**
	 * Write from the server indefinitely
	 *
	 * @return Thread the thread used to write
	 */
	public Thread asyncWriteToSocket(){
		Thread t = new Thread(() -> {
			while (true) {
				Message to_send = this.view.getInput(this.message_to_parse);
				if (this.view.isMessageParsed()) {
					write(to_send);
					this.message_to_parse = null;
				} else {
					// continue parsing the object we were parsing before
					handleMessage(this.message_to_parse);
				}
			}
		});
		t.start();
		return t;
	}

	/**
	 * Process the Messages in the queue
	 *
	 * @return Thread the thread used to process
	 */
	public Thread processData() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					// wait until there is at least a Message in the queue
					Message input_message = this.messages.take();
					handleMessage(input_message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	/**
	 * Write to the server
	 *
	 * @param to_send the Object to send
	 */
	private synchronized void write(Object to_send) {
		try {
			this.dout.reset();
			this.dout.writeObject(to_send);
			this.dout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * Handle the Message received from the Server
	 *
	 * @param message the Message received
	 */
	private void handleMessage(Message message) {
		this.message_to_parse = message;
		this.view.setMessageToParse(message);
		switch(message.getMessageType()) {
			case STRING:
				this.view.handleString((String) message.getMessage());
				break;
			case ARRAYCHOOSER:
				this.view.handleArray((ArrayChooser) message.getMessage());
				break;
			case CHOICE:
				this.view.handleChoice((Choice) message.getMessage());
				break;
			case TURNSELECTOR:
				this.view.handleTurn((TurnSelector) message.getMessage());
				break;
			case RANKING:
				this.view.handleRank((HashMap<String, Integer>) message.getMessage());
				break;
			default:
				System.out.println("Received unknown object");
				throw new IllegalArgumentException();
		}
	}
}
