package it.polimi.ingsw.server;

import java.util.concurrent.ArrayBlockingQueue;

import java.io.*;

import java.net.*;

import it.polimi.ingsw.controller.util.ViewController;
import it.polimi.ingsw.controller.util.ViewMessage;
import it.polimi.ingsw.controller.util.MessageType;
import it.polimi.ingsw.controller.util.Message;

import it.polimi.ingsw.server.Server;

public class ClientHandler implements Runnable {
	private Server server;
	private Socket client;
	private ObjectInputStream din;
	private ObjectOutputStream dout;
	// queue that keeps the messages coming from the client
	private ArrayBlockingQueue<Object> view_messages_received;
	private ArrayBlockingQueue<Object> messages_received;
	private ArrayBlockingQueue<Object> messages_to_send;
	private String nickname;
	private ViewController view_controller;

	public ClientHandler(Server server, Socket client) throws IOException {
		this.server = server;
		this.client = client;
		this.dout = new ObjectOutputStream(client.getOutputStream()); 
		this.din = new ObjectInputStream(client.getInputStream()); 
		this.view_messages_received = new ArrayBlockingQueue<Object>(10);
		this.messages_received = new ArrayBlockingQueue<Object>(10);
		this.messages_to_send = new ArrayBlockingQueue<Object>(10);
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setViewController(ViewController view_controller) {
		this.view_controller = view_controller;
	}

	/**
	 * Send messages from the messages_to_send queue
	 *
	 * @return Thread the thread used to send
	 */
	private Thread sendToClient() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					Object o = this.messages_to_send.take();
					dout.reset();
					dout.writeObject(o);
					dout.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	/**
	 * Put the messages in the messages_to_send queue
	 *
	 * @param message the message to send
	 */
	public void asyncSendToClient(Object message) {
		try {
			this.messages_to_send.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Put the messages received from the client in the messages_received queue
	 *
	 * @return Thread the thread used to receive
	 */
	private Thread receiveFromClient() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					// TODO: better exception handling
					try {
						Message message = (Message) din.readObject();
						if (message.getMessageType() == MessageType.VIEWREQUEST) {
							this.view_messages_received.put(message);
						} else {
							this.messages_received.put(message);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	/**
	 * @return the object received from the client
	 */
	public Object asyncReceiveFromClient() throws InterruptedException {
		// take the message from the messages_received queue
		return this.messages_received.take();
	}

	/**
	 * Take ViewRequests from the view_messages_received queue and handle them
	 *
	 * @return Thread the thread used to handle them
	 */
	private Thread receiveViewRequestFromClient() {
		Thread t = new Thread(() -> {
			try {
				while (true) {
					Message message = (Message) this.view_messages_received.take();
					this.view_controller.handleViewRequest((ViewMessage) message.getMessage());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
		return t;
	}

	/**
	 * Add a ViewReply to the messages_to_send queue
	 *
	 * @param message the ViewReply to send
	 */
	public void addViewReply(Message message) {
		try {
			this.messages_to_send.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Safely close the connection with the client
	 */
	public void close(Object message) {
		try {
			asyncSendToClient(message);
			this.server.removeNickname(this.nickname);
			this.client.close();
		} catch (IOException e) {
			System.out.println("Connection closed");
		}
	}

	public void run() {
		try{
			Thread t0 = sendToClient();
			Thread t1 = receiveFromClient();
			Thread t2 = receiveViewRequestFromClient();
			t0.join();
			t1.join();
			t2.join();
		} catch(InterruptedException e){
			System.out.println("Connection closed from the client side");
		}
	}
}
