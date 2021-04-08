package it.polimi.ingsw.model.card;

import java.util.AbstractCollection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator; 
import java.lang.IndexOutOfBoundsException;
import java.util.NoSuchElementException;

public class Deck<E> extends AbstractCollection <E> {
	private int max_size;
	private ArrayList <E> structure;

	public Deck(int size){
		this.max_size = size;
		this.structure = new ArrayList<E>();
	}


	/**
	* @return is the structure empty?
	*/
	public boolean isEmpty(){
		return structure.isEmpty();
	}


	/**
	* @return collection iterator
	*/
	public Iterator <E> iterator(){
		return structure.iterator();
	}
	
	/**
	* @return collection size 
	*/
	public int size (){
		return structure.size();
	}
	
	/**
	* @return maximum allowed collection size 
	*/
	public int get_max(){
		return this.max_size;
	}

	/**
	* @param elem element to be added to the collection
	* @return true if the element has been added? 
	* @throws IndexOutOfBoundsException if the deck already is at max size
	*/
	@Override
	public boolean add(E elem) throws IndexOutOfBoundsException {
		if (structure.size() < max_size){
			return structure.add(elem);
		}
		else {throw new IndexOutOfBoundsException();}
	}

	/**
	* @return the top element of the deck
	*/
	public E peekTopCard() {
		return structure.size() > 0 ? structure.get(structure.size() - 1) : null;
	}
	
	/**
	* Removes the top element of the deck and returns it
	*
	* @return the top element of the deck
	* @throws NoSuchElementException if the deck is empty
	*/
	public E draw() throws NoSuchElementException {
		E elem = peekTopCard();
		if (elem == null) {throw new NoSuchElementException();}
		else{		
			structure.remove(structure.size() -1);
			return elem;
		}
	}
	
	/**
	* Shuffles the elements of the deck
	*
	* @return the shuffled deck
	*/
	public void shuffle() {
		//allows for more equal probability of each possible output
		//due to the shuffle method never keeping the same order, less cycles would make the initial configuration either impossible
		//or to likely after the shuffling function
		for (int i = 0; i < 5; i++){
			Collections.shuffle (structure);
		}
	}				
}
