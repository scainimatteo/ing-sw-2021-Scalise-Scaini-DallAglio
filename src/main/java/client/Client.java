package it.polimi.ingsw.client;

import java.util.Scanner;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.util.ArrayChooser;
import it.polimi.ingsw.controller.util.TurnSelector;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;
import it.polimi.ingsw.controller.util.Choice;

public class Client {
	private int port;
	private String address;
	private Socket server;
	private ObjectInputStream din;
	private ObjectOutputStream dout;
	private Scanner stdin;
	private Message message_to_parse;


	public Client(String address, int port) throws IOException {
		this.port = port;
		this.address = address;
		// connect to the server
		this.server = new Socket(address, port);
		this.dout = new ObjectOutputStream(server.getOutputStream()); 
		this.din = new ObjectInputStream(server.getInputStream()); 
		this.stdin = new Scanner(System.in);
	}

	public void run() throws IOException {
		System.out.printf("Client connected to server %s:%d\n", this.address, this.port);

		try{
			// indefinitely read to and write from the server
			Thread t0 = asyncReadFromSocket();
			Thread t1 = asyncWriteToSocket();
			t0.join();
			t1.join();
		} catch(InterruptedException e){
			System.out.println("Connection closed from the client side");
		} finally {
			this.stdin.close();
			this.din.close();
			this.dout.close();
			this.server.close();
		}
	}

	/**
	 * Read from the server indefinitely
	 *
	 * @return Thread the thread used to read
	 */
	public Thread asyncReadFromSocket() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					Message inputMessage = (Message) this.din.readObject();
					handleMessage(inputMessage);
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
				// TODO: Change how the client gives the input
				String inputLine = this.stdin.nextLine();
				Message to_send = parseSend(inputLine);
				if (this.message_to_parse.isParsed()) {
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

	// TODO: These functions are temporary and will be made better in the view

	/**
	 * Handle the Message received from the Server
	 *
	 * @param message the Message received
	 */
	private void handleMessage(Message message) {
		this.message_to_parse = message;
		switch(message.getMessageType()) {
			case STRING:
				handleString((String) message.getMessage());
				message.setParsed();
				break;
			case ARRAYCHOOSER:
				handleArray((ArrayChooser) message.getMessage());
				break;
			case CHOICE:
				handleChoice((Choice) message.getMessage());
				break;
			case TURNSELECTOR:
				handleTurn((TurnSelector) message.getMessage());
				break;
			default:
				System.out.println("Received unknown object");
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Print the String received
	 *
	 * @param s the String received
	 */
	private void handleString(String s) {
		System.out.println(s);
	}

	/**
	 * Print the elements of the Array and parse the next line as an element of the Array
	 *
	 * @param array_chooser the ArrayChooser received
	 */
	private void handleArray(ArrayChooser array_chooser) {
		System.out.println(array_chooser.getMessage());
		Object[] array = array_chooser.getArray();
		for (int i = 1; i < array.length + 1; i++) {
			System.out.printf("%d. %s\n", i, array[i - 1]);
		}
		System.out.printf("Put the index of the element chosen: ");
	}

	/**
	 * Print the message of the Choice received and parse the next line as a Choice
	 *
	 * @param c the Choice received
	 */
	private void handleChoice(Choice c) {
		System.out.println(c.getMessage() + " [y/n] ");
	}

	/**
	 * Print the message of the TurnSelector received and parse the next line as a Turn
	 *
	 * @param t the TurnSelector received
	 */
	private void handleTurn(TurnSelector t) {
		System.out.println(t.getMessage());
		System.out.printf("Put the index of the turn chosen: ");
	}

	/**
	 * Parse the string to send so that you can send objects
	 *
	 * @param input the string written on the console
	 * @return the Message that the string represents
	 */
	private Message parseSend(String input) {
		Message message = null;

		switch(this.message_to_parse.getMessageType()) {
			case ARRAYCHOOSER:
				message = new Message(MessageType.ARRAYCHOOSER, sendArray(input));
				break;
			case CHOICE:
				message = new Message(MessageType.CHOICE, sendChoice(input));
				break;
			case TURNSELECTOR:
				message = new Message(MessageType.TURNSELECTOR, sendTurn(input));
				break;
		}

		if (message == null) {
			try {
				// INTEGER
				message = new Message(MessageType.INTEGER, Integer.parseInt(input));
			} catch (NumberFormatException e) {
				// FALLBACK
				message = new Message(MessageType.STRING, input);
			}
		}

		return message;
	}

	/**
	 * Parse the input as an index of the Array and send the ArrayChooser if it's complete
	 *
	 * @param input a String representing an index of the Array
	 * @return the complete ArrayChooser or null if it's not complete (the parsing will continue)
	 */
	private ArrayChooser sendArray(String input) {
		try {
			int chosen = Integer.parseInt(input);
			ArrayChooser array_chooser = (ArrayChooser) this.message_to_parse.getMessage();

			if (array_chooser.setChosen(chosen)) {
				if (array_chooser.isComplete()) {
					this.message_to_parse.setParsed();
					return array_chooser;
				}
			}
		} catch (NumberFormatException e) {
			//TODO: print an error
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse the input as a Choice and send the Choice
	 *
	 * @param input the String rapresenting the choice made
	 * @return the Choice representing the choice made
	 */
	private Choice sendChoice(String input){
		Choice c = new Choice("");
		if (input.equals("y")) {
			c.setResponse(true);
		} else {
			c.setResponse(false);
		}
		this.message_to_parse.setParsed();
		return c;
	}

	/**
	 * Parse the input as a TurnSelector and send the TurnSelector
	 *
	 * @param input the String rapresenting the turn chosen
	 * @return the TurnSelector representing the turn chosen
	 */
	private TurnSelector sendTurn(String input) {
		try {
			int chosen = Integer.parseInt(input);
			TurnSelector turn = (TurnSelector) this.message_to_parse.getMessage();

			if (turn.setChosen(chosen)) {
				this.message_to_parse.setParsed();
				return turn;
			}
		} catch (NumberFormatException e) {
			//TODO: print an error
			e.printStackTrace();
		}
		return null;
	}
}
