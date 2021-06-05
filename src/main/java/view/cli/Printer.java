package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.simplemodel.SimpleWarehouse;
import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

import it.polimi.ingsw.model.game.Turn;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.ProductionAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.player.track.Cell;
import it.polimi.ingsw.model.player.track.Tile;

import it.polimi.ingsw.util.ANSI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;

public class Printer {
	/**
	 * Print the text of an array of DevelopmentCards on one line
	 *
	 * @param cards_array the array of DevelopmentCard to combine
	 * @return the String with all the DevelopmentCard text
	 */
	public static String printDevelopmentCardsArray(DevelopmentCard[] cards_array) {
		String total = "";
		int dim = printDevelopmentCard(cards_array[0]).split("\n").length;
		String[] strings = new String[dim];

		for (int i = 0; i < dim; i++) {
			strings[i] = "";
		}

		for (int i = 0; i < cards_array.length; i++) {
			String text = printDevelopmentCard(cards_array[i]);
			String[] texts = text.split("\n");
			for (int j = 0; j < dim; j++) {
				strings[j] += texts[j];
				// if it's the last add a \n, otherwise add spaces
				if (i != cards_array.length - 1) {
					strings[j] += "   ";
				} else {
					strings[j] += "\n";
				}
			}
		}
		
		for (String s: strings) {
			total += s;
		}

		return total;
	}

	/**
	 * Print the text of an array of LeaderCards on one line
	 *
	 * @param cards_array the array of LeaderCard to combine
	 * @return the String with all the LeaderCard text
	 */
	public static String printLeaderCardArray(LeaderCard[] cards_array, boolean print_deactivated) {
		String total = "";
		int dim = printLeader(cards_array[0], print_deactivated).split("\n").length;
		String[] strings = new String[dim];

		for (int i = 0; i < dim; i++) {
			strings[i] = "";
		}

		for (int i = 0; i < cards_array.length; i++) {
			String text = printLeader(cards_array[i], print_deactivated);
			String[] texts = text.split("\n");
			for (int j = 0; j < dim; j++) {
				strings[j] += texts[j];
				// if it's the last add a \n, otherwise add spaces
				if (i != cards_array.length - 1) {
					strings[j] += "   ";
				} else {
					strings[j] += "\n";
				}
			}
		}
		
		for (String s: strings) {
			total += s;
		}

		return total;
	}

	/**
	 * Print the text of a matrix of DevelopmentCards as a matrix
	 *
	 * @param viewables the array of DevelopmentCard to combine
	 * @return the String with all the DevelopmentCard text
	 */
	public static String printDevelopmentCardsMatrix(DevelopmentCard[][] cards_matrix) {
		String total = "";

		for (int i = 0; i < cards_matrix.length; i++) {
			total += Printer.printDevelopmentCardsArray(cards_matrix[i]);
			// if it's the last of aline add a \n\n
			if (i != cards_matrix.length - 1) {
				total += "\n\n";
			}
		}

		return total;
	}

	public static String printResource(Resource res) {
		if (res == null) {
			return "  ";
		} else {
			return res.getAbbreviation();
		}
	}

	public static String printMarket(SimpleGame game) {
		Resource null_marble = Resource.FAITH;
		String string = "\n·--------------------·--\n|                     ";

		if (game.getFreeMarble() == null) {
			string += null_marble.printNullMarble();
		} else {
			string += game.getFreeMarble().printMarble();
		}

		string += " )\n|  ·--^---^---^---^--·--\n";

		for (Resource[] array : game.getMarket()){
			string += "|  |                 |\n|  <";
			for (Resource x : array) {
				if (x == null){
					string += "  " + null_marble.printNullMarble() + " ";
				} else {
					string += "  " + x.printMarble() + " ";
				}
			}
			string += " |\n";
		}

		string += "|  |                 |\n·--·-----------------·\n";

		return string;
	}

