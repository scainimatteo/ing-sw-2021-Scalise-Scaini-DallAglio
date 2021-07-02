package it.polimi.ingsw.server.persistence;

import java.util.ArrayDeque;
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

import it.polimi.ingsw.model.game.sologame.DiscardDevelopmentCards;
import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.sologame.SoloGame;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.DevelopmentCardsSlots;
import it.polimi.ingsw.model.player.track.SoloFaithTrack;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.SoloPlayer;
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
	 * Dump all the whole SoloGame in the appropriate json file
	 *
	 * @param match_name the identifier of the match to save
	 * @param sologame the SoloGame to write onto the file
	 */
	public static void writeSoloPersistenceFile(String match_name, SoloGame sologame) {
		JSONObject persistent_match_object = new JSONObject();

		populateJSON(persistent_match_object, sologame);

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
	 * Populate the JSONObject with the whole content of the SoloGame
	 *
	 * @param match_object the JSONObject to write the Game to
	 * @param sologame the SoloGame to write onto the JSONObject
	 */
	private static void populateJSON(JSONObject match_object, SoloGame sologame) {
		populateJSONClient(match_object, sologame.getPlayers());
		populateJSONSoloPlayer(match_object, (SoloPlayer) sologame.getPlayers().get(0));
		populateJSONTurn(match_object, sologame.getTurn());
		populateJSONSoloGame(match_object, sologame);
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
		player_object.put("strongbox", populateJSONStrongBox(player.getStrongBox()));
		player_object.put("development_cards_slots", populateJSONDevelopmentCardsSlots(player.getDevelopmentCardsSlots()));
		player_object.put("leader_cards", populateJSONLeaderCards(player.getLeaderCards()));
		return player_object;
	}

	private static void populateJSONFaithTrack(JSONObject player_object, FaithTrack track) {
		player_object.put("marker_position", track.getMarkerPosition());

		JSONArray tiles_array = new JSONArray();
		for (Tile tile: track.getTiles()) {
			tiles_array.add(populateJSONTile(tile));
		}
		player_object.put("tiles", tiles_array);
	}

	private static JSONObject populateJSONTile(Tile tile) {
		if (tile == null) {
			return null;
		}

		JSONObject tile_object = new JSONObject();
		tile_object.put("activated", tile.isActive());
		tile_object.put("victory_points", tile.getVictoryPoints());
		tile_object.put("report", tile.getVaticanReport().toString());
		return tile_object;
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
		strongbox_array.add(shield_object);
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
		turn_object.put("setup_done", turn.hasDoneSetup());
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
			JSONArray row_array = new JSONArray();
			for (int j = 0; j < 4; j++) {
				JSONArray deck_array = new JSONArray();
				for (DevelopmentCard dev_card: development_cards_on_table.getDeckAsArrayList(i, j)) {
					deck_array.add(dev_card.getId());
				}
				row_array.add(deck_array);
			}
			dcot_array.add(row_array);
		}

		return dcot_array;
	}

	/**
	 * SOLOPLAYER
	 */

	private static void populateJSONSoloPlayer(JSONObject match_object, SoloPlayer player) {
		JSONObject player_object = new JSONObject();
		player_object.put("nickname", player.getNickname());
		populateJSONSoloFaithTrack(player_object, (SoloFaithTrack) player.getFaithTrack());
		player_object.put("warehouse", populateJSONWarehouse(player.getWarehouse()));
		player_object.put("strongbox", populateJSONStrongBox(player.getStrongBox()));
		player_object.put("development_cards_slots", populateJSONDevelopmentCardsSlots(player.getDevelopmentCardsSlots()));
		player_object.put("leader_cards", populateJSONLeaderCards(player.getLeaderCards()));
		match_object.put("player", player_object);
	}

	private static void populateJSONSoloFaithTrack(JSONObject player_object, SoloFaithTrack track) {
		player_object.put("marker_position", track.getMarkerPosition());
		player_object.put("black_marker_position", track.getBlackMarkerPosition());

		JSONArray tiles_array = new JSONArray();
		for (Tile tile: track.getTiles()) {
			tiles_array.add(populateJSONTile(tile));
		}
		player_object.put("tiles", tiles_array);
	}

	/**
	 * SOLOGAME
	 */

	private static void populateJSONSoloGame(JSONObject match_object, SoloGame sologame) {
		JSONObject game_object = new JSONObject();
		populateJSONMarket(game_object, sologame.getMarket());
		game_object.put("development_cards_on_table", populateJSONDevelopmentCardsOnTable(sologame.getDevelopmentCardsOnTable()));
		game_object.put("active_tokens", populateJSONActiveTokens(sologame.getActiveTokens()));
		game_object.put("last_token", populateJSONSoloActionToken(sologame.getLastToken()));
		match_object.put("game", game_object);
	}

	private static JSONArray populateJSONActiveTokens(ArrayDeque<SoloActionToken> active_tokens) {
		JSONArray active_tokens_array = new JSONArray();
		while (active_tokens.size() != 0) {
			active_tokens_array.add(populateJSONSoloActionToken(active_tokens.pop()));
		}
		return active_tokens_array;
	}

	private static JSONObject populateJSONSoloActionToken(SoloActionToken token) {
		if (token == null) {
			return null;
		}

		JSONObject token_object = new JSONObject();
		token_object.put("type", token.getType());

		if (token.getType().equals("DISCARDDEVELOPMENTCARDS")) {
			token_object.put("color", ((DiscardDevelopmentCards) token).getColor().toString());
		}

		return token_object;
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
