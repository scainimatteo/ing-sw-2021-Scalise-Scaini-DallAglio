package it.polimi.ingsw.controller.message;

import it.polimi.ingsw.controller.Controller;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import java.util.ArrayList;

public class MarketMessage implements TurnMessage{
	public boolean row_or_column;
	public ArrayList<Resource> white_marbles;

	public MarketMessage(int row, int column, boolean row_or_column){
		this.row = row;
		this.column = column;
		this.row_or_column = row_or_column;
	}

	public MarketMessage(int row, int column, boolean row_or_column, ArrayList<Resuource> white_marbles){
		this.row = row;
		this.column = column;
		this.row_or_column = row_or_column;
		this.white_marbles = white_marbles;
	}

	public void useMessage(Controller controller){
		controller.handleMarket(this.player, this.row, this.column, this.row_or_column, this.white_marbles);
	}
}
