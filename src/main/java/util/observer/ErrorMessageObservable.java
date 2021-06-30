package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.controller.servermessage.ErrorMessage;

import it.polimi.ingsw.util.observer.ErrorMessageObserver;

public interface ErrorMessageObservable {
	public void notifyReceivedErrorMessage(ErrorMessage error_message);
	public void addErrorMessageObserver(ErrorMessageObserver observer);
}
