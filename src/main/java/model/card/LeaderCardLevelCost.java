package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.card.DevelopmentCardsColor;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.io.Serializable;

public class LeaderCardLevelCost extends LeaderCard implements Serializable {
	private static final long serialVersionUID = 34732L;
	private ArrayList<CardLevel> requirements;
	
	public LeaderCardLevelCost (int points, LeaderAbility ability, ArrayList<CardLevel> requirements, int id, String front_path) {
		this.victory_points = points;
		this.ability = ability;
		this.activated = false;
		this.requirements = requirements;
		this.id = id;
		this.front_path = front_path;
	}

	public ArrayList<CardLevel> getRequirements() {
		return this.requirements;
	}

	/**
	 *@return true if the player has bought the correct DevelopmentCards
	 */
	@Override
	public boolean isActivable(Player player){
		ArrayList<CardLevel> req_levels = (ArrayList<CardLevel>) this.requirements.clone();
		ArrayList<CardLevel> total_levels = new ArrayList<CardLevel>();
		Iterator<DevelopmentCard> iterator = player.getDevCardIterator();
		while (iterator.hasNext()){
			total_levels.add(iterator.next().getCardLevel());
		}

		for (CardLevel req_lvl : this.requirements){
			int required = (int) req_levels.stream().filter(e -> e.equals(req_lvl)).count();
			int available = (int) total_levels.stream().filter(e -> e.equals(req_lvl)).count();
			if (required > available){
				return false;
			}
		}
		return true;
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
					greenstring = card_level.toString();
					break;
				case BLUE:
					blue++;
					bluestring = card_level.toString();
					break;
				case YELLOW:
					yellow++;
					yellowstring = card_level.toString();
					break;
				case PURPLE:
					purple++;
					purplestring = card_level.toString();
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