	public static String printDevelopmentCard(DevelopmentCard card){
		String top = "|-----------------|\n";
		String mid = "|                 |\n";

		if (card == null){
			return printNullCard();
		} 

		CardLevel level = card.getCardLevel();
		ArrayList<Resource> cost = card.getCost();
		Production production = card.getProduction();
		int pv = card.getPoints();

		/**
		 * level
		 */
		String spaces = "                 ";
		String tmp = "|  " + String.valueOf(level.getLevel()) + "  " + level.getColor().toString();
		String level_string = tmp + spaces.substring(tmp.length());
		level_string += " |\n";

		/**
		 * cost
		 */
		HashMap<Resource, Integer> cost_to_print = numFromArraylist(cost);
		Set<Resource> cost_set = cost_to_print.keySet();
		ArrayList<Resource> cost_array = new ArrayList<Resource>(Arrays.asList(cost_set.toArray(new Resource[cost_set.size()])));

		String cost_string = "| " + String.valueOf(cost_to_print.get(cost_array.get(0))) + " " + cost_array.get(0).getAbbreviation();
		if (cost_array.size() >= 2){
			cost_string += " " + String.valueOf(cost_to_print.get(cost_array.get(1))) + " " + cost_array.get(1).getAbbreviation();
			if (cost_array.size() >= 3){
				cost_string += " " + String.valueOf(cost_to_print.get(cost_array.get(2))) + " " + cost_array.get(2).getAbbreviation();
			} else {
				cost_string += "     ";
			}
		} else {
			cost_string += "          ";
		}
		cost_string += "  |\n";

		/**
		 * pv
		 */
		String pv_string = "";
		if (pv < 10){
			pv_string = "|-------(" + String.valueOf(pv) + ")-------|\n";
		} else {
			String vp = String.valueOf(pv);
			pv_string = "|------(" + vp.substring(0,1) + " " + vp.substring(1) + ")------|\n";
		}

		/**
		 * production
		 */
		String[] production_string = production.productionToText();
		String prod1 = "| " + production_string[0] + "  |\n";
		String prod2 = "| " + production_string[1] + "  |\n";
		String prod3 = "| " + production_string[2] + "  |\n";
		String prod4 = "| " + production_string[3] + "  |\n";
		String prod5 = "| " + production_string[4] + "  |\n";

		String to_return = top + level_string + top + mid + cost_string + mid + pv_string + mid + prod1 + prod2 + prod3 + prod4 + prod5 + mid + top;

		return to_return;
	}

	public static String printDevelopmentCardsOnTable(SimpleGame game) {
		return printDevelopmentCardsMatrix(game.getDevelopmentCardsOnTable());
	}

	public static String printTrack(SimplePlayer player) {
		Cell[] track = player.getFaithTrack();
		Cell marker = player.getMarker();
		Tile[] vatican_reports = player.getReports();
		int last_position = 20;
		boolean flag = false;
		String top = "";
		String mid = "";
		String bot = "";

		for (int i = 0; i <= last_position; i++){
			top = top + "|---";
		}
		top = top + "|\n";

		for (int i = 0; i <= last_position; i ++){
			mid = mid + "| ";

			if (marker.getPosition() == i){
				mid = mid + ANSI.blue("M");
			} else if (track[i].isPopeSpace()){
				mid = mid + ANSI.red("P");
			} else {
				mid = mid + " ";
			}

			mid = mid + " ";
		}
		mid = mid + "|\n";

		for (int i = 0; i <= last_position; i ++){
			if ( !flag ){
				if (track[i].whichVaticanReport() == null){
					bot = bot + "    ";
					flag = false;
				} else {
					bot = bot + "=====";
					flag = true;
				}
			} else {
				if (track[i].whichVaticanReport() == null){
					bot = bot + "   ";
					flag = false;
				} else {
					bot = bot + "====";
					flag = true;
				}
			}
		}
		bot = bot + "\n";

		String top_v_r = "|---|   |---|   |---|\n";
		String mid_v_r = "|";

		for (int i = 0; i < 3; i ++){
			if (vatican_reports[i].isActive()){
				mid_v_r += " " + String.valueOf(vatican_reports[i].getVictoryPoints()) + " ";
			} else {
				mid_v_r += " X ";
			}

			if (i != 2){
				mid_v_r += "|   |";
			} else {
				mid_v_r += "|\n";
			}
		}
		
		return top + mid + top + "\n\n" + ANSI.red(bot) + "\n\n" + top_v_r + mid_v_r + top_v_r;
	}

