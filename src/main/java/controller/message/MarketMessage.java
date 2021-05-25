package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public class MarketMessage extends TurnMessage{
	public boolean row_or_column;
	public ArrayList<Resource> white_marbles;

	public MarketMessage(boolean row_or_column, int index){
		this.row_or_column = row_or_column;
		if (row_or_column) {
			this.column = index;
		} else {
			this.row = index;
		}
	}

	public MarketMessage(boolean row_or_column, int index, ArrayList<Resource> white_marbles){
		this.white_marbles = white_marbles;
	}

	public void useMessage(Controller controller){
		controller.handleMarket(this.player, this.row, this.column, this.row_or_column, this.white_marbles);
	}
}
