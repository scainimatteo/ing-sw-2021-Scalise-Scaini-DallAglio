package it.polimi.ingsw.controller.servermessage;

import java.io.Serializable;

public abstract class ServerMessage implements Serializable {
	protected static final long serialVersionUID = 11331L;
	public boolean error = false;
	public boolean initializing = false;
}
