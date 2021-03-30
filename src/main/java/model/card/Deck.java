package it.polimi.ingsw.model.card;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator; 
import java.lang.IndexOutOfBoundsException;
import it.polimi.ingsw.model.card.Card;

public class Deck<E> extends AbstractCollection <E> {
	private int max_size;
	private ArrayList <E> structure;

	public Deck(int size){
		this.max_size = size;
		this.structure = new ArrayList<E>();
	}

	public boolean isEmpty(){
		return structure.isEmpty();
	}

	public Iterator <E> iterator(){
		return structure.iterator();
	}
	
	public int size (){
		return structure.size();
	}

	@Override
	public boolean add(E elem) {
		if (structure.size() <= max_size){
			return structure.add(elem);
		}
		else {throw new IndexOutOfBoundsException();}
	}

	public E peekTopCard() {
		return structure.size() > 0 ? structure.get(structure.size() - 1) : null;
	}
	
	public E draw() {
		E elem = peekTopCard();
		structure.remove(structure.size() -1);
		return elem;
	}
		
}
