package it.polimi.ingsw.util;

import java.util.ArrayList;

public class Observable<T> {

    private ArrayList<Observer<T>> observers = new ArrayList<>();

    public void addObservers(Observer<T> observer){
        observers.add(observer);
    }

    public void notify(T message){
        for(Observer<T> observer: observers){
            observer.update(message);
        }
    }

}
