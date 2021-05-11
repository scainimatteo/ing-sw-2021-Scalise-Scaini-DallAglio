package it.polimi.ingsw.view;

import java.util.HashMap;

import it.polimi.ingsw.controller.util.ArrayChooser;
import it.polimi.ingsw.controller.util.TurnSelector;
import it.polimi.ingsw.controller.util.Message;
import it.polimi.ingsw.controller.util.Choice;

public interface View {
	public boolean isMessageParsed();
	public void setMessageToParse(Message message);

	// RECEIVE

	public void handleString(String s);
	public void handleArray(ArrayChooser array_chooser);
	public void handleChoice(Choice c);
	public void handleTurn(TurnSelector t);
	public void handleRank(HashMap<String, Integer> rank);


	// SEND

	public Message getInput(Message message);
}
