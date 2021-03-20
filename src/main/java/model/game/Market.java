package it.polimi.ingsw.model.game;

import java.util.ArrayList;
import java.util.Random;

import it.polimi.ingsw.model.resources.Resource;

/* Singleton */
public class Market {
	private static Market instance;
	private Resource[][] market_board;
	private Resource free_marble;
	private ArrayList<Resource> all_resources;

	private Market() {
		this.market_board = new Resource[3][4];
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

	private void shiftRow(int index) {
		return;
	}

	private void shiftColumn(int index) {
		return;
	}

	private void randomizeMarket() {
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				Resource res = all_resources.remove(random.nextInt(all_resources.size()));
				this.market_board[i][j] = res;
			}
		}
		this.free_marble = all_resources.get(0);
	}

	public static Market getIstance() {
		if (instance == null) {
			instance = new Market();
		}
		return instance;
	}

	public Resource[] getColumn(int index) {
		Resource[] column = {
			market_board[0][index],
			market_board[1][index],
			market_board[2][index]
		};
		return column;
	}

	public Resource[] getRow(int index) {
		return market_board[index];
	}
}
