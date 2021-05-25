package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.simplemodel.SimplePlayer;
import it.polimi.ingsw.view.simplemodel.SimpleGame;

public class Printer {
	//TODO: rethink these methods
	/**
	 * Print the text of an array of Viewable on one line
	 *
	 * @param viewables the array of Viewable to combine
	 * @return the String with all the Viewables text
	 */
	public static String printArray(Object[] viewables) {
		//String total = "";
		//// we assume that all viewables have the same kind of string
		//int dim = viewables[0].printText().split("\n").length;
		//String[] strings = new String[dim];
		//for (int i = 0; i < dim; i++) {
		//	strings[i] = "";
		//}

		//for (int i = 0; i < viewables.length; i++) {
		//	String text = viewables[i].printText();
		//	String[] texts = text.split("\n");
		//	for (int j = 0; j < dim; j++) {
		//		strings[j] += texts[j];
		//		// if it's the last add a \n, otherwise add spaces
		//		if (i != viewables.length - 1) {
		//			strings[j] += "   ";
		//		} else {
		//			strings[j] += "\n";
		//		}
		//	}
		//}
		//
		//for (String s: strings) {
		//	total += s;
		//}
		//return total;
		return "";
	}

	/**
	 * Print the text of a matrix of Viewable as a matrix
	 *
	 * @param viewables the array of Viewable to combine
	 * @return the String with all the Viewables text
	 */
	public static String printMatrix(Object[][] viewables) {
		//String total = "";
		//for (int i = 0; i < viewables.length; i++) {
		//	total += Printer.printArray(viewables[i]);
		//	// if it's the last of aline add a \n\n
		//	if (i != viewables.length - 1) {
		//		total += "\n\n";
		//	}
		//}
		//return total;
		return "";
	}

	public static String printMarket(SimpleGame game) {
		return "";
	}

	public static String printDevelopmentCardsOnTable(SimpleGame game) {
		return "";
	}

	public static String printTrack(SimplePlayer player) {
		return "";
	}

	public static String printWarehouse(SimplePlayer player) {
		return "";
	}

	public static String printStrongbox(SimplePlayer player) {
		return "";
	}

	public static String printLeaderCards(SimplePlayer player) {
		return "";
	}

	public static String printDevelopmentCardsSlots(SimplePlayer player) {
		return "";
	}
}
