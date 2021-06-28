package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public interface ModelObserver {
    public void updateModel(ServerMessage message);
}
