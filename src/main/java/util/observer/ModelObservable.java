package it.polimi.ingsw.util.observer;

import java.util.ArrayList;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

import it.polimi.ingsw.util.observer.ModelObserver;

public class ModelObservable {
	private ArrayList<ModelObserver> observers = new ArrayList<ModelObserver>();

	public void addModelObserver(ModelObserver observer){
		observers.add(observer);
	}

	public void notifyModel(ServerMessage message){
		for(ModelObserver observer: observers){
			observer.updateModel(message);
		}
	}
}
