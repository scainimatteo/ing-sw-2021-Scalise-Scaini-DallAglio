package it.polimi.ingsw.model.resources;

import it.polimi.ingsw.util.ANSI;

public enum Resource {
	COIN ("co"),
	STONE ("st"),
	SERVANT ("se"),
	SHIELD ("sh"),
	FAITH ("fa");

	private String abbreviation;

	private Resource(String abbreviation){
		this.abbreviation = abbreviation;
	}

	public String getAbbreviation(){
		switch (this){
			case COIN: return ANSI.yellow(this.abbreviation);
			case STONE: return ANSI.grey(this.abbreviation);
			case SERVANT: return ANSI.magenta(this.abbreviation);
			case SHIELD: return ANSI.cyan(this.abbreviation);
			case FAITH: return ANSI.red(this.abbreviation);
			default: return ANSI.white(this.abbreviation);
		}
	}

	/**
	 * @return a String with a circle of the right color
	 */
	public String printMarble(){
		char circle = '\u25EF';
		switch (this){
			case COIN: return ANSI.yellow(Character.toString(circle));
			case STONE: return ANSI.grey(Character.toString(circle));
			case SERVANT: return ANSI.magenta(Character.toString(circle));
			case SHIELD: return ANSI.cyan(Character.toString(circle));
			case FAITH: return ANSI.red(Character.toString(circle));
			default: return ANSI.white(Character.toString(circle));
		}
	}

	/**
	 * @return a String with a white circle
	 */
	public String printNullMarble(){
		char circle = '\u25EF';
		return ANSI.white(Character.toString(circle));
	}

	/**
	 * @return a String with the path of the image of the marble of the right color
	 */
	public String getPath() {
		switch (this){
			case COIN: return "/images/tokens/resources/yellow_marble.png";
			case STONE: return "/images/tokens/resources/grey_marble.png";
			case SERVANT: return "/images/tokens/resources/purple_marble.png";
			case SHIELD: return "/images/tokens/resources/blue_marble.png";
			case FAITH: return "/images/tokens/resources/red_marble.png";
			default: return "/images/tokens/resources/white_marble.png";
		}
	}

	/**
	 * @return a String with the path of the image of the white marble
	 */
	public static String getNullMarblePath(){
		return "/images/tokens/resources/white_marble.png";
	}
}
