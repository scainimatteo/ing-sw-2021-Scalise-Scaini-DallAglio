package it.polimi.ingsw.server.persistence;

import java.util.stream.Collectors;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;

import java.nio.file.Paths;
import java.nio.file.Path;

import org.json.simple.parser.*;
import org.json.simple.*;

import it.polimi.ingsw.model.card.DevelopmentCardsColor;
import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Table;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.model.game.sologame.DiscardDevelopmentCards;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossTwoSpaces;
import it.polimi.ingsw.model.game.sologame.MoveBlackCrossOneSpace;
import it.polimi.ingsw.model.game.sologame.SoloActionToken;
import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.sologame.SoloGame;
import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.DevelopmentCardsSlots;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.SoloFaithTrack;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Tile;
import it.polimi.ingsw.model.player.SoloPlayer;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.player.StrongBox;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.model.resources.Resource;

import it.polimi.ingsw.server.persistence.PersistenceUtil;

public class PersistenceParser {
	private static JSONParser jsonParser = new JSONParser();

	/**
	 * @param match_name the identifier of the saved match
	 * @return the number of Clients of the saved match
	 */
	public static int getClientsNumber(String match_name) throws ParseException, IOException {
		InputStream is = new FileInputStream(new File(PersistenceUtil.getPersistenceFileFromMatchName(match_name)));
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is));

		return (int)(long) jsonObject.get("clients_number");
	}

	/**
	 * Parse the json file for the Clients nickname
	 *
	 * @param match_name the identifier of the saved match
	 * @return an array containing all the Clients nickname of the saved match
	 */
	public static ArrayList<String> getAllClientsNickname(String match_name) throws ParseException, IOException {
		InputStream is = new FileInputStream(new File(PersistenceUtil.getPersistenceFileFromMatchName(match_name)));
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is));

		int clients_number = (int)(long) jsonObject.get("clients_number");
		ArrayList<String> clients_nickname = new ArrayList<String>();

		JSONArray clients_nickname_obj = (JSONArray) jsonObject.get("clients_nickname");
		for (int i = 0; i < clients_number; i++) {
			clients_nickname.add(clients_nickname_obj.get(i).toString());
		}
		return clients_nickname;
	}

	/**
	 *  Parse the entire match from the json file and create all the model classes
	 *
	 * @param match_name the identifier of the saved match
	 *  @return the Game as it's represented in the json file
	 */
	public static Game parseMatch(String match_name) throws ParseException, IOException {
		InputStream is = new FileInputStream(new File(PersistenceUtil.getPersistenceFileFromMatchName(match_name)));
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is));

		ArrayList<Player> players = parsePlayers((JSONArray) jsonObject.get("players"));
		Turn turn = parseTurn((JSONObject) jsonObject.get("turn"), players);
		return parseGame((JSONObject) jsonObject.get("game"), turn, players);
	}

	/**
	 *  Parse the entire match from the json file and create all the model classes for a SoloGame
	 *
	 * @param match_name the identifier of the saved match
	 *  @return the SoloGame as it's represented in the json file
	 */
	public static SoloGame parseSoloMatch(String match_name) throws ParseException, IOException {
		InputStream is = new FileInputStream(new File(PersistenceUtil.getPersistenceFileFromMatchName(match_name)));
		JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is));

		ArrayList<Player> players = new ArrayList<Player>();
		SoloPlayer player = parseSoloPlayer((JSONObject) jsonObject.get("player"));
		players.add(player);

		Turn turn = parseTurn((JSONObject) jsonObject.get("turn"), players);
		return parseSoloGame((JSONObject) jsonObject.get("game"), turn, players);
	}

	/**
	 * TURN
	 */

	private static Turn parseTurn(JSONObject turn_object, ArrayList<Player> players) throws ParseException {
		Turn turn = createTurn(players, turn_object.get("active_player").toString());
		turn.setDoneAction(Boolean.parseBoolean(turn_object.get("action_done").toString()));
		turn.setFinal(Boolean.parseBoolean(turn_object.get("final_round").toString()));
		turn.setupDone(Boolean.parseBoolean(turn_object.get("setup_done").toString()));
		turn.setDiscard(Boolean.parseBoolean(turn_object.get("must_discard").toString()));
		turn.addProducedResources(convertJsonArrayToResourceArray((JSONArray) turn_object.get("produced_resources")));
		turn.addRequiredResources(convertJsonArrayToResourceArray((JSONArray) turn_object.get("required_resources")));
		return turn;
	}

	private static Turn createTurn(ArrayList<Player> players, String nickname) throws ParseException {
		Turn turn = null;
		for (Player p: players) {
			if (p.getNickname().equals(nickname)) {
				turn = new Turn(p);
				break;
			}
		}
		if (turn == null) {
			throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
		}
		return turn;
	}

	/**
	 * PLAYERS
	 */

	private static ArrayList<Player> parsePlayers(JSONArray players_array) throws ParseException, IOException {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Object player_object: players_array) {
			players.add(parsePlayer((JSONObject) player_object));
		}
		return players;
	}

	private static Player parsePlayer(JSONObject player_object) throws ParseException, IOException {
		String nickname = player_object.get("nickname").toString();
		FaithTrack track = createFaithTrack(player_object);
		Warehouse warehouse = createWarehouse((JSONArray) player_object.get("warehouse"));
		StrongBox strongbox = createStrongBox((JSONArray) player_object.get("strongbox"));
		DevelopmentCardsSlots development_card_slots = createDevelopmentCardsSlots((JSONArray) player_object.get("development_cards_slots"));
		ArrayList<LeaderCard> leader_cards = createLeaderCards((JSONArray) player_object.get("leader_cards"));
		return new Player(nickname, track, warehouse, strongbox, development_card_slots, leader_cards);
	}

	private static Tile createTile(JSONObject tile_object) throws ParseException, IOException {
		VaticanReports report = VaticanReports.valueOf(tile_object.get("report").toString());
		int victory_points = (int)(long) tile_object.get("victory_points");
		Tile tile = new Tile(report, victory_points);

		if (Boolean.parseBoolean(tile_object.get("activated").toString())) {
			tile.activateVaticanReport();
		}
		return tile;
	}

	private static FaithTrack createFaithTrack(JSONObject player_object) throws ParseException, IOException {
		int marker_position = (int)(long) player_object.get("marker_position");

		JSONArray tiles_array = (JSONArray) player_object.get("tiles");
		Tile[] tiles = new Tile[tiles_array.size()];
		for (int i = 0; i < tiles_array.size(); i++) {
			tiles[i] = createTile((JSONObject) jsonParser.parse(tiles_array.get(i).toString()));
		}

		return new FaithTrack(Factory.getInstance().getAllCells(), tiles, marker_position);
	}

	private static Warehouse createWarehouse(JSONArray warehouse_array) throws ParseException {
		ArrayList<Resource> top_resource = new ArrayList<Resource>();
		ArrayList<Resource> middle_resources = new ArrayList<Resource>();
		ArrayList<Resource> bottom_resources = new ArrayList<Resource>();

		for (int i = 0; i < warehouse_array.size(); i++) {
			JSONObject shelf_object = (JSONObject) jsonParser.parse(warehouse_array.get(i).toString());
			switch (shelf_object.get("shelf").toString()) {
				case "TOP":
					top_resource.addAll(convertJsonArrayToResourceArray((JSONArray) shelf_object.get("resources")));
					break;
				case "MIDDLE":
					middle_resources.addAll(convertJsonArrayToResourceArray((JSONArray) shelf_object.get("resources")));
					break;
				case "BOTTOM":
					bottom_resources.addAll(convertJsonArrayToResourceArray((JSONArray) shelf_object.get("resources")));
					break;
				default:
					throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
			}
		}
		return new Warehouse(top_resource, middle_resources, bottom_resources);
	}

	private static StrongBox createStrongBox(JSONArray strongbox_array) throws ParseException {
		HashMap<Resource, Integer> storage = new HashMap<Resource, Integer>();

		for (int i = 0; i < strongbox_array.size(); i++) {
			JSONObject resource_object = (JSONObject) jsonParser.parse(strongbox_array.get(i).toString());

			Resource type = Resource.valueOf(resource_object.get("type").toString());
			int number_of_resources = (int)(long) resource_object.get("number");
			storage.put(type, number_of_resources);
		}

		return new StrongBox(storage);
	}

	private static DevelopmentCardsSlots createDevelopmentCardsSlots(JSONArray dcs_array) throws ParseException, IOException {
		Table<DevelopmentCard> slots = new Table<DevelopmentCard>(1, 3);
		ArrayList<DevelopmentCard> first_slot = new ArrayList<DevelopmentCard>();
		ArrayList<DevelopmentCard> second_slot = new ArrayList<DevelopmentCard>();
		ArrayList<DevelopmentCard> third_slot = new ArrayList<DevelopmentCard>();

		for (int i = 0; i < dcs_array.size(); i++) {
			JSONObject slot_object = (JSONObject) jsonParser.parse(dcs_array.get(i).toString());
			int slot_number = (int)(long) slot_object.get("slot_number");

			JSONArray slot_array = (JSONArray) slot_object.get("cards");
			for (int j = 0; j < slot_array.size(); j++) {
				int id = (int)(long) slot_array.get(i);
				switch (slot_number) {
					case 1:
						first_slot.add(getDevelopmentCardFromId(id));
						break;
					case 2:
						second_slot.add(getDevelopmentCardFromId(id));
						break;
					case 3:
						third_slot.add(getDevelopmentCardFromId(id));
						break;
					default:
						throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
				}
			}
		}
		slots.addDeck(new Deck<DevelopmentCard>(3, first_slot), 0, 0);
		slots.addDeck(new Deck<DevelopmentCard>(3, second_slot), 0, 1);
		slots.addDeck(new Deck<DevelopmentCard>(3, third_slot), 0, 2);
		return new DevelopmentCardsSlots(slots);
	}

	private static ArrayList<LeaderCard> createLeaderCards(JSONArray leader_cards_array) throws ParseException, IOException {
		ArrayList<LeaderCard> leader_cards = new ArrayList<LeaderCard>();

		for (int i = 0; i < leader_cards_array.size(); i++) {
			JSONObject leader_card_object = (JSONObject) jsonParser.parse(leader_cards_array.get(i).toString());
			LeaderCard leader_card = getLeaderCardFromId((int)(long) leader_card_object.get("id"));

			if(Boolean.parseBoolean(leader_card_object.get("activated").toString())) {
				leader_card.activateLeaderCard();
			}

			// ExtraSpaceAbility
			LeaderAbility ability = leader_card.getAbility();
			if (ability.checkAbility(new ExtraSpaceAbility(Resource.COIN))) {
				addExtraSpaceResources((ExtraSpaceAbility) ability, (int)(long) leader_card_object.get("storage"));
			}

			leader_cards.add(leader_card);
		}
		return leader_cards;
	}

	private static void addExtraSpaceResources(ExtraSpaceAbility extra_space, int number_of_resources) {
		Resource type = extra_space.getResourceType();
		ArrayList<Resource> storage = new ArrayList<Resource>();
		for (int i = 0; i < number_of_resources; i++) {
			storage.add(type);
		}
		extra_space.storeExtra(storage);
	}

	/**
	 * GAME
	 */

	private static Game parseGame(JSONObject game_object, Turn turn, ArrayList<Player> players) throws ParseException, IOException {
		Market market = createMarket((JSONArray) game_object.get("market"), (JSONObject) game_object.get("free_marble"));
		DevelopmentCardsOnTable development_cards_on_table = createDevelopmentCardsOnTable((JSONArray) game_object.get("development_cards_on_table"));
		return new Game(players, market, development_cards_on_table, turn);
	}

	private static Market createMarket(JSONArray market_array, JSONObject free_marble_object) {
		Resource[][] market_board = new Resource[Market.dim_cols][Market.dim_rows];
		for (int i = 0; i < market_array.size(); i++) {
			JSONArray row = (JSONArray) market_array.get(i);
			for (int j = 0; j < row.size(); j++) {
				if (row.get(j) == null) {
					market_board[i][j] = null;
				} else {
					market_board[i][j] = Resource.valueOf(row.get(j).toString());
				}
			}
		}
		return new Market(market_board, createFreeMarble(free_marble_object));
	}

	private static Resource createFreeMarble(JSONObject free_marble_object) {
		if (free_marble_object == null) {
			return null;
		} else {
			return Resource.valueOf(free_marble_object.toString());
		}
	}

	private static DevelopmentCardsOnTable createDevelopmentCardsOnTable(JSONArray dcot_array) throws ParseException, IOException {
		Table<DevelopmentCard> development_cards_table = new Table<DevelopmentCard>(DevelopmentCardsOnTable.dim_rows, DevelopmentCardsOnTable.dim_cols);

		for (int i = 0; i < dcot_array.size(); i++) {
			JSONArray deck_array = (JSONArray) jsonParser.parse(dcot_array.get(i).toString());

			for (int j = 0; j < deck_array.size(); j++) {
				Deck<DevelopmentCard> deck = new Deck<DevelopmentCard>(4);

				JSONArray row_array = (JSONArray) jsonParser.parse(deck_array.get(j).toString());
				for (int k = row_array.size() - 1; k >= 0; k--) {
					int id = (int)(long) row_array.get(k);
					deck.add(getDevelopmentCardFromId(id));
				}
				development_cards_table.addDeck(deck, i, j);
			}
		}
		return new DevelopmentCardsOnTable(development_cards_table);
	}

	/**
	 * SOLOPLAYER
	 */

	private static SoloPlayer parseSoloPlayer(JSONObject player_object) throws ParseException, IOException {
		String nickname = player_object.get("nickname").toString();
		SoloFaithTrack track = createSoloFaithTrack(player_object);
		Warehouse warehouse = createWarehouse((JSONArray) player_object.get("warehouse"));
		StrongBox strongbox = createStrongBox((JSONArray) player_object.get("strongbox"));
		DevelopmentCardsSlots development_card_slots = createDevelopmentCardsSlots((JSONArray) player_object.get("development_cards_slots"));
		ArrayList<LeaderCard> leader_cards = createLeaderCards((JSONArray) player_object.get("leader_cards"));
		return new SoloPlayer(nickname, track, warehouse, strongbox, development_card_slots, leader_cards);
	}

	private static SoloFaithTrack createSoloFaithTrack(JSONObject player_object) throws ParseException, IOException {
		int marker_position = (int)(long) player_object.get("marker_position");
		int black_marker_position = (int)(long) player_object.get("black_marker_position");

		JSONArray tiles_array = (JSONArray) player_object.get("tiles");
		Tile[] tiles = new Tile[tiles_array.size()];
		for (int i = 0; i < tiles_array.size(); i++) {
			tiles[i] = createTile((JSONObject) jsonParser.parse(tiles_array.get(i).toString()));
		}

		return new SoloFaithTrack(Factory.getInstance().getAllCells(), tiles, marker_position, black_marker_position);
	}

	/**
	 * SOLOGAME
	 */

	private static SoloGame parseSoloGame(JSONObject game_object, Turn turn, ArrayList<Player> players) throws ParseException, IOException {
		Market market = createMarket((JSONArray) game_object.get("market"), (JSONObject) game_object.get("free_marble"));
		DevelopmentCardsOnTable development_cards_on_table = createDevelopmentCardsOnTable((JSONArray) game_object.get("development_cards_on_table"));
		ArrayDeque<SoloActionToken> active_tokens = createActiveTokens((JSONArray) game_object.get("active_tokens"));
		SoloActionToken last_token = createSoloActionToken((JSONObject) game_object.get("last_token"));
		return new SoloGame(players, market, development_cards_on_table, turn, Factory.getInstance().getAllSoloActionTokens(), active_tokens, last_token);
	}

	private static ArrayDeque<SoloActionToken> createActiveTokens(JSONArray active_tokens_array) throws ParseException {
		ArrayDeque<SoloActionToken> active_tokens = new ArrayDeque<SoloActionToken>();
		for (int i = active_tokens_array.size() - 1; i >= 0; i--) {
			active_tokens.push(createSoloActionToken((JSONObject) active_tokens_array.get(i)));
		}
		return active_tokens;
	}

	//TODO: it's the same of createSoloActionToken in the Factory
	private static SoloActionToken createSoloActionToken(JSONObject token_object) throws ParseException {
		if (token_object == null) {
			return null;
		}

		String type = token_object.get("type").toString();
		switch (type) {
			case "MOVEBLACKCROSSTWOSPACES":
				return new MoveBlackCrossTwoSpaces();
			case "MOVEBLACKCROSSONESPACE":
				return new MoveBlackCrossOneSpace();
			case "DISCARDDEVELOPMENTCARDS":
				try {
					String color_type = token_object.get("color").toString();
					DevelopmentCardsColor color = DevelopmentCardsColor.valueOf(color_type);
					return new DiscardDevelopmentCards(color);
				} catch (IllegalArgumentException e) {
					// if the color is not a DevelopmentCardsColor
					throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
				}
			default:
				throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
		}
	}

	@SuppressWarnings("unchecked")
	/**
	 * @param json_array the JSONArray to convert
	 * @return a Resource array with all the elements of json_array
	 */
	private static ArrayList<Resource> convertJsonArrayToResourceArray(JSONArray json_array) {
		// map all the Objects in json_array to Resources using toString() and valueOf()
		List<Resource> resource_list = (List<Resource>) json_array.stream().map(x -> Resource.valueOf(x.toString())).collect(Collectors.toList());
		return new ArrayList<Resource>(resource_list);
	}

	/**
	 * @param id the identifier of the DevelopmentCard as specified in the json file
	 * @return the DevelopmentCard with that specified id
	 */
	private static DevelopmentCard getDevelopmentCardFromId(int id) throws ParseException, IOException {
		DevelopmentCard[] all_development_cards = Factory.getInstance().getAllDevelopmentCards();
		for (DevelopmentCard card: all_development_cards) {
			if (card.getId() == id) {
				return card;
			}
		}

		throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
	}

	/**
	 * @param id the identifier of the LeaderCard as specified in the json file
	 * @return the LeaderCard with that specified id
	 */
	private static LeaderCard getLeaderCardFromId(int id) throws ParseException, IOException {
		LeaderCard[] all_leader_cards = Factory.getInstance().getAllLeaderCards();
		for (LeaderCard card: all_leader_cards) {
			if (card.getId() == id) {
				return card;
			}
		}

		throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
	}
}
