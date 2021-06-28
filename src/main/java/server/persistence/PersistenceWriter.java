package it.polimi.ingsw.server.persistence;

import java.util.ArrayList;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.LeaderCard;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.DevelopmentCardsSlots;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.server.persistence.PersistenceUtil;

@SuppressWarnings("unchecked")
public class PersistenceWriter {
	/**
	 * Dump all the whole Game in the appropriate json file
	 *
	 * @param match_name the identifier of the match to save
	 * @param game the Game to write onto the file
	 */
	public static void writePersistenceFile(String match_name, Game game) {
		JSONObject persistent_match_object = new JSONObject();

		populateJSON(persistent_match_object, game);

		String filename = PersistenceUtil.getPersistenceFileFromMatchName(match_name);
		try {
			FileWriter file = new FileWriter(filename);
			file.write(persistent_match_object.toJSONString()); 
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Populate the JSONObject with the whole content of the Game
	 *
	 * @param match_object the JSONObject to write the Game to
	 * @param game the Game to write onto the JSONObject
	 */
	private static void populateJSON(JSONObject match_object, Game game) {
		populateJSONClient(match_object, game.getPlayers());
		populateJSONPlayers(match_object, game.getPlayers());
		populateJSONTurn(match_object, game.getTurn());
		populateJSONGame(match_object, game);
	}

	/**
	 * CLIENT
	 */

	private static void populateJSONClient(JSONObject match_object, ArrayList<Player> players) {
		match_object.put("clients_number", players.size());
		JSONArray nicknames = new JSONArray();
		for (Player p: players) {
			nicknames.add(p.getNickname());
		}
		match_object.put("clients_nickname", nicknames);
	}

	/**
	 * PLAYERS
	 */

	private static void populateJSONPlayers(JSONObject match_object, ArrayList<Player> players) {
		JSONArray players_array = new JSONArray();
		for (Player p: players) {
			players_array.add(populateJSONPlayer(p));
		}
		match_object.put("players", players_array);
	}

	private static JSONObject populateJSONPlayer(Player player) {
		JSONObject player_object = new JSONObject();
		player_object.put("nickname", player.getNickname());
		populateJSONFaithTrack(player_object, player.getFaithTrack());
		player_object.put("warehouse", populateJSONWarehouse(player.getWarehouse()));
		player_object.put("strongbox", populateJSONStrongBox(player.getPlayerStrongBox()));
		player_object.put("development_cards_slots", populateJSONDevelopmentCardsSlots(player.getDevelopmentCardsSlots()));
		player_object.put("leader_cards", populateJSONLeaderCards(player.getLeaderCards()));
		return player_object;
	}

	private static void populateJSONFaithTrack(JSONObject player_object, FaithTrack track) {
		player_object.put("marker_position", track.getMarkerPosition());

		JSONArray tiles_array = new JSONArray();
		for (Tile tile: track.getTiles()) {
			JSONObject tile_object = new JSONObject();
			tile_object.put("activated", tile.isActive());
			tile_object.put("victory_points", tile.getVictoryPoints());
			tile_object.put("report", tile.getVaticanReport().toString());
			tiles_array.add(tile_object);
		}
		player_object.put("tiles", tiles_array);
	}

	private static JSONArray populateJSONWarehouse(Warehouse warehouse) {
		JSONArray warehouse_array = new JSONArray();

		JSONObject top_object = new JSONObject();
		top_object.put("shelf", "TOP");
		JSONArray top_array = new JSONArray();
		for (Resource res: warehouse.getTopResource()) {
			top_array.add(res.toString());
		}
		top_object.put("resources", top_array);

		JSONObject middle_object = new JSONObject();
		middle_object.put("shelf", "MIDDLE");
		JSONArray middle_array = new JSONArray();
		for (Resource res: warehouse.getMiddleResources()) {
			middle_array.add(res.toString());
		}
		middle_object.put("resources", middle_array);

		JSONObject bottom_object = new JSONObject();
		bottom_object.put("shelf", "BOTTOM");
		JSONArray bottom_array = new JSONArray();
		for (Resource res: warehouse.getBottomResources()) {
			bottom_array.add(res.toString());
		}
		bottom_object.put("resources", bottom_array);

		warehouse_array.add(top_object);
		warehouse_array.add(middle_object);
		warehouse_array.add(bottom_object);
		return warehouse_array;
	}

	private static JSONArray populateJSONStrongBox(StrongBox strongbox) {
		JSONArray strongbox_array = new JSONArray();

		JSONObject coin_object = new JSONObject();
		coin_object.put("type", "COIN");
		coin_object.put("number", strongbox.get(Resource.COIN));

		JSONObject shield_object = new JSONObject();
		shield_object.put("type", "SHIELD");
		shield_object.put("number", strongbox.get(Resource.SHIELD));

		JSONObject servant_object = new JSONObject();
		servant_object.put("type", "SERVANT");
		servant_object.put("number", strongbox.get(Resource.SERVANT));

		JSONObject stone_object = new JSONObject();
		stone_object.put("type", "STONE");
		stone_object.put("number", strongbox.get(Resource.STONE));

		strongbox_array.add(coin_object);
		strongbox_array.add(stone_object);
		strongbox_array.add(servant_object);
		strongbox_array.add(stone_object);
		return strongbox_array;
	}

	private static JSONArray populateJSONDevelopmentCardsSlots(DevelopmentCardsSlots development_cards_slots) {
		JSONArray dcs_array = new JSONArray();

		JSONObject first_slot_object = new JSONObject();
		first_slot_object.put("slot_number", 1);
		JSONArray first_slot_array = new JSONArray();
		for (DevelopmentCard dev_card: development_cards_slots.getDeckAsArrayList(0, 0)) {
			first_slot_array.add(dev_card.getId());
		}
		first_slot_object.put("cards", first_slot_array);

		JSONObject second_slot_object = new JSONObject();
		second_slot_object.put("slot_number", 2);
		JSONArray second_slot_array = new JSONArray();
		for (DevelopmentCard dev_card: development_cards_slots.getDeckAsArrayList(0, 1)) {
			second_slot_array.add(dev_card.getId());
		}
		second_slot_object.put("cards", second_slot_array);

		JSONObject third_slot_object = new JSONObject();
		third_slot_object.put("slot_number", 3);
		JSONArray third_slot_array = new JSONArray();
		for (DevelopmentCard dev_card: development_cards_slots.getDeckAsArrayList(0, 2)) {
			third_slot_array.add(dev_card.getId());
		}
		third_slot_object.put("cards", third_slot_array);

		dcs_array.add(first_slot_object);
		dcs_array.add(second_slot_object);
		dcs_array.add(third_slot_object);
		return dcs_array;
	}

	private static JSONArray populateJSONLeaderCards(ArrayList<LeaderCard> leader_cards) {
		JSONArray leader_cards_array = new JSONArray();

		for (LeaderCard leader_card: leader_cards) {
			JSONObject leader_card_object = new JSONObject();
			leader_card_object.put("id", leader_card.getId());
			leader_card_object.put("activated", leader_card.isActive());

			// ExtraSpaceAbility
			LeaderAbility ability = leader_card.getAbility();
			if (ability.checkAbility(new ExtraSpaceAbility(Resource.COIN))) {
				leader_card_object.put("storage", ((ExtraSpaceAbility) ability).peekResources());
			}

			leader_cards_array.add(leader_card_object);
		}

		return leader_cards_array;
	}

	/**
	 * TURN
	 */

	private static void populateJSONTurn(JSONObject match_object, Turn turn) {
		JSONObject turn_object = new JSONObject();

		turn_object.put("active_player", turn.getNickname());
		turn_object.put("action_done", turn.hasDoneAction());
		turn_object.put("final_round", turn.isFinal());
		turn_object.put("initialized", turn.isInitialized());
		turn_object.put("must_discard", turn.mustDiscard());

		JSONArray produced_array = new JSONArray();
		for (Resource res: turn.getProducedResources()) {
			produced_array.add(res.toString());
		}
		turn_object.put("produced_resources", produced_array);

		JSONArray required_array = new JSONArray();
		for (Resource res: turn.getRequiredResources()) {
			required_array.add(res.toString());
		}
		turn_object.put("required_resources", required_array);

		match_object.put("turn", turn_object);
	}

	/**
	 * GAME
	 */

	private static void populateJSONGame(JSONObject match_object, Game game) {
		JSONObject game_object = new JSONObject();
		populateJSONMarket(game_object, game.getMarket());
		game_object.put("development_cards_on_table", populateJSONDevelopmentCardsOnTable(game.getDevelopmentCardsOnTable()));
		match_object.put("game", game_object);
	}

	private static void populateJSONMarket(JSONObject game_object, Market market) {
		game_object.put("free_marble", getResourceAsStringOrNull(market.getFreeMarble()));

		JSONArray market_array = new JSONArray();
		for (Resource[] market_row: market.peekMarket()) {
			JSONArray market_row_array = new JSONArray();

			for (Resource resource: market_row) {
				market_row_array.add(getResourceAsStringOrNull(resource));
			}

			market_array.add(market_row_array);
		}

		game_object.put("market", market_array);
	}

	private static JSONArray populateJSONDevelopmentCardsOnTable(DevelopmentCardsOnTable development_cards_on_table) {
		JSONArray dcot_array = new JSONArray();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				JSONArray deck_array = new JSONArray();
				for (DevelopmentCard dev_card: development_cards_on_table.getDeckAsArrayList(i, j)) {
					deck_array.add(dev_card.getId());
				}
				dcot_array.add(deck_array);
			}
		}

		return dcot_array;
	}

	/**
	 * @param res a Resource from the market that could be null
	 * @return a String representing the Resource or null if res was null
	 */
	private static String getResourceAsStringOrNull(Resource res) {
		if (res == null) {
			return null;
		} else {
			return res.toString();
		}
	}

}
