package it.polimi.ingsw.util;

public class ANSI {
	public static final String BASE = "\u001B[";
	public static final String RST = "\u001b[0m";

	//TEXT MODIFICATION
	public static final String BOLD = BASE + "1m";
	public static final String ITALIC = BASE + "3m";
	public static final String UNDERLINE = BASE + "4m";

	// FOREGROUND COLOR
	public static final String BLACK = BASE + "30m";
	public static final String RED = BASE + "31m";
	public static final String GREEN = BASE + "32m";
	public static final String YELLOW = BASE + "33m";
	public static final String BLUE = BASE + "34m";
	public static final String MAGENTA = BASE + "35m";
	public static final String CYAN = BASE + "36m";
	public static final String WHITE = BASE + "37m";

	// BACKGROUND COLOR
	public static final String BG_BLACK = BASE + "40m";
	public static final String BG_RED = BASE + "41m";
	public static final String BG_GREEN = BASE + "42m";
	public static final String BG_YELLOW = BASE + "43m";
	public static final String BG_BLUE = BASE + "44m";
	public static final String BG_MAGENTA = BASE + "45m";
	public static final String BG_CYAN = BASE + "46m";
	public static final String BG_WHITE = BASE + "47m";

	public synchronized static String bold(String s) {
		return BOLD + s + RST;
	}

	public synchronized static String italic(String s) {
		return ITALIC + s + RST;
	}

	public synchronized static String underline(String s) {
		return UNDERLINE + s + RST;
	}

	public synchronized static String black(String s) {
		return BLACK + s + RST;
	}

	public synchronized static String red(String s) {
		return RED + s + RST;
	}

	public synchronized static String green(String s) {
		return GREEN + s + RST;
	}

	public synchronized static String yellow(String s) {
		return YELLOW + s + RST;
	}

	public synchronized static String blue(String s) {
		return BLUE + s + RST;
	}

	public synchronized static String magenta(String s) {
		return MAGENTA + s + RST;
	}

	public synchronized static String cyan(String s) {
		return CYAN + s + RST;
	}

	public synchronized static String white(String s) {
		return WHITE + s + RST;
	}

	public synchronized static String blackBg(String s) {
		return BG_BLACK + s + RST;
	}

	public synchronized static String redBg(String s) {
		return BG_RED + s + RST;
	}

	public synchronized static String greenBg(String s) {
		return BG_GREEN + s + RST;
	}

	public synchronized static String yellowBg(String s) {
		return BG_YELLOW + s + RST;
	}

	public synchronized static String blueBg(String s) {
		return BG_BLUE + s + RST;
	}

	public synchronized static String magentaBg(String s) {
		return BG_MAGENTA + s + RST;
	}

	public synchronized static String cyanBg(String s) {
		return BG_CYAN + s + RST;
	}

	public synchronized static String whiteBg(String s) {
		return BG_WHITE + s + RST;
	}

	//CURSOR

	public synchronized static String cursorUp(int n) {
		return BASE + n + "A";
	}

	public synchronized static String cursorDown(int n) {
		return BASE + n + "B";
	}

	public synchronized static String cursorForward(int n) {
		return BASE + n + "C";
	}

	public synchronized static String cursorBack(int n) {
		return BASE + n + "D";
	}

	public synchronized static String cursorNextLine(int n) {
		return BASE + n + "E";
	}

	public synchronized static String cursorPreviousLine(int n) {
		return BASE + n + "F";
	}

	public synchronized static String cursorHorizontalAbsolute(int n) {
		return BASE + n + "G";
	}

	public synchronized static String cursorPosition(int n, int m) {
		return BASE + n + ";" + m + "H";
	}

	public synchronized static String cursorErase(int n) {
		return BASE + n + "J";
	}

	public synchronized static String scrollUp(int n) {
		return BASE + n + "S";
	}

	public synchronized static String scrollDown(int n) {
		return BASE + n + "T";
	}

	public synchronized static String saveCursor() {
		return BASE + "s";
	}

	public synchronized static String restoreCursor() {
		return BASE + "u";
	}
}
