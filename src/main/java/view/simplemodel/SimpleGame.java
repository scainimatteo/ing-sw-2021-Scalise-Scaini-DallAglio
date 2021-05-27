package it.polimi.ingsw.view.simplemodel;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.io.Serializable;

public class SimpleGame implements Serializable{
	private static final long serialVersionUID = 99234L;
	private Resource[][] market;
	private Resource free_marble;
	private DevelopmentCard[][] development_cards_on_table;

	public SimpleGame(Resource[][] market, Resource free_marble, DevelopmentCard[][] development_cards_on_table){
		this.market = market;
		this.free_marble = free_marble;
		this.development_cards_on_table = development_cards_on_table;
	}

	public Resource[][] getMarket(){
		return this.market;
	}

	public Resource getFreeMarble(){
		return this.free_marble;
	}

	public DevelopmentCard[][] getDevelopmentCardsOnTable(){
		return this.development_cards_on_table;
	}
}
