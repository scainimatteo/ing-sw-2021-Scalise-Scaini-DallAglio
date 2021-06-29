package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.util.observer.InitializedGameObserver;

public interface InitializedGameObservable {
	public void notifyInitialied();
	public void addInitializedGameObserver(InitializedGameObserver observer);
}
