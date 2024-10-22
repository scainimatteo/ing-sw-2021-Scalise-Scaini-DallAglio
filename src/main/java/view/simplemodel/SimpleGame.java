package it.polimi.ingsw.view.simplemodel;

import java.util.ArrayList;

import it.polimi.ingsw.model.card.DevelopmentCard;

import it.polimi.ingsw.model.resources.Resource;

import java.io.Serializable;

public class SimpleGame implements Serializable {
	private static final long serialVersionUID = 99234L;
	protected ArrayList<String> order;
	protected Resource[][] market;
	protected Resource free_marble;
	protected DevelopmentCard[][] development_cards_on_table;

	public SimpleGame(ArrayList<String> order, Resource[][] market, Resource free_marble, DevelopmentCard[][] development_cards_on_table){
		this.order = order;
		this.market = market;
		this.free_marble = free_marble;
		this.development_cards_on_table = development_cards_on_table;
	}

	public ArrayList<String> getOrder() {
		return this.order;
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
