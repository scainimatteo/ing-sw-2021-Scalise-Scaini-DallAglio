package it.polimi.ingsw.server;

import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
	private Server server;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;

	public ClientHandler(Server server, Socket client) throws IOException {
		this.server = server;
		this.client = client;
		this.dis = new DataInputStream(client.getInputStream()); 
		this.dos = new DataOutputStream(client.getOutputStream()); 
	}

	public void sendToClient(String message) {
		return;
	}

	public String receiveFromServer() {
		return null;
	}
}
