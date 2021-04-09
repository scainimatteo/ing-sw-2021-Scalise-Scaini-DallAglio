package it.polimi.ingsw.controller.turn;

import it.polimi.ingsw.controller.turn.Turn;
import it.polimi.ingsw.model.game.Game;

import java.lang.NoSuchElementException;

public class MarketTurn extends Turn {
	private Market market

	public MarketTurn (Player player, Market market){
		this.player = player;
		this.market = market;
	}

	/**
	* @return the index of the column choosen by the player
	*/
	public int ChooseColumn(){
		int clientreturn;
		//TODO: insert client communication function
		return clientreturn;
	}
	
	/**
	* @return the index of the row choosen by the player
	*/
	public int ChooseRow(){
		int clientreturn;
		//TODO: insert client communication function
		return clientreturn;
	}
	
	/**
	* Checks for the presence of active WhiteMarblesAbility cards for the given player, shows each one by one to the player to ask for confirmation, returns the resource_type of the selected card.
	*
	* @return resource type of the selected card
	* @throws NoSuchElementException if no card is present/accepted by the player
	*/
	public Resource checkWhiteMarble() throws NoSuchElementException {
		for (LeaderCard card: player.getDeck()){
			if (card.isActive() && card.getType() == "WhiteMarble"){
				if (/*TODO: insent client communication function for confirmation of card selecton*/){
					return card.resource_type;
				}
			}
		}
		throw new NoSuchElementException();
	}
	
	/**
	* Gets the resources corresponding either to the row or the column the player selected
	* 
	* @return resource array of gained resources
	*/
	public Resource[] getFromMarket(){
		/*TODO: insert client communication function for choosing column or row*/
		if (/*chosen column*/)	{
			return market.getColumn(ChooseColumn());
		}
		else {
			return market.getRow(ChooseRow());
		}
	}
}
