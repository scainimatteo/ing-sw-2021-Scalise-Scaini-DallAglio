package it.polimi.ingsw.server.persistence;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class PersistenceUtil {
	/**
	 * Get the directory for the persistence files
	 *
	 * @return a Path object representing the directory
	 *
	 * Source: <https://stackoverflow.com/questions/35388882/find-place-for-dedicated-application-folder>
	 */
	public static Path getPersistenceDirectory() {
		String os = System.getProperty("os.name");
		String home = System.getProperty("user.home");

		if (os.contains("Mac")) {
			return Paths.get(home, "Library", "Application Support");
		} else if (os.contains("Windows")) {
			String version = System.getProperty("os.version");
			if (version.startsWith("5.")) {
				return getFromEnv("APPDATA", false, Paths.get(home, "Application Data"));
			} else {
				return getFromEnv("APPDATA", false, Paths.get(home, "AppData", "Roaming"));
			}
		} else {
			return getFromEnv("XDG_DATA_HOME", true, Paths.get(home, ".local", "share", "GC02"));
		}
	}

	/**
	* Retrieves a path from an environment variable, substituting a default
	* if the value is absent or invalid.
	*
	* @param envVar name of environment variable to read
	* @param mustBeAbsolute whether enviroment variable's value should be considered invalid if it's not an absolute path
	* @param defaultPath default to use if environment variable is absent or invalid
	* @return environment variable's value as a Path, or de defaultPath
	*
	 * Source: <https://stackoverflow.com/questions/35388882/find-place-for-dedicated-application-folder>
	*/
	private static Path getFromEnv(String envVar, boolean mustBeAbsolute, Path defaultPath) {
		Path dir;
		String envDir = System.getenv(envVar);
		if (envDir == null || envDir.isEmpty()) {
			dir = defaultPath;
		} else {
			dir = Paths.get(envDir);
			if (mustBeAbsolute && !dir.isAbsolute()) {
				dir = defaultPath;
			}
		}
		return dir;
	}

	/**
	 * Checks if a match with the name match_name is saved in memory
	 *
	 * @param match_name the name of the match to check
	 * @return true if the match is saved in the appropriate directory
	 */
	public static boolean checkPersistence(String match_name) {
		try {
			final String match_name_json = match_name + ".json";
			int files = (int) Files.list(getPersistenceDirectory()).filter(path -> path.getFileName().toString().equals(match_name_json)).count();
			return files > 0;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * @param match_name the identifier of the match to save
	 * @return the filename of the appropriate json file to save the match
	 */
	public static String getPersistenceFileFromMatchName(String match_name) {
		Path persistence_directory = PersistenceUtil.getPersistenceDirectory();
		return Paths.get(persistence_directory.toString(), match_name).toString() + ".json";
	}

}
