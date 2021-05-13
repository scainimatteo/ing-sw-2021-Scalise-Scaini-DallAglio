package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.util.ANSI;

public enum Resource {
	COIN ("co"),
	STONE ("st"),
	SERVANT ("se"),
	SHIELD ("sh"),
	FAITH ("fa");

	private String abbreviation;

	private Resource(String color){
		this.abbreviation = abbreviation;
	}

	public String getAbbreviation(){
		return this.abbreviation;
	}

	public String printMarble(){
		char circle = '\u25EF';
		switch (this){
			case COIN: return ANSI.yellow(Character.toString(circle));
			case STONE: return ANSI.green(Character.toString(circle));
			case SERVANT: return ANSI.magenta(Character.toString(circle));
			case SHIELD: return ANSI.cyan(Character.toString(circle));
			case FAITH: return ANSI.red(Character.toString(circle));
			default: return ANSI.white(Character.toString(circle));
		}
	}
}
