package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import java.util.Iterator;

public class LeaderCardLevelCost extends LeaderCard {
	private CardLevel[] requirements;
	
	public LeaderCardLevelCost (int points, LeaderAbility ability, CardLevel[] requirements, int id) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
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

	protected String printTop() {
		int i = 2;
		String top = "|                 |\n";
		String padding = "                 ";
		String temp;
		int green = 0, blue = 0, yellow = 0, purple = 0;	
		String greenstring = "";
		String bluestring = "";
		String yellowstring = "";
		String purplestring = "";
		for (CardLevel x : requirements){
			if (x.getColor() == DevelopmentCardsColor.GREEN){
				green++;
				greenstring = x.toText();
			}else if(x.getColor() == DevelopmentCardsColor.BLUE){
				blue++;
				bluestring = x.toText();
			}else if(x.getColor() == DevelopmentCardsColor.YELLOW){
				yellow++;
				yellowstring = x.toText();
			}else {
				purple++;
				purplestring = x.toText();
			}
		}
		if (green >0){
			temp = " "  + String.valueOf(green) + " " + greenstring ; 
			top += "|" + temp + padding.substring(temp.length()) + "|\n|                 |\n";
			i--;	
		}
		if (blue >0){
			temp = " "  + String.valueOf(blue) + " " + bluestring ; 
			top += "|" + temp + padding.substring(temp.length()) + "|\n|                 |\n";
			i--;	
		}
		if (yellow >0){
			temp = " "  + String.valueOf(yellow) + " " + yellowstring ; 
			top += "|" + temp + padding.substring(temp.length()) + "|\n|                 |\n";
			i--;	
		}
		if (purple >0){
			temp = " "  + String.valueOf(purple) + " " + purplestring ; 
			top += "|" + temp + padding.substring(temp.length()) + "|\n|                 |\n";
			i--;	
		}
		if (i == 1){ 
			top += "|                 |\n|                 |\n";
		}
		return top + "|                 |\n";
	}
	
	public String printText(int index){
		return null;
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
