package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public interface Observer {
    public void update(ServerMessage message);
}
