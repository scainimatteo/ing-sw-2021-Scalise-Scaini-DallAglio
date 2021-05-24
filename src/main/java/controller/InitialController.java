package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.message.Message;

public class InitialController implements Controller {
	String message = null;

	public void handleMessage(Message message) {
		message.useMessage(this);
	}

	/**
	 * Set the message and notify the threads in wait
	 *
	 * @param message the message to set
	 */
	public synchronized void setReceivedMessage(String message) {
		this.message = message;
		notify();
	}

	/**
	 * Receive a message from the client, wait if the message is null
	 *
	 * @return the message received from the client
	 */
	public synchronized String receiveMessage() throws InterruptedException {
		while (this.message == null) {
			wait();
		}
		String to_return = this.message;
		this.message = null;
		return to_return;
	}
}
