package it.polimi.ingsw.util;

public interface Observer<T> {

    public void update(T message);

}
