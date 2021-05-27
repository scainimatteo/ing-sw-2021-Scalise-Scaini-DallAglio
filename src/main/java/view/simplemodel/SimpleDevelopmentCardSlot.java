package it.polimi.ingsw.view.simplemodel;

import it.polimi.ingsw.model.card.DevelopmentCard;

public class SimpleDevelopmentCardSlot {
	private DevelopmentCard[] first_column;
	private DevelopmentCard[] second_column;
	private DevelopmentCard[] third_column;

	public SimpleDevelopmentCardSlot(DevelopmentCard[] first_column, DevelopmentCard[] second_column, DevelopmentCard[] third_column){
		this.first_column = first_column;
		this.second_column = second_column;
		this.third_column = third_column;
	}

	public DevelopmentCard[] getTopCards(){
		DevelopmentCard[] to_return = {first_column[0], second_column[0], third_column[0]};
		return to_return;
	}

	public DevelopmentCard[] getFirstColumn(){
		return this.first_column;
	}

	public DevelopmentCard[] getSecondColumn(){
		return this.second_column;
	}

	public DevelopmentCard[] getThirdColumn(){
		return this.third_column;
	}
}
