package it.polimi.ingsw.view.simplemodel;

import it.polimi.ingsw.model.card.DevelopmentCard;

import java.io.Serializable;
import java.util.ArrayList;

public class SimpleDevelopmentCardSlot implements Serializable {
	private static final long serialVersionUID = 4664L;
	private ArrayList<DevelopmentCard> first_column;
	private ArrayList<DevelopmentCard> second_column;
	private ArrayList<DevelopmentCard> third_column;

	public SimpleDevelopmentCardSlot(ArrayList<DevelopmentCard> first_column, ArrayList<DevelopmentCard> second_column, ArrayList<DevelopmentCard> third_column){
		this.first_column = first_column;
		this.second_column = second_column;
		this.third_column = third_column;
	}

	public ArrayList<DevelopmentCard> getTopCards(){
		ArrayList<DevelopmentCard> top_cards = new ArrayList<DevelopmentCard>();
		top_cards.add(first_column.isEmpty() ? null : first_column.get(0));
		top_cards.add(second_column.isEmpty() ? null : second_column.get(0));
		top_cards.add(third_column.isEmpty() ? null : third_column.get(0));
		return top_cards;
	}

	public ArrayList<DevelopmentCard> getFirstColumn(){
		return this.first_column;
	}

	public ArrayList<DevelopmentCard> getSecondColumn(){
		return this.second_column;
	}

	public ArrayList<DevelopmentCard> getThirdColumn(){
		return this.third_column;
	}
}
