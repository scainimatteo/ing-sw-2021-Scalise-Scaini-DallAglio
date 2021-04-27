package it.polimi.ingsw.controller.util;

import java.util.ArrayList;
import java.util.Arrays;

import java.lang.IndexOutOfBoundsException;

import java.io.Serializable;

public class ArrayChooser implements Serializable {
	private static final long serialVersionUID = 2L;
	private String message;
	private ArrayList<Object> array;
	private ArrayList<Object> chosen_array;
	private int to_choose;
	private int index = 0;

	public ArrayChooser(String message, Object[] array, int to_choose) {
		this.message = message;
		this.array = new ArrayList<Object>(Arrays.asList(array));
		this.to_choose = to_choose;
		this.chosen_array = new ArrayList<Object>(to_choose);
	}

	public String getMessage() {
		return this.message;
	}

	public Object[] getArray() {
		return this.array.toArray(new Object[this.array.size()]);
	}

	public Object[] getChosenArray() {
		return this.chosen_array.toArray(new Object[this.to_choose]);
	}

	/**
	 * Move an element from the array to the chosen_array
	 *
	 * @param index_chosen the index of the element to move
	 * @return if the move was successfull
	 */
	public boolean setChosen(int index_chosen) {
		try {
			this.chosen_array.add(this.array.get(index_chosen - 1));
			this.array.remove(index_chosen - 1);
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 * @return true if the chosen_array is full
	 */
	public boolean isComplete() {
		return this.to_choose == this.chosen_array.size();
	}
}
