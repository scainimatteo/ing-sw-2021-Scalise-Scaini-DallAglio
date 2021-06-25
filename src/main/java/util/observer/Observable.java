package it.polimi.ingsw.util.observer;

import java.util.ArrayList;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.util.observer.Observer;

public class Observable {
	private ArrayList<Observer> observers = new ArrayList<Observer>();

	public void addObserver(Observer observer){
		observers.add(observer);
	}

	public void notify(ServerMessage message){
		for(Observer observer: observers){
			observer.update(message);
		}
	}
}
