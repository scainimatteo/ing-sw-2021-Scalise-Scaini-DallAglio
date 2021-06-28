package it.polimi.ingsw.server.persistence;

import java.util.stream.Collectors;
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

import it.polimi.ingsw.model.card.ExtraSpaceAbility;
import it.polimi.ingsw.model.card.DevelopmentCard;
import it.polimi.ingsw.model.card.LeaderAbility;
import it.polimi.ingsw.model.card.LeaderCard;
import it.polimi.ingsw.model.card.Table;
import it.polimi.ingsw.model.card.Deck;

import it.polimi.ingsw.model.game.DevelopmentCardsOnTable;
import it.polimi.ingsw.model.game.Factory;
import it.polimi.ingsw.model.game.Market;
import it.polimi.ingsw.model.game.Turn;
import it.polimi.ingsw.model.game.Game;

import it.polimi.ingsw.model.player.DevelopmentCardsSlots;
import it.polimi.ingsw.model.player.track.VaticanReports;
import it.polimi.ingsw.model.player.track.FaithTrack;
import it.polimi.ingsw.model.player.track.Tile;
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
	 * TURN
	 */

	private static Turn parseTurn(JSONObject turn_object, ArrayList<Player> players) throws ParseException {
		Turn turn = createTurn(players, turn_object.get("active_player").toString());
		turn.setDoneAction(Boolean.parseBoolean(turn_object.get("action_done").toString()));
		turn.setFinal(Boolean.parseBoolean(turn_object.get("final_round").toString()));
		turn.setInitialized(Boolean.parseBoolean(turn_object.get("initialized").toString()));
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

	private static FaithTrack createFaithTrack(JSONObject player_object) throws ParseException, IOException {
		int marker_position = (int)(long) player_object.get("marker_position");

		JSONArray tiles_array = (JSONArray) player_object.get("tiles");
		Tile[] tiles = new Tile[tiles_array.size()];
		for (int i = 0; i < tiles_array.size(); i++) {
			JSONObject tile = (JSONObject) jsonParser.parse(tiles_array.get(i).toString());

			VaticanReports report = VaticanReports.valueOf(tile.get("report").toString());
			int victory_points = (int)(long) tile.get("victory_points");
			tiles[i] = new Tile(report, victory_points);

			if (Boolean.parseBoolean(tile.get("activated").toString())) {
				tiles[i].activateVaticanReport();
			}
		}

		return new FaithTrack(Factory.getIstance().getAllCells(), tiles, marker_position);
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
		Market market = createMarket((JSONArray) game_object.get("market"), game_object.get("free_marble").toString());
		DevelopmentCardsOnTable development_cards_on_table = createDevelopmentCardsOnTable((JSONArray) game_object.get("development_cards_on_table"));
		return new Game(players, market, development_cards_on_table, turn);
	}

	private static Market createMarket(JSONArray market_array, String free_marble_string) {
		Resource free_marble = Resource.valueOf(free_marble_string);
		//TODO avoid constants
		Resource[][] market_board = new Resource[3][4];
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
		return new Market(market_board, free_marble);
	}

	private static DevelopmentCardsOnTable createDevelopmentCardsOnTable(JSONArray dcot_array) throws ParseException, IOException {
		Table<DevelopmentCard> development_cards_table = new Table<DevelopmentCard>(4, 3);

		for (int i = 0; i < dcot_array.size(); i++) {
			JSONArray deck_array = (JSONArray) jsonParser.parse(dcot_array.get(i).toString());

			Deck<DevelopmentCard> deck = new Deck<DevelopmentCard>(4);
			for (int j = 0; j < deck_array.size(); j++) {
				int id = (int)(long) deck_array.get(j);
				deck.add(getDevelopmentCardFromId(id));
			}
			// TODO rewrite using matrixes
			development_cards_table.addDeck(deck, 0, 0);
		}
		return new DevelopmentCardsOnTable(development_cards_table);
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
		DevelopmentCard[] all_development_cards = Factory.getIstance().getAllDevelopmentCards();
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
		LeaderCard[] all_leader_cards = Factory.getIstance().getAllLeaderCards();
		for (LeaderCard card: all_leader_cards) {
			if (card.getId() == id) {
				return card;
			}
		}

		throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
	}
}
