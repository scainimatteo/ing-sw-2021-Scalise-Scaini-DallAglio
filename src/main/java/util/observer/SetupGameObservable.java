package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.util.observer.SetupGameObserver;

public interface SetupGameObservable {
	public void notifySetupDone();
	public void addSetupGameObserver(SetupGameObserver observer);
}