	public static String printWarehouse(SimplePlayer player) {
		SimpleWarehouse warehouse = player.getWarehouse();
		ArrayList<Resource> top = warehouse.getTopResource();
		ArrayList<Resource> mid = warehouse.getMiddleResources();
		ArrayList<Resource> bot = warehouse.getBottomResources();

		String total = "        /\\\n       /  \\\n      / ";

		if (top.size() == 1) {
			total += printResource(top.get(0)) + " \\\n     /------\\\n";
		} else {
			total += printResource(null) + " \\\n     /------\\\n";
		}

		if (mid.size() >= 1) {
			total += "    / " + printResource(mid.get(0)) + "  ";
		} else {
			total += "    / " + printResource(null) + "  ";
		}

		if (mid.size() == 2) {
			total += printResource(mid.get(1)) + " \\\n   /----------\\\n";
		} else {
			total += printResource(null) + " \\\n   /----------\\\n";
		}

		if (bot.size() >= 1) {
			total += "  / " + printResource(bot.get(0)) + "  ";
		} else {
			total += "  / " + printResource(null) + "  ";
		}

		if (bot.size() >= 2) {
			total += printResource(bot.get(1)) + "  ";
		} else {
			total += printResource(null) + "  ";
		}

		if (bot.size() == 3) {
			total += printResource(bot.get(2)) + " \\\n";
		} else {
			total += printResource(null) + " \\\n";
		}

		return total + " /--------------\\";
	}

	public static String printStrongbox(SimplePlayer player) {
		HashMap<Resource, Integer> storage = player.getStrongbox();
		String string = "·--------------·\n|              |\n";

		for (Resource r: storage.keySet()) {
			if (storage.get(r) < 10) {
				string += "|    ";
			} else {
				string += "|   ";
			}

			string += String.valueOf(storage.get(r)) + " x " + r.getAbbreviation() + "    |\n";
		}

		string += "|              |\n|--------------|\n";

		return string + "|              |\n|--------------|\n";
	}
	
	public static String printLeader(LeaderCard card, boolean print_deactivated){
		if (card == null){
			return printNullCard();
		} 

		if (card.isActive() || print_deactivated ){
			return card.printText();
		} else {
			return printNullCard();
		}
	}

	public static String printLeaderCards(SimplePlayer player, boolean print_deactivated) {
		ArrayList<LeaderCard> deck = player.getLeaderCards();
		LeaderCard[] leader_cards = deck.toArray(new LeaderCard[deck.size()]);
		
		return printLeaderCardArray(leader_cards, print_deactivated);
	}

	public static String printDevelopmentCardsSlots(SimplePlayer player) {
		return printDevelopmentCardsArray(player.getTopCards().toArray(new DevelopmentCard[3]));
	}

	public static String printDevelopmentCardsSlots(SimplePlayer player, int slot) {
		switch (slot){
			case 1: return printDevelopmentCardsArray(player.getFirstColumn().toArray(new DevelopmentCard[3]));
			case 2: return printDevelopmentCardsArray(player.getSecondColumn().toArray(new DevelopmentCard[3]));
			case 3: return printDevelopmentCardsArray(player.getThirdColumn().toArray(new DevelopmentCard[3]));
			default: return "";
		}
	}

	public static String printTurn(Turn turn){
		String to_return;
		String top = "|-----------------------------------------------------|\n";
		String mid = "|                                                     |\n";
		String pad = "                                                      ";

		String tmp_nickname = "| ACTIVE PLAYER: " + turn.getNickname() + " ";
		String nickname_string = tmp_nickname + pad.substring(tmp_nickname.length()) + "|\n";

		String tmp_req_resources = "| REQUIRED RESOURCES: ";
		String req_pad = pad;
		ArrayList<Resource> req_resources = turn.getRequiredResources();
		for (int i = 0; i < req_resources.size(); i ++){
			tmp_req_resources += req_resources.get(i) + " ";
		}
		String req_string = tmp_req_resources + req_pad.substring(tmp_req_resources.length()) + "|\n";

		String tmp_prod_resources = "| PRODUCED RESOURCES: ";
		String prod_pad = pad;
		ArrayList<Resource> prod_resources = turn.getProducedResources();
		for (int i = 0; i < prod_resources.size(); i ++){
			tmp_prod_resources += prod_resources.get(i) + " ";
		}
		String prod_string = tmp_prod_resources + prod_pad.substring(tmp_prod_resources.length()) + "|\n";

		to_return = top + mid + nickname_string + mid + top + mid + req_string + mid + top + mid + prod_string + mid + top;

		return to_return;
	}

	public static String printNullCard(){
		String top = "|-----------------|\n";
		String mid = "|                 |\n";

		String to_return = top + mid + mid + mid + mid + mid + mid + mid + mid + mid + mid + mid + mid + mid + top;

		return to_return;
	}

	private static HashMap<Resource, Integer> numFromArraylist(ArrayList<Resource> arraylist){
		HashMap<Resource, Integer> to_return = new HashMap<Resource, Integer>();

		for (Resource res : arraylist){
			if (to_return.containsKey(res)){
				to_return.put(res, to_return.get(res) + 1);
			} else {
				to_return.put(res, 1);
			}
		}

		return to_return;
	}
}
