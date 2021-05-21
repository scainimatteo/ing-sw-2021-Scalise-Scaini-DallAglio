package it.polimi.ingsw.util;

import java.util.ArrayList;

import it.polimi.ingsw.controller.servermessage.ServerMessage;

public class Observable {

    private ArrayList<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer){
        observers.add(observer);
    }

    public void notify(ServerMessage message){
        for(Observer observer: observers){
            observer.update(message);
        }
    }

}
