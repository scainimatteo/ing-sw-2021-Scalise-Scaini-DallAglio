package it.polimi.ingsw.controller.message;

import java.io.Serializable;

public abstract class TurnMessage extends Message implements Serializable {
	private static final long serialVersionUID = 443342L;
	public int row;
	public int column;
}
