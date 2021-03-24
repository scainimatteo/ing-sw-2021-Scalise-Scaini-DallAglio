package it.polimi.ingsw.model.game;

import java.util.stream.Collectors;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;

import org.json.simple.parser.*;
import org.json.simple.*;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.CardLevel;

import it.polimi.ingsw.model.player.track.Cell;

import it.polimi.ingsw.model.resources.Production;
import it.polimi.ingsw.model.resources.Resource;

public class Factory {
	private static Factory istance;
	private DevelopmentCard[] all_development_cards;
	private LeaderCard[] all_leader_cards;
	private Cell[] all_cells;

	private Factory() {
		this.all_development_cards = readAllDevelopmentCards();
		this.all_leader_cards = readAllLeaderCards();
		this.all_cells = readAllCells();
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

	@SuppressWarnings("unchecked")
	private Resource[] convertJsonArrayToResourceArray(JSONArray json_array) {
		List<Resource> resource_list = (List<Resource>)json_array.stream().map(x -> Resource.valueOf(x.toString())).collect(Collectors.toList());
		return resource_list.toArray(new Resource[resource_list.size()]);
	}

	private DevelopmentCard[] readAllDevelopmentCards() {
		int development_cards_number = 48;
		DevelopmentCard[] development_cards = new DevelopmentCard[development_cards_number];

		JSONParser jsonParser = new JSONParser();
		try {
			InputStream is = getClass().getClassLoader().getResourceAsStream("json/developmentcards.json");
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is));

			JSONArray development_cards_obj = (JSONArray) jsonObject.get("development_cards");
			for (int i = 0; i < development_cards_number; i++) {
				JSONObject card = (JSONObject) jsonParser.parse(development_cards_obj.get(i).toString());

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

				development_cards[i] = new DevelopmentCard(new_production, cost, cardlevel, id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return development_cards;
	}

	private LeaderCard[] readAllLeaderCards() {
		return null;
	}

	private Cell[] readAllCells() {
		return null;
	}
}
