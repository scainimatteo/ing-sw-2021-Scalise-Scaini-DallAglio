package it.polimi.ingsw.model.game;

import java.util.stream.Collectors;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;

import org.json.simple.parser.*;
import org.json.simple.*;

import it.polimi.ingsw.model.card.LeaderCardResourcesCost;
import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.LeaderCardLevelCost;
import it.polimi.ingsw.model.card.WhiteMarblesAbility;
import it.polimi.ingsw.model.card.ProductionAbility;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DiscountAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.track.Cell;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

/**
 * Singleton - Class that parses the json files and creates arrays containing
 * 			   all the DevelopmentCards, LeaderCards, Cells and Tiles
 */
public class Factory {
	private static Factory istance;
	private DevelopmentCard[] all_development_cards;
	private LeaderCard[] all_leader_cards;
	private Cell[] all_cells;
	private Tile[] all_tiles;
	private JSONParser jsonParser;


	//TODO: vanno bene queste costanti? Andrebbero messe da qualche altra parte?
	private final int development_cards_number = 48;
	private final int leader_cards_number = 16;
	private final int cells_number = 24;
	private final int tiles_number = 3;

	private Factory() throws ParseException, IOException {
		this.jsonParser = new JSONParser();
		this.all_development_cards = readAllDevelopmentCards();
		this.all_leader_cards = readAllLeaderCards();
		this.all_cells = readAllCells();
		this.all_tiles = readAllTiles();
	}

	/**
	 * @return the istance of Factory, creating it if it was not already created
	 */
	public static Factory getIstance() throws ParseException, IOException {
		if (istance == null) {
			istance = new Factory();
		}
		return istance;
	}

	/**
	 * @return an array containing all the DevelopmentCards specified in the json file
	 */
	public DevelopmentCard[] getAllDevelopmentCards() {
		return this.all_development_cards;
	}

	/**
	 * @return an array containing all the LeaderCards specified in the json file
	 */
	public LeaderCard[] getAllLeaderCards() {
		return this.all_leader_cards;
	}

	/**
	 * @return an array containing all the Cells specified in the json file
	 */
	public Cell[] getAllCells() {
		return this.all_cells;
	}

	/**
	 * @return an array containing all the Tiles specified in the json file
	 */
	public Tile[] getAllTiles() {
		return this.all_tiles;
	}

	@SuppressWarnings("unchecked")
	/**
	 * @param json_array the JSONArray to convert
	 * @return a Resource array with all the elements of json_array
	 */
	private Resource[] convertJsonArrayToResourceArray(JSONArray json_array) {
		// map all the Objects in json_array to Resources using toString() and valueOf()
		List<Resource> resource_list = (List<Resource>)json_array.stream().map(x -> Resource.valueOf(x.toString())).collect(Collectors.toList());
		return resource_list.toArray(new Resource[resource_list.size()]);
	}

	/**
	 * Parse the json file for the DevelopmentCards
	 *
	 * @return an array containing all the DevelopmentCards specified in the json file
	 */
	private DevelopmentCard[] readAllDevelopmentCards() throws ParseException, IOException {
		DevelopmentCard[] development_cards = new DevelopmentCard[development_cards_number];

		InputStream is = getClass().getClassLoader().getResourceAsStream("json/developmentcards.json");
		JSONObject jsonObject = (JSONObject) this.jsonParser.parse(new InputStreamReader(is));

		JSONArray development_cards_obj = (JSONArray) jsonObject.get("development_cards");
		for (int i = 0; i < development_cards_number; i++) {
			JSONObject card = (JSONObject) this.jsonParser.parse(development_cards_obj.get(i).toString());

			// ID
			int id = (int)(long) card.get("id");

			// COST
			Resource[] cost = convertJsonArrayToResourceArray((JSONArray) card.get("cost"));

			// PRODUCTION
			JSONObject production_obj = (JSONObject) card.get("production");
			Resource[] required_resources = convertJsonArrayToResourceArray((JSONArray) production_obj.get("required_resources"));
			Resource[] produced_resources = convertJsonArrayToResourceArray((JSONArray) production_obj.get("produced_resources"));
			Production new_production = new Production(required_resources, produced_resources);

			// CARDLEVEL
			JSONObject cardlevel_obj = (JSONObject) card.get("cardlevel");
			int level = (int)(long) cardlevel_obj.get("level");
			DevelopmentCardsColor color = DevelopmentCardsColor.valueOf(cardlevel_obj.get("color").toString());
			CardLevel cardlevel = new CardLevel(level, color);

			// VICTORYPOINTS
			int victory_points = (int)(long) card.get("victory_points");

			development_cards[i] = new DevelopmentCard(victory_points, new_production, cost, cardlevel, id);
		}
		return development_cards;
	}

	/**
	 * @param ability_obj the JSONObject containing all the ability parameters
	 * @return the corrisponding LeaderAbility
	 */
	private LeaderAbility createAbility(JSONObject ability_obj) throws ParseException {
		String ability_str = ability_obj.get("type").toString();
		switch (ability_str) {
			case "DISCOUNT":
				Resource discounted_resource = Resource.valueOf(ability_obj.get("resource").toString());
				return new DiscountAbility(discounted_resource);
			case "WHITE_MARBLES":
				Resource white_marble_resource = Resource.valueOf(ability_obj.get("resource").toString());
				return new WhiteMarblesAbility(white_marble_resource);
			case "EXTRA_SPACE":
				Resource extra_space_resource = Resource.valueOf(ability_obj.get("resource").toString());
				return new ExtraSpaceAbility(extra_space_resource);
			case "PRODUCTION":
				JSONArray required_resources_arr = (JSONArray) ability_obj.get("required_resources");
				Resource[] requirements_resources = convertJsonArrayToResourceArray(required_resources_arr);
				Resource[] produced_resources = {null, Resource.FAITH};
				return new ProductionAbility(requirements_resources, produced_resources);
			default:
				throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
		}
	}

