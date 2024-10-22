package it.polimi.ingsw.model.game;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

import it.polimi.ingsw.model.resources.Resource;

public class Market {
	public static final int dim_rows = 4;
	public static final int dim_cols = 3;
	private Resource[][] market_board;
	private Resource free_marble;
	private ArrayList<Resource> all_resources;

	public Market() {
		this.market_board = new Resource[dim_cols][dim_rows];
		this.all_resources = new ArrayList<Resource>();
		all_resources.add(null);
		all_resources.add(null);
		all_resources.add(null);
		all_resources.add(null);
		all_resources.add(Resource.SHIELD);
		all_resources.add(Resource.SHIELD);
		all_resources.add(Resource.STONE);
		all_resources.add(Resource.STONE);
		all_resources.add(Resource.COIN);
		all_resources.add(Resource.COIN);
		all_resources.add(Resource.SERVANT);
		all_resources.add(Resource.SERVANT);
		all_resources.add(Resource.FAITH);
		randomizeMarket();
	}

	/**
	 * Persistence only - recreate a Market from the match saved in memory
	 */
	public Market(Resource[][] market_board, Resource free_marble) {
		this.market_board = market_board;
		this.free_marble = free_marble;
	}

	/**
	 * Put the resources in radom order; the last one is the free marble
	 */
	private void randomizeMarket() {
		Random random = new Random();
		for (int i = 0; i < dim_cols; i++) {
			for (int j = 0; j < dim_rows; j++) {
				Resource res = all_resources.remove(random.nextInt(all_resources.size()));
				this.market_board[i][j] = res;
			}
		}
		this.free_marble = all_resources.get(0);
	}

	/**
	 * Getters
	 */
	public Resource[][] peekMarket() {
		return this.market_board;
	}

	public Resource getFreeMarble() {
		return this.free_marble;
	}

	/**
	 * Shift a row one to the left, the free_marble becomes the last element of the row
	 *
	 * @param index the index of the row to shift
	 */
	public void shiftRow(int index) {
		Resource[] new_row = new Resource[4];
		new_row[dim_rows - 1] = this.free_marble;
		this.free_marble = this.market_board[index][0];
		for (int i = 0; i < dim_rows - 1; i++) {
			new_row[i] = this.market_board[index][i + 1];
		}
		this.market_board[index] = new_row;
	}

	/**
	 * Shift a column one to the left, the free_marble becomes the last element of the column
	 *
	 * @param index the index of the column to shift
	 */
	public void shiftColumn(int index) {
		Resource old_free_marble = this.free_marble;
		this.free_marble = this.market_board[0][index];
		for (int i = 0; i < dim_cols - 1; i++) {
			this.market_board[i][index] = this.market_board[i + 1][index];
		}
		this.market_board[dim_cols - 1][index] = old_free_marble;
	}

	/**
	 * Get the resources stores in a row and shift it
	 *
	 * @param index the index of the row to shift
	 * @return an array of the resources taken by the player
	 */
	public ArrayList<Resource> getRow(int index) {
		try {
			ArrayList<Resource> row = new ArrayList<Resource>(Arrays.asList(market_board[index]));
			return row;
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Get the resources stores in a column and shift it
	 *
	 * @param index the index of the column to shift
	 * @return an array of the resources taken by the player
	 */
	public ArrayList<Resource> getColumn(int index) {
		ArrayList<Resource> column = new ArrayList<Resource>();
		try {
			for (int i = 0; i < dim_cols; i++) {
				column.add(market_board[i][index]);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}
		return column;
	}
}

/*
·--------------------·--
|                     O )
|  ·--^---^---^---^--·--
|  |                 |
|  <  O   O   O   O  |
|  |                 |
|  <  O   O   O   O  |
|  |                 |
|  <  O   O   O   O  |
|  |                 |
·--·-----------------·
*/
