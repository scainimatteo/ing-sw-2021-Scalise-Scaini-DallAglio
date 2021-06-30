package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.controller.servermessage.ErrorMessage;

public interface ErrorMessageObserver {
    public void receivedErrorMessage(ErrorMessage error_message);
}
