package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import java.util.Iterator;

import java.io.Serializable;

public class LeaderCardLevelCost extends LeaderCard implements Serializable {
	private static final long serialVersionUID = 34732L;
	private CardLevel[] requirements;
	
	public LeaderCardLevelCost (int points, LeaderAbility ability, CardLevel[] requirements, int id, String front_path) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
		this.front_path = front_path;
	}

	public CardLevel[] getRequirements() {
		CardLevel[] to_return = this.requirements.clone();
	return to_return;
	}

	public boolean isActivable(Player player){
		CardLevel[] req = this.getRequirements();
		Iterator<DevelopmentCard> iterator = player.getDevCardIterator();
		boolean to_return = true;
		CardLevel tmp;

		while (iterator.hasNext()){
			tmp = iterator.next().getCardLevel();

			for (int i = 0; i < req.length; i ++){
				if (req[i].equals(tmp)){
					req[i] = null;
					break;
				} 
			}
		}

		for (int j = 0; j < req.length; j ++){
			if (req[j] != null){
				to_return = false;
				break;
			} 
		}

		return to_return;
	}


	@Override
	protected String printTop() {
		int i = 2;
		String top = "|                 |\n";
		String padding = "                        ";
		String temp;
		int green = 0, blue = 0, yellow = 0, purple = 0;	
		String greenstring = "";
		String bluestring = "";
		String yellowstring = "";
		String purplestring = "";

		for (CardLevel card_level: requirements){
			switch (card_level.getColor()) {
				case GREEN:
					green++;
					greenstring = card_level.toText();
					break;
				case BLUE:
					blue++;
					bluestring = card_level.toText();
					break;
				case YELLOW:
					yellow++;
					yellowstring = card_level.toText();
					break;
				case PURPLE:
					purple++;
					purplestring = card_level.toText();
			}
		}

		if (green > 0){
			temp = " "  + String.valueOf(green) + " " + greenstring ; 
			top += "|" + temp + padding.substring(temp.length()) + "  |\n|                 |\n";
			i--;	
		}
		if (blue > 0){
			temp = " "  + String.valueOf(blue) + " " + bluestring ; 
			top += "|" + temp + padding.substring(temp.length()) + "  |\n|                 |\n";
			i--;	
		}
		if (yellow > 0){
			temp = " "  + String.valueOf(yellow) + " " + yellowstring ; 
			top += "|" + temp + padding.substring(temp.length()) + "  |\n|                 |\n";
			i--;	
		}
		if (purple > 0){
			temp = " "  + String.valueOf(purple) + " " + purplestring ; 
			top += "|" + temp + padding.substring(temp.length()) + "  |\n|                 |\n";
			i--;	
		}

		if (i == 1){ 
			top += "|                 |\n|                 |\n";
		}
		return top;
	}


}

/*
/-----------------\
|                 |
|                 |
|                 |
|                 |
|                 |
|-------(X)-------|
|                 |
|                 |
|                 |
|                 |
|                 |
|                 |
|                 |
\-----------------/
*/
