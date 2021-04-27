package it.polimi.ingsw.client;

import java.util.Scanner;

import java.net.*;

import java.io.*;

import it.polimi.ingsw.controller.util.Choice;

public class Client {
	private int port;
	private String address;
	private Socket server;
	private ObjectInputStream din;
	private ObjectOutputStream dout;
	private Scanner stdin;
	private boolean choice = false;


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
					Object inputObject = this.din.readObject();
					handleObject(inputObject);
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
				Object to_send = parseSend(inputLine);
				write(to_send);
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

	/**
	 * Handle the Object received from the Server
	 *
	 * @param object the Object received
	 */
	private void handleObject(Object object) {
		if (object instanceof String) {
			// STRING
			handleString((String) object);
		} else if (object instanceof Object[]) {
			// ARRAY
			// if you can handle the single object, you can handle an array of them
			for (Object o: (Object[]) object) {
				handleObject(o);
			}
		} else if (object instanceof Choice) {
			// CHOICE
			handleChoice((Choice) object);
		} else {
			// FALLBACK
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
	 * Print the message of the Choice received and parse the next line as a Choice
	 *
	 * @param c the Choice received
	 */
	private synchronized void handleChoice(Choice c) {
		System.out.println(c.getMessage() + " [y/n] ");
		this.choice = true;
	}

	// TODO: This function is temporary and will be made better after the view
	/**
	 * Parse the string to send so that you can send objects
	 *
	 * @param input the string written on the console
	 * @return the object that the string represents
	 */
	private Object parseSend(String input) {
		try {
			// INTEGER
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			// ARRAYS
			if (input.contains(",")) {
				return input.split(",");
			// CHOICE
			} else if (this.choice) {
				Choice c = new Choice("");
				if (input.equals("y")) {
					c.setResponse(true);
				} else {
					c.setResponse(false);
				}
				this.choice = false;
				return c;
			}

			// FALLBACK
			return input;
		}
	}
}
