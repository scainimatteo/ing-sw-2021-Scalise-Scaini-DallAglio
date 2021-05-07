package it.polimi.ingsw.server;

import java.util.concurrent.ArrayBlockingQueue;

import java.io.*;

import java.net.*;

import it.polimi.ingsw.server.Server;

public class ClientHandler implements Runnable {
	private Server server;
	private Socket client;
	private ObjectInputStream din;
	private ObjectOutputStream dout;
	// queue that keeps the messages coming from the client
	private ArrayBlockingQueue<Object> messages;
	private String nickname;

	public ClientHandler(Server server, Socket client) throws IOException {
		this.server = server;
		this.client = client;
		this.dout = new ObjectOutputStream(client.getOutputStream()); 
		this.din = new ObjectInputStream(client.getInputStream()); 
		this.messages = new ArrayBlockingQueue<Object>(1);
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @param message the object to send to the client
	 */
	private synchronized void sendToClient(Object message) {
		try {
			dout.reset();
			dout.writeObject(message);
			dout.flush();
		} catch (IOException e) {
			// TODO: better exception handling
			e.printStackTrace();
		}
	}

	/**
	 * @param message the object to send to the client
	 */
	public void asyncSendToClient(Object message) {
		new Thread(() -> sendToClient(message)).start();
	}

	/**
	 * @return the object received from the client
	 */
	private synchronized Object receiveFromClient() {
		Object message = null;
		// TODO: better exception handling
		try {
			message = din.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * @return the object received from the client
	 */
	public Object asyncReceiveFromClient() throws InterruptedException {
		new Thread(() -> {
			try {
				// put the message in the queue
				messages.put(receiveFromClient());
			} catch (InterruptedException e) {
				//TODO: better exception handling
				e.printStackTrace();
			}
		}).start();

		// take the message from the queue
		return this.messages.take();
	}

	/**
	 * Safely close the connection with the client
	 */
	public void close(Object message) {
		try {
			sendToClient(message);
			this.server.removeNickname(this.nickname);
			this.client.close();
		} catch (IOException e) {
			System.out.println("Connection closed");
		}
	}

	// TODO: Not sure what to do with this method
	public void run() {
		return;
	}
}
