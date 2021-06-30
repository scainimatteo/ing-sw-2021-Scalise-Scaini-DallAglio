package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.util.observer.ViewUpdateObserver;

public interface ViewUpdateObservable {
	public void notifyViewUpdate();
	public void addViewUpdateObserver(ViewUpdateObserver observer);
	public void removeViewUpdateObserver(ViewUpdateObserver observer);
}
