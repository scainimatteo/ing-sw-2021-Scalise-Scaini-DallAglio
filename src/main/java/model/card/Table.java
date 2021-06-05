package it.polimi.ingsw.model.card;

import java.util.NoSuchElementException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator; 

import java.io.Serializable;

import java.lang.IndexOutOfBoundsException;

import it.polimi.ingsw.model.card.Deck;

public class Table<E> extends AbstractCollection<E> implements Serializable {
	private static final long serialVersionUID = 765L;
	private int max_size;
	private int row_size;
	private ArrayList<Deck<E>> structure;

	public Table(int rows, int columns){
		this.max_size = rows * columns;
		this.row_size = rows;
		this.structure = new ArrayList<Deck<E>>(rows * columns);
	}

	/**
	 * @return a TableIterator for the table
	 */
	public Iterator<E> iterator(){
		return new TableIterator(this);
	}

	/**
	 * @return the number of decks in the table
	 */
	public int size(){
		return structure.size();
	}

	public Deck<E> getDeck(int row, int column) {
		return structure.get(row_size * row + column);
	}
	
	/**
	 * @param deck the deck to add to the table
	 * @param row the index of the row where the deck will be put
	 * @param column the index of the column where the deck will be put
	 */
	public void addDeck(Deck<E> deck, int row, int column) {
		if (structure.size() < max_size){
			structure.add(row_size * row + column, deck);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * @param elem the elem to add to a deck of the table
	 * @param row the index of the row of the deck 
	 * @param column the index of the column of the deck
	 */
	public void addElement(E elem, int row, int column) {
		structure.get(row_size * row + column).add(elem);
	}

	/**
	 * Returns the element at the top of a deck without discarding it
	 *
	 * @param row the index of the row of the deck 
	 * @param column the index of the column of the deck
	 * @return the element at the top of the corrisponding deck
	 */
	public E peekTop(int row, int column) {
		return structure.get(row_size * row + column).peekTopCard();
	}
	
	/**
	 * Returns the element at the top of a deck while discarding it
	 *
	 * @param row the index of the row of the deck 
	 * @param column the index of the column of the deck
	 * @return the element at the top of the corrisponding deck
	 */
	public E draw(int row, int column) throws NoSuchElementException {
		return structure.get(row_size * row + column).draw();
	}

	/**
	 * Shuffle all the decks on the table
	 */
	public void shuffleAllDecks() {
		for (Deck<E> deck: structure) {
			deck.shuffle();
		}
	}

	/**
	 * Return the Deck as an ArrayList
	 *
	 * @param row the index of the row of the deck 
	 * @param column the index of the column of the deck
	 * @return the Deck as an ArrayList
	 */
	public ArrayList<E> getDeckAsArrayList(int row, int column) {
		return this.getDeck(row, column).toArrayList();
	}

	/**
	 * Iterator of the class
	 *
	 * Iterates between all the elements in the decks
	 */
	private class TableIterator implements Iterator<E> {
		ArrayList<E> elements_on_table;
		int index = 0;

		public TableIterator(Table<E> table) {
			this.elements_on_table = new ArrayList<E>();
			for (int i = 0; i < max_size; i++) {
				for (E element: structure.get(i)) {
					this.elements_on_table.add(element);
				}
			}
		}

		public boolean hasNext() {
			return index < elements_on_table.size();
		}

		public E next() {
			index++;
			return elements_on_table.get(index - 1);
		}
	}
}
