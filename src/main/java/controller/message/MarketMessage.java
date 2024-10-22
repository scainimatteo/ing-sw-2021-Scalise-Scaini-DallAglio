package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

import java.io.Serializable;

public class MarketMessage extends TurnMessage implements Serializable {
	private static final long serialVersionUID = 88668L;
	public boolean row_or_column;
	public ArrayList<Resource> white_marbles;

	public MarketMessage(boolean row_or_column, int index){
		this.row_or_column = row_or_column;
		if (row_or_column) {
			this.column = index;
		} else {
			this.row = index;
		}
		this.white_marbles = new ArrayList<Resource>();
	}

	public MarketMessage(boolean row_or_column, int index, ArrayList<Resource> white_marbles){
		this(row_or_column, index);
		this.white_marbles = white_marbles;
	}

	public void useMessage(Controller controller){
		controller.handleMarket(this.player, this.row, this.column, this.row_or_column, this.white_marbles);
	}
}
