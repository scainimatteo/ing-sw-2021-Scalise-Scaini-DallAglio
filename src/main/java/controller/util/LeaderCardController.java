package it.polimi.ingsw.controller.util;

import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.card.LeaderCardResourcesCost;
import it.polimi.ingsw.model.card.LeaderCardLevelCost;
import it.polimi.ingsw.model.card.LeaderCard;

public class LeaderCardController {
	
	public LeaderCardController(){
	}

	/**
	 * @return an array of two boolean where the 1st position represent if the card has to be activated and the 2nd position represent has to be discarded
	 */
	private boolean[] askToPlayLeaderCard(){
		boolean[] to_return = {false, false};
		return to_return;
	}

	private void activateLeader(LeaderCard leader_card){
		leader_card.activateLeaderCard();
	}

	/**
	 * TODO: check with isActivable
	 * @param leader_cards is the array containing the two personal leader cards
	 * @return an array of two booleans where each position is true if the corresponding card is to be discarded
	 */
	public boolean[] playLeaderCard(Player player){
		boolean[] to_return = {false, false};
		boolean[] returned;
		LeaderCard[] leader_cards = player.getDeck();

		for (int i = 0; i < 2; i ++){
			if(leader_cards[i] != null){
				returned = askToPlayLeaderCard();

				if (returned[0] || returned[1]){
					if (returned[0]){
						if (player.isActivable(leader_cards[i])){
							activateLeader(leader_cards[i]);
						}
					} else if (returned[1]){
						to_return[i] = true;
					}
				} 
			}
		}

		return to_return;
	}
}
