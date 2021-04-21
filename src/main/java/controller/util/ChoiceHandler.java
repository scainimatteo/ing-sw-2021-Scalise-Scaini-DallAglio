package it.polimi.ingsw.controller.util;

import java.util.Arrays;

public class ChoiceHandler {
	//TODO: inserirvi interfaccia/riferimento al collegamento di rete
		
	public Object pickBetween (Object[] selection){
		selection = Arrays.stream(selection).filter(x -> x != null).toArray();	
		Object choice = null; //only useful for compiling
		if (selection.length == 1){
			return selection[0];
		}
		else { 
			//interazione network
		}
		return choice;
	}

	public boolean pickFlow (Object a, Object b){
		//interazione network
		if (/*TODO*/ true) {
			return true;
		} 
		else {
			return false;
		}
	}
	
	public boolean pickFlow (String message){
		boolean response = true;
		//interazioneNetwork
		return response;
	}
}		
