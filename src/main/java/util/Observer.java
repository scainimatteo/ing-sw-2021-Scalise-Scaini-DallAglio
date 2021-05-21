package it.polimi.ingsw.util;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public interface Observer {
    public void update(ServerMessage message);
}
