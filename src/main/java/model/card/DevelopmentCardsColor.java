package it.polimi.ingsw.model.card;

import it.polimi.ingsw.util.ANSI;

public enum DevelopmentCardsColor {
	GREEN(0),
	BLUE(1),
	YELLOW(2),
	PURPLE(3);

	private int order;

	private DevelopmentCardsColor(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}
	
	public String colorString(String arg){
		if (this == DevelopmentCardsColor.GREEN){
			return ANSI.green(arg);
		}else if(this == DevelopmentCardsColor.BLUE){
			return ANSI.blue(arg);
		}else if(this == DevelopmentCardsColor.YELLOW){
			return ANSI.yellow(arg);
		}else {
			return ANSI.magenta(arg);
		}
	}
}
