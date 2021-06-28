package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import java.io.Serializable;

public class PersistenceMessage extends Message implements Serializable {
	private static final long serialVersionUID = 23899L;

	public void useMessage(Controller controller) {
		controller.handlePersistence(this.player);
	}
}
