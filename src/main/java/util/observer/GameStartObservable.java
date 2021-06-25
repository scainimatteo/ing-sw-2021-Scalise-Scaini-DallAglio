package it.polimi.ingsw.util.observer;

import java.util.ArrayList;

import it.polimi.ingsw.util.observer.GameStartObserver;

public class GameStartObservable {
	private ArrayList<GameStartObserver> observers = new ArrayList<GameStartObserver>();

	public void addObserver(GameStartObserver observer){
		observers.add(observer);
	}

	public void notifyGameStarted(){
		for(GameStartObserver observer: observers){
			observer.gameStarted();
		}
	}
}