	/**
	 * @param id the incremental id of the LeaderCard
	 * @param ability the ability of the LeaderCard
	 * @param victory_points the victory points of the LeaderCard
	 * @param requirements_obj the JSONObject containing all the requirements parameters
	 * @return the correct LeaderCard based on the requirements
	 */
	private LeaderCard createLeaderCard(int id, LeaderAbility ability, int victory_points, JSONObject requirements_obj) throws ParseException {
		String requirements_type = requirements_obj.get("type").toString();
		switch (requirements_type) {

			// to activate the LeaderCard a correct CardLevel is needed
			case "CARDLEVEL":
				JSONArray cardlevel_arr = (JSONArray) requirements_obj.get("cardlevel");
				CardLevel requirements_cardlevels[] = new CardLevel[cardlevel_arr.size()];
				for (int i = 0; i < cardlevel_arr.size(); i++) {
					JSONObject cardlevel_obj = (JSONObject) this.jsonParser.parse(cardlevel_arr.get(i).toString());
					int level = (int)(long) cardlevel_obj.get("level");
					DevelopmentCardsColor color = DevelopmentCardsColor.valueOf(cardlevel_obj.get("color").toString());
					requirements_cardlevels[i] = new CardLevel(level, color);
				}
				return new LeaderCardLevelCost(victory_points, ability, requirements_cardlevels, id);

			// to activate the LeaderCard the correct resources are needed
			case "RESOURCES":
				JSONArray resources_arr = (JSONArray) requirements_obj.get("resources");
				Resource[] requirements_resources = convertJsonArrayToResourceArray(resources_arr);
				return new LeaderCardResourcesCost(victory_points, ability, requirements_resources, id);

			default:
				throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
		}
	}

	/**
	 * Parse the json file for the LeaderCards
	 *
	 * @return an array containing all the LeaderCards specified in the json file
	 */
	private LeaderCard[] readAllLeaderCards() throws ParseException, IOException {
		LeaderCard[] leader_cards = new LeaderCard[leader_cards_number];

		InputStream is = getClass().getClassLoader().getResourceAsStream("json/leadercards.json");
		JSONObject jsonObject = (JSONObject) this.jsonParser.parse(new InputStreamReader(is));

		JSONArray leader_cards_obj = (JSONArray) jsonObject.get("leader_cards");
		for (int i = 0; i < leader_cards_number; i++) {
			JSONObject card = (JSONObject) this.jsonParser.parse(leader_cards_obj.get(i).toString());

			// ID
			int id = (int)(long) card.get("id");

			// ABILITY
			JSONObject ability_obj = (JSONObject) card.get("ability");
			LeaderAbility ability = createAbility(ability_obj);

			// VICTORYPOINTS
			int victory_points = (int)(long) card.get("victory_points");

			// REQUIREMENTS
			JSONObject requirements_obj = (JSONObject) card.get("requirements");

			leader_cards[i] = createLeaderCard(id, ability, victory_points, requirements_obj);
		}
		return leader_cards;
	}

	/**
	 * Parse the json file for the Cells
	 *
	 * @return an array containing all the Cells specified in the json file
	 */
	private Cell[] readAllCells() throws ParseException, IOException {
		Cell[] cells = new Cell[cells_number];

		InputStream is = getClass().getClassLoader().getResourceAsStream("json/cells.json");
		JSONObject jsonObject = (JSONObject) this.jsonParser.parse(new InputStreamReader(is));

		JSONArray cells_obj = (JSONArray) jsonObject.get("cells");
		for (int i = 0; i < cells_number; i++) {
			JSONObject cell = (JSONObject) this.jsonParser.parse(cells_obj.get(i).toString());

			// POSITION
			int position = (int)(long) cell.get("position");

			// VICTORYPOINTS
			int victory_points = (int)(long) cell.get("victory_points");

			// REPORT
			VaticanReports report = null;
			String report_str = cell.get("report").toString();
			if (!report_str.equals("null")) {
				report = VaticanReports.valueOf(report_str);
			}

			// POPE SPACE
			boolean pope_space = Boolean.parseBoolean(cell.get("pope_space").toString());

			// LAST CELL
			boolean last_cell = Boolean.parseBoolean(cell.get("last_cell").toString());

			cells[i] = new Cell(position, victory_points, report, pope_space, last_cell);
		}
		return cells;
	}

	/**
	 * Parse the json file for the Tiles
	 *
	 * @return an array containing all the Tiles specified in the json file
	 */
	private Tile[] readAllTiles() throws ParseException, IOException {
		Tile[] tiles = new Tile[tiles_number];

		InputStream is = getClass().getClassLoader().getResourceAsStream("json/tiles.json");
		JSONObject jsonObject = (JSONObject) this.jsonParser.parse(new InputStreamReader(is));

		JSONArray tiles_obj = (JSONArray) jsonObject.get("tiles");
		for (int i = 0; i < tiles_number; i++) {
			JSONObject tile = (JSONObject) this.jsonParser.parse(tiles_obj.get(i).toString());

			// REPORT
			VaticanReports report = VaticanReports.valueOf(tile.get("report").toString());

			// VICTORYPOINTS
			int victory_points = (int)(long) tile.get("victory_points");

			tiles[i] = new Tile(report, victory_points);
		}
		return tiles;
	}
}