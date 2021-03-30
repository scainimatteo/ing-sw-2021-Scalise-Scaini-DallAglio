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

public class Factory {
	private static Factory istance;
	private DevelopmentCard[] all_development_cards;
	private LeaderCard[] all_leader_cards;
	private Cell[] all_cells;
	private Tile[] all_tiles;
	private JSONParser jsonParser;
	private final int development_cards_number = 48;
	private final int leader_cards_number = 16;
	private final int cells_number = 24;
	private final int tiles_number = 0;

	private Factory() {
		this.jsonParser = new JSONParser();
		this.all_development_cards = readAllDevelopmentCards();
		this.all_leader_cards = readAllLeaderCards();
		this.all_cells = readAllCells();
		this.all_tiles = readAllTiles();
	}

	public static Factory getIstance() {
		if (istance == null) {
			istance = new Factory();
		}
		return istance;
	}

	public DevelopmentCard[] getAllDevelopmentCards() {
		return this.all_development_cards;
	}

	public LeaderCard[] getAllLeaderCards() {
		return this.all_leader_cards;
	}

	public Cell[] getAllCells() {
		return this.all_cells;
	}

	public Tile[] getAllTiles() {
		return this.all_tiles;
	}

	@SuppressWarnings("unchecked")
	private Resource[] convertJsonArrayToResourceArray(JSONArray json_array) {
		List<Resource> resource_list = (List<Resource>)json_array.stream().map(x -> Resource.valueOf(x.toString())).collect(Collectors.toList());
		return resource_list.toArray(new Resource[resource_list.size()]);
	}

	private DevelopmentCard[] readAllDevelopmentCards() {
		DevelopmentCard[] development_cards = new DevelopmentCard[development_cards_number];

		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return development_cards;
	}

	private LeaderAbility createAbility(JSONObject ability_obj) {
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
				// TODO: Exception
				return null;
		}
	}

	private LeaderCard createLeaderCard(int id, LeaderAbility ability, int victory_points, JSONObject requirements_obj) throws ParseException {
		String requirements_type = requirements_obj.get("type").toString();
		switch (requirements_type) {
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
			case "RESOURCES":
				JSONArray resources_arr = (JSONArray) requirements_obj.get("resources");
				Resource[] requirements_resources = convertJsonArrayToResourceArray(resources_arr);
				return new LeaderCardResourcesCost(victory_points, ability, requirements_resources, id);
			default:
				// TODO: Exception
				return null;
		}
	}

	private LeaderCard[] readAllLeaderCards() {
		LeaderCard[] leader_cards = new LeaderCard[leader_cards_number];

		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return leader_cards;
	}

	private Cell[] readAllCells() {
		Cell[] cells = new Cell[cells_number];

		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cells;
	}

	private Tile[] readAllTiles() {
		return null;
	}
}
